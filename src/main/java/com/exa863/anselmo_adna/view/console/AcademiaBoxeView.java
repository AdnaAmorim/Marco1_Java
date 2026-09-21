package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.MatchController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.combat.EstiloLuta;
import com.exa863.anselmo_adna.model.combat.Lutador;
import com.exa863.anselmo_adna.model.combat.Match;
import com.exa863.anselmo_adna.model.combat.OponentesBoxe;
import com.exa863.anselmo_adna.model.combat.OponentesBoxe.InfoOponente;
import com.exa863.anselmo_adna.model.combat.ResultadoLuta;
import com.exa863.anselmo_adna.model.combat.estilos.Contragolpeador;
import com.exa863.anselmo_adna.model.combat.estilos.Nocauteador;
import com.exa863.anselmo_adna.model.combat.estilos.Velocista;
import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.view.View;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Tela da academia de boxe.
 * Gerencia as lutas amadoras dos circuitos Local, Estadual e Nacional.
 *
 * @author Anselmo e Adna
 */
public class AcademiaBoxeView implements View {

    private static final int LIMITE_LUTAS_ESTADUAIS = 2;
    private static final int LIMITE_LUTAS_NACIONAIS = 2;

    private final Console console;
    private final SceneController sceneController;
    private final GameController gameController;

    public AcademiaBoxeView(Console console, SceneController sceneController, GameController gameController) {
        this.console = console;
        this.sceneController = sceneController;
        this.gameController = gameController;
    }

