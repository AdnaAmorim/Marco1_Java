package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.controller.cenas.CutsceneController;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.narrativa.Capitulo;
import com.exa863.anselmo_adna.model.narrativa.TipoGatilho;
import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.model.world.Local;
import com.exa863.anselmo_adna.view.View;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
        Player player = gameController.getPlayer();
        Atributo attr = player != null ? player.getAtributos() : new Atributo();
        String nomePlayer = player != null ? player.getNome() : "Boxeador";
        int saude = attr.getSaude();
        int energia = attr.getEnergia();
        int dinheiro = player != null ? player.getDinheiro() : 0;
        String dataHora = gameController.getDataDia().getDiaFormatado() + " • " + gameController.getDataDia().getHoraFormatada();

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

        List<Local> subLocais = localAtual.getSubLocais();
        int totalOpcoes = subLocais.size() + 1; // + Mochila (Inventário)
        CEscolha[] opcoes = new CEscolha[totalOpcoes];

        for (int i = 0; i < subLocais.size(); i++) {
            Local local = subLocais.get(i);
            String rotulo = local.getNome();

            if (local.getCustoAcesso() > 0 && !local.isAcessoLiberado()) {
                rotulo += " • [Passagem: " + local.getCustoAcesso() + " Reais]";
            }

            opcoes[i] = new CEscolha(rotulo, i);
        }

        int indexMochila = subLocais.size();
        opcoes[indexMochila] = new CEscolha("Abrir Mochila (Inventário)", indexMochila);

        CMultiplaEscolha menu = new CMultiplaEscolha(console);

        try {
            CEscolha escolha = menu.escolha(opcoes);

            if (escolha.index == indexMochila) {
                sceneController.trocarCena(new InventarioView(console, sceneController, gameController));
                return;
            }

            Local localEscolhido = subLocais.get(escolha.index);

            if (!podeAcessar(localEscolhido)) {
                sceneController.trocarCena(new MapaView(console, sceneController, gameController));
                return;
            }

            gameController.entrarLocal(localEscolhido);
            String nomeDoLocal = localEscolhido.getNome();

            // Determina a View correspondente a esse local
            View viewDestino = resolverViewParaLocal(nomeDoLocal);

            // 1. Verificação de gatilhos da história (Capítulos)
            Optional<Capitulo> capituloDisponivel = gameController.getNarrativaController()
                    .obterCapituloDisponivel(
                            TipoGatilho.ENTRAR_LOCAL,
                            nomeDoLocal,
                            gameController.getPlayer()
                    );

            // 2. Se tem história para esse local, roda Cutscene primeiro
            if (capituloDisponivel.isPresent()) {
                CutsceneController cc = new CutsceneController(
                        capituloDisponivel.get(),
                        gameController.getPlayer()
                );
                sceneController.trocarCena(new CutsceneView(console, sceneController, cc, viewDestino));
                return;
            }

            // 3. Fluxo direto sem cutscene
            sceneController.trocarCena(viewDestino);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private View resolverViewParaLocal(String nomeLocal) {
        if (nomeLocal.equalsIgnoreCase("Academia") || nomeLocal.equalsIgnoreCase("Academia Profissional")) {
            return new AcademiaView(console, sceneController, gameController);
        }
        if (nomeLocal.equalsIgnoreCase("Casa")) {
            return new CasaView(console, sceneController, gameController);
        }
        if (nomeLocal.equalsIgnoreCase("Loja")) {
            return new LojaView(console, sceneController, gameController);
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
