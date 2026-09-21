package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.controller.cenas.CutsceneController;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.narrativa.Capitulo;
import com.exa863.anselmo_adna.controller.NarrativaController;
import com.exa863.anselmo_adna.model.narrativa.TipoGatilho;
import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.model.world.AcaoMenu;
import com.exa863.anselmo_adna.model.world.Local;
import com.exa863.anselmo_adna.view.View;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Tela do mapa da cidade.
 * Mostra os dados do jogador, a lista de locais para visitar, a mochila de itens
 * e inicia as cutscenes ao entrar nos lugares.
 *
 * @author Anselmo e Adna
 */
public class MapaView implements View {

    private final Console console;
    private final SceneController sceneController;
    private final GameController gameController;

    public MapaView(Console console, SceneController sceneController, GameController gameController) {
        this.console = console;
        this.sceneController = sceneController;
        this.gameController = gameController;
    }

    @Override
    public void render() {
        Local localAtual = gameController.getLocalAtual();

        View viewDedicada = resolverViewParaLocal(localAtual.getNome());
        if (!(viewDedicada instanceof MapaView)) {
            sceneController.trocarCena(viewDedicada);
            return;
        }

        Player player = gameController.getPlayer();

        renderHUD(localAtual, player);

        List<ItemMenuMapa> itensMenu = construirItensMenu(localAtual, player);
        CEscolha[] opcoes = new CEscolha[itensMenu.size()];
        for (int i = 0; i < itensMenu.size(); i++) {
            opcoes[i] = new CEscolha(itensMenu.get(i).rotulo(), i);
        }

        try {
            CMultiplaEscolha menu = new CMultiplaEscolha(console);
            CEscolha escolha = menu.escolha(opcoes);
            ItemMenuMapa itemEscolhido = itensMenu.get(escolha.index);

            if (itemEscolhido.ehAcao()) {
                executarAcao(itemEscolhido.acao());
            } else {
                entrarNoLocal(itemEscolhido.local());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void renderHUD(Local localAtual, Player player) {
        Atributo attr = player.getAtributos();
        String nomePlayer = player.getNome();
        String dataHora = gameController.getDataDia().getDiaFormatado() + " às " + gameController.getDataDia().getHoraFormatada();
        int saude = attr.getSaude();
        int energia = attr.getEnergia();
        int dinheiro = player.getDinheiro();

        console.clearConsole();
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║" + centralizar("MAPA : " + localAtual.getNome().toUpperCase(), 70) + "║");
        console.printlnConsole("╠══════════════════════════════════════════════════════════════════════╣");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("║  ► ATLETA  : " + String.format("%-20s", nomePlayer) + " │ DATA   : " + String.format("%-24s", dataHora) + "║");
        console.printlnConsole("║  ► SAÚDE   : " + String.format("%-20s", saude + " / 100") + " │ ENERGIA: " + String.format("%-24s", energia + " / 100") + "║");
        console.printlnConsole("║  ► DINHEIRO: " + String.format("%-20s", dinheiro + " Reais") + " │ LOCAL  : " + String.format("%-24s", localAtual.getNome()) + "║");
        console.printlnConsole("║  ► INFO    : " + String.format("%-56s", localAtual.getDescricao()) + "║");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");
        console.printlnConsole("");
        console.printlnConsole("  Selecione para onde deseja ir com [ ▲ / ▼ ] e [ ENTER ]:\n");
    }

    private List<ItemMenuMapa> construirItensMenu(Local localAtual, Player player) {
        List<Local> subLocais = localAtual.getSubLocais();
        NarrativaController narrativa = gameController.getNarrativaController();

        // Filtra dinamicamente locais bloqueados pelo estado atual da narrativa
        if (narrativa != null) {
            subLocais = subLocais.stream()
                    .filter(l -> l.isLocalDeSaida() || !narrativa.isLocalBloqueadoPelaNarrativa(l.getNome(), player))
                    .toList();
        }

        List<ItemMenuMapa> itens = new java.util.ArrayList<>();

        for (Local local : subLocais) {
            if (!local.isLocalDeSaida()) {
                String rotulo = local.getNome();
                if (local.getCustoAcesso() > 0 && !local.isAcessoLiberado()) {
                    rotulo += " • [Passagem: " + local.getCustoAcesso() + " Reais]";
                }
                itens.add(new ItemMenuMapa(rotulo, local, null));
            }
        }

        itens.add(new ItemMenuMapa(AcaoMenu.ABRIR_MOCHILA.getTitulo(), null, AcaoMenu.ABRIR_MOCHILA));

        if (localAtual.getNome().equalsIgnoreCase("Cidade B")) {
            itens.add(new ItemMenuMapa(AcaoMenu.ABRIR_ACADEMIA.getTitulo(), null, AcaoMenu.ABRIR_ACADEMIA));
        }

        for (Local local : subLocais) {
            if (local.isLocalDeSaida()) {
                itens.add(new ItemMenuMapa(AcaoMenu.SAIR_LOCAL.getTitulo(), local, AcaoMenu.SAIR_LOCAL));
            }
        }

        return itens;
    }

    private void executarAcao(AcaoMenu acao) {
        switch (acao) {
            case ABRIR_MOCHILA -> sceneController.trocarCena(new InventarioView(console, sceneController, gameController));
            case ABRIR_ACADEMIA -> executarAcaoAbrirAcademia();
            case SAIR_LOCAL -> {
                gameController.voltarLocal();
                sceneController.trocarCena(new MapaView(console, sceneController, gameController));
            }
        }
    }

    private void entrarNoLocal(Local localEscolhido) throws IOException {
        if (!podeAcessar(localEscolhido)) {
            sceneController.trocarCena(new MapaView(console, sceneController, gameController));
            return;
        }

        gameController.entrarLocal(localEscolhido);
        String nomeDoLocal = localEscolhido.getNome();

        View viewDestino = resolverViewParaLocal(nomeDoLocal);

        Optional<Capitulo> capituloDisponivel = gameController.getNarrativaController()
                .obterCapituloDisponivel(
                        TipoGatilho.ENTRAR_LOCAL,
                        nomeDoLocal,
                        gameController.getPlayer()
                );

        if (capituloDisponivel.isPresent()) {
            CutsceneController cc = new CutsceneController(
                    capituloDisponivel.get(),
                    gameController.getPlayer()
            );
            sceneController.trocarCena(new CutsceneView(console, sceneController, cc, viewDestino));
            return;
        }

        sceneController.trocarCena(viewDestino);
    }

    private void executarAcaoAbrirAcademia() {
        console.clearConsole();
        Player player = gameController.getPlayer();
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                  PROJETO: ABRIR UMA ACADEMIA                         ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
        console.printlnConsole("  Construir sua própria academia de boxe profissional na Cidade B");
        console.printlnConsole("  é o seu maior sonho como atleta e mestre!\n");
        console.printlnConsole("  ► Requisitos necessários:");
        console.printlnConsole("    • Conquistar o Campeonato Mundial");
        console.printlnConsole("    • Capital inicial : 5.000 Reais");
        console.printlnConsole("    • Seu Dinheiro    : " + player.getDinheiro() + " Reais\n");
        console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
        console.printlnConsole("       │                 [ ENTER ]  Voltar                      │");
        console.printlnConsole("       └────────────────────────────────────────────────────────┘");
        console.esperarEnter("");
        sceneController.trocarCena(new MapaView(console, sceneController, gameController));
    }

    private record ItemMenuMapa(String rotulo, Local local, com.exa863.anselmo_adna.model.world.AcaoMenu acao) {
        public boolean ehAcao() {
            return acao != null;
        }
    }

    private View resolverViewParaLocal(String nomeLocal) {
        if (nomeLocal.equalsIgnoreCase("Academia") || nomeLocal.equalsIgnoreCase("Academia Profissional")) {
            return new AcademiaView(console, sceneController, gameController);
        }
        if (nomeLocal.equalsIgnoreCase("Academia de Boxe")) {
            return new AcademiaBoxeView(console, sceneController, gameController);
        }
        if (nomeLocal.equalsIgnoreCase("Clube de Luta")) {
            return new ClubeDeLutaView(console, sceneController, gameController);
        }
        if (nomeLocal.equalsIgnoreCase("Casa")) {
            return new CasaView(console, sceneController, gameController);
        }
        if (nomeLocal.equalsIgnoreCase("Loja")) {
            return new LojaView(console, sceneController, gameController);
        }
        if (nomeLocal.equalsIgnoreCase("Campeonato Mundial")) {
            return new CampeonatoMundialView(console, sceneController, gameController);
        }
        return new MapaView(console, sceneController, gameController);
    }

    private boolean podeAcessar(Local local) throws IOException {
        int custo = local.getCustoAcesso();

        if (custo <= 0 || local.isLocalDeSaida() || local.isAcessoLiberado()) {
            return true;
        }

        Player player = gameController.getPlayer();
        int dinheiro = player.getDinheiro();

        if (dinheiro < custo) {
            console.printlnConsole("");
            console.printlnConsole("  [!] Dinheiro insuficiente para viajar até " + local.getNome() + ".");
            console.printlnConsole("      Custo da passagem : " + custo + " Reais");
            console.printlnConsole("      Seu Dinheiro      : " + dinheiro + " Reais\n");
            console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
            console.printlnConsole("       │                 [ ENTER ]  Voltar                      │");
            console.printlnConsole("       └────────────────────────────────────────────────────────┘");
            console.esperarEnter("");
            return false;
        }

        console.printlnConsole("");
        console.printlnConsole("  ┌────────────────────────────────────────────────────────────────────┐");
        console.printlnConsole("  │ VIAGEM   : " + String.format("%-56s", local.getNome()) + "│");
        console.printlnConsole("  │ Custo    : " + String.format("%-56s", custo + " Reais") + "│");
        console.printlnConsole("  │ Dinheiro : " + String.format("%-56s", dinheiro + " Reais") + "│");
        console.printlnConsole("  └────────────────────────────────────────────────────────────────────┘");
        console.printlnConsole("");

        CEscolha[] opcoesPagamento = new CEscolha[] {
                new CEscolha("Pagar passagem (" + custo + " Reais) e viajar", 0),
                new CEscolha("Desistir e voltar", 1)
        };

        CMultiplaEscolha menuPagamento = new CMultiplaEscolha(console);
        CEscolha escolha = menuPagamento.escolha(opcoesPagamento);

        if (escolha.index != 0) {
            return false;
        }

        player.setDinheiro(dinheiro - custo);
        local.setAcessoLiberado(true);
        return true;
    }

    private String centralizar(String texto, int largura) {
        if (texto.length() >= largura) {
            return texto.substring(0, largura);
        }
        int espacosTotais = largura - texto.length();
        int espacosEsquerda = espacosTotais / 2;
        int espacosDireita = espacosTotais - espacosEsquerda;
        return " ".repeat(espacosEsquerda) + texto + " ".repeat(espacosDireita);
    }
}