    @Override
    public void render() {
        console.clearConsole();
        Player player = gameController.getPlayer();
        int diaAtual = gameController.getDataDia().getDias();

        if (!player.podeLutarBoxe(diaAtual)) {
            renderSemLutaHoje(diaAtual, player.getProximoDiaLutaBoxe());
            return;
        }

        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                  ACADEMIA DE BOXE - MENU DE LUTAS                    ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
        console.printlnConsole("  O árbitro aguarda a sua inscrição. Onde deseja lutar hoje?\n");

        boolean estadualEsgotado = player.getLutasEstaduais() >= LIMITE_LUTAS_ESTADUAIS;
        boolean nacionalLiberado = player.getLutasEstaduais() >= LIMITE_LUTAS_ESTADUAIS;
        boolean nacionalEsgotado = player.getLutasNacionais() >= LIMITE_LUTAS_NACIONAIS;

        String labelEstadual = estadualEsgotado
                ? "Cinturão Estadual   (ENCERRADO - vaga já disputada)"
                : "Cinturão Estadual   (Taxa: 500 Reais | Prêmio: 2.000)";
        String labelNacional;
        if (!nacionalLiberado) {
            labelNacional = "Cinturão Nacional   (BLOQUEADO - complete o Estadual primeiro)";
        } else if (nacionalEsgotado) {
            labelNacional = "Cinturão Nacional   (ENCERRADO - vaga já disputada)";
        } else {
            labelNacional = "Cinturão Nacional   (Taxa: 1.500 Reais | Prêmio: 5.000)";
        }

        CEscolha[] opcoes = new CEscolha[] {
                new CEscolha("Circuito Local Diário (Sem taxa | Oponentes Aleatórios)", 0),
                new CEscolha(labelEstadual, 1),
                new CEscolha(labelNacional, 2),
                new CEscolha("Sair da Academia", 3)
        };

        try {
            CMultiplaEscolha menu = new CMultiplaEscolha(console);
            CEscolha escolha = menu.escolha(opcoes);

            if (escolha.index == 3) {
                gameController.voltarLocal();
                sceneController.trocarCena(new MapaView(console, sceneController, gameController));
                return;
            }

            if (escolha.index == 1 && estadualEsgotado) {
                renderCampeonatoEncerrado("Estadual", LIMITE_LUTAS_ESTADUAIS);
                return;
            }

            if (escolha.index == 2 && !nacionalLiberado) {
                renderNacionalBloqueado(player);
                return;
            }

            if (escolha.index == 2 && nacionalEsgotado) {
                renderCampeonatoEncerrado("Nacional", LIMITE_LUTAS_NACIONAIS);
                return;
            }

            InfoOponente oponente = null;
            int taxa = 0;

            if (escolha.index == 0) {
                oponente = OponentesBoxe.obterPorDia(diaAtual);
            } else if (escolha.index == 1) {
                taxa = 500;
                oponente = new InfoOponente(101, "Vitor", "A Rocha", "Campeão Estadual de defesa impenetrável.", new Contragolpeador(), 4, 4, 7, 5, 2000);
            } else if (escolha.index == 2) {
                taxa = 1500;
                oponente = new InfoOponente(102, "Igor", "O Carrasco", "Campeão Nacional explosivo e muito veloz.", new Velocista(), 7, 8, 6, 6, 5000);
            }

            renderCardLuta(player, diaAtual, oponente, taxa, escolha.index);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void renderCampeonatoEncerrado(String nomeCampeonato, int limite) {
        console.printlnConsole("\n  [!] Você já disputou o Cinturão " + nomeCampeonato + " o número máximo de vezes ("
                + limite + "), vitória ou derrota. Essa vaga está encerrada.");
        console.esperarEnter("       [ ENTER ] Voltar");
        sceneController.trocarCena(this);
    }

    private void renderNacionalBloqueado(Player player) {
        console.printlnConsole("\n  [!] O Cinturão Nacional ainda está bloqueado.");
        console.printlnConsole("      É preciso disputar as " + LIMITE_LUTAS_ESTADUAIS + " lutas do Cinturão Estadual antes.");
        console.printlnConsole("      Lutas Estaduais disputadas: " + player.getLutasEstaduais() + "/" + LIMITE_LUTAS_ESTADUAIS);
        console.esperarEnter("       [ ENTER ] Voltar");
        sceneController.trocarCena(this);
    }

    private void renderSemLutaHoje(int diaAtual, int proximoDia) {
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                  ACADEMIA DE BOXE - DIA DE DESCANSO                  ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
        console.printlnConsole("  O árbitro principal cruza os braços na entrada do ringue:\n");
        console.printlnConsole("  \"Hoje não há lutas no card oficial, campeão!");
        console.printlnConsole("   Você já lutou hoje. Os ringues estão reservados para manutenção.\"");
        console.printlnConsole("");
        console.printlnConsole("  ► Dia Atual   : Dia " + diaAtual);
        console.printlnConsole("  ► Próxima Luta: Dia " + proximoDia + " (As lutas ocorrem diariamente)");
        console.printlnConsole("  ► Sugestão    : Aproveite para treinar na Academia ou descansar em Casa!\n");

        console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
        console.printlnConsole("       │              [ ENTER ]  Voltar ao Mapa                 │");
        console.printlnConsole("       └────────────────────────────────────────────────────────┘");
        console.esperarEnter("");
        gameController.voltarLocal();
        sceneController.trocarCena(new MapaView(console, sceneController, gameController));
    }

    private void renderCardLuta(Player player, int diaAtual, InfoOponente oponente, int taxa, int tipoCampeonato) {
        Atributo attr = player.getAtributos();
        console.clearConsole();
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                        CARD DA LUTA OFICIAL                          ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");

        console.printlnConsole("  ┌────────────────────────────────────────────────────────────────────┐");
        console.printlnConsole("  │ ADVERSÁRIO : " + String.format("%-54s", oponente.nome() + " \"" + oponente.apelido() + "\"") + "│");
        console.printlnConsole("  │ Estilo     : " + String.format("%-54s", oponente.estilo().getNome()) + "│");
        console.printlnConsole("  │ Prêmio     : " + String.format("%-54s", oponente.premio() + " Reais") + "│");
        console.printlnConsole("  │ Taxa       : " + String.format("%-54s", taxa > 0 ? taxa + " Reais" : "Grátis") + "│");
        console.printlnConsole("  ├────────────────────────────────────────────────────────────────────┤");
        console.printlnConsole("  │ SEU STATUS : Saúde: " + String.format("%-3d", attr.getSaude()) + "/100  |  Energia: "
                + String.format("%-3d", attr.getEnergia()) + "/100  |  Dinheiro: " + player.getDinheiro() + " Reais" + " ".repeat(15) + "│");
        console.printlnConsole("  └────────────────────────────────────────────────────────────────────┘\n");

        CEscolha[] opcoes = new CEscolha[] {
                new CEscolha("Pagar taxa e Subir no Ringue", 0),
                new CEscolha("Recuar e Voltar ao Menu", 1)
        };

        try {
            CMultiplaEscolha menu = new CMultiplaEscolha(console);
            CEscolha escolha = menu.escolha(opcoes);

            if (escolha.index == 1) {
                sceneController.trocarCena(this);
                return;
            }

            if (player.getDinheiro() < taxa) {
                console.printlnConsole("\n  [!] Dinheiro insuficiente para pagar a taxa de inscrição de " + taxa + " Reais.");
                console.esperarEnter("       [ ENTER ] Voltar");
                sceneController.trocarCena(this);
                return;
            }

            if (attr.getSaude() < 30 || attr.getEnergia() < 25) {
                console.printlnConsole("\n  [!] O médico da comissão vetou sua participação por estar debilitado!");
                console.esperarEnter("       [ ENTER ] Voltar");
                sceneController.trocarCena(this);
                return;
            }

            player.setDinheiro(player.getDinheiro() - taxa);
            EstiloLuta posturaEscolhida = escolherPostura();
            iniciarLuta(player, diaAtual, oponente, posturaEscolhida, tipoCampeonato);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private EstiloLuta escolherPostura() throws IOException {
        console.clearConsole();
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                    ESCOLHA SUA POSTURA DE LUTA                       ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
        console.printlnConsole("  Cada postura altera suas capacidades com bônus e penalidades:\n");

        CEscolha[] posturas = new CEscolha[] {
                new CEscolha("Adotar Postura Velocista       (+Agilidade, +Energia, -Força)", 0),
                new CEscolha("Adotar Postura Nocauteador     (+Força, -Agilidade, -Energia)", 1),
                new CEscolha("Adotar Postura Contragolpeador (+Resistência, +Inteligência, -Agilidade)", 2)
        };

        CMultiplaEscolha menuPostura = new CMultiplaEscolha(console);
        CEscolha escolha = menuPostura.escolha(posturas);

        return switch (escolha.index) {
            case 0 -> new Velocista();
            case 1 -> new Nocauteador();
            default -> new Contragolpeador();
        };
    }

    private void iniciarLuta(Player player, int diaAtual, InfoOponente oponenteInfo, EstiloLuta posturaEscolhida, int tipoCampeonato) {
        Lutador lutadorPlayer = new Lutador(player, player.getAtributos(), posturaEscolhida);
        Lutador lutadorOponente = oponenteInfo.criarLutador();

        MatchController matchController = new MatchController();
        Match match = matchController.executarPartida(lutadorPlayer, lutadorOponente);

        Consumer<ResultadoLuta> callbackPosLuta = resultado -> {
            if (tipoCampeonato == 1) {
                player.registrarLutaEstadual();
            } else if (tipoCampeonato == 2) {
                player.registrarLutaNacional();
            } else if (tipoCampeonato == 0) {
                player.registrarLutaLocal();
            }

            player.registrarLutaBoxe(diaAtual);
            gameController.getDataDia().avancarMinutos(180);
            player.getAtributos().setEnergia(Math.max(0, player.getAtributos().getEnergia() - 35));

            console.printlnConsole("");
            if (resultado.isVencedor(lutadorPlayer)) {
                player.setDinheiro(player.getDinheiro() + oponenteInfo.premio());
                console.printlnConsole("  ╔══════════════════════════════════════════════════════════════════════╗");
                console.printlnConsole("  ║                       VITÓRIA NO RINGUE!                             ║");
                console.printlnConsole("  ╚══════════════════════════════════════════════════════════════════════╝\n");
                console.printlnConsole("  ► Prêmio recebido  : +" + oponenteInfo.premio() + " Reais em Dinheiro");
                console.printlnConsole("  ► Dinheiro total   : " + player.getDinheiro() + " Reais");
            } else if (resultado.isEmpate()) {
                int meioPremio = oponenteInfo.premio() / 2;
                player.setDinheiro(player.getDinheiro() + meioPremio);
                player.getAtributos().setSaude(Math.max(15, player.getAtributos().getSaude() - 20));
                console.printlnConsole("  ► Empate! Os juízes não conseguiram decidir um vencedor claro.");
                console.printlnConsole("  ► Prêmio dividido  : +" + meioPremio + " Reais em Dinheiro");
            } else {
                player.getAtributos().setSaude(Math.max(15, player.getAtributos().getSaude() - 40));
                console.printlnConsole("  ► Você foi derrotado. Treine mais e volte mais forte!");
            }
        };

        View destinoRetorno = new AcademiaBoxeView(console, sceneController, gameController);
        sceneController.trocarCena(new MatchView(console, sceneController, gameController, match,
                lutadorPlayer, lutadorOponente, destinoRetorno, callbackPosLuta));
    }
}