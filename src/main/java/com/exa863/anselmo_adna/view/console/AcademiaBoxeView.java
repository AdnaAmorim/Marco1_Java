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

public class AcademiaBoxeView implements View {

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

        // 1. Verificação de intervalo (Dia sim, dia não)
        if (!player.podeLutarBoxe(diaAtual)) {
            renderSemLutaHoje(diaAtual, player.getProximoDiaLutaBoxe());
            return;
        }

        // 2. Dia de luta: seleciona o oponente do dia da lista pré-definida
        InfoOponente oponente = OponentesBoxe.obterPorDia(diaAtual);
        renderCardLuta(player, diaAtual, oponente);
    }

    private void renderSemLutaHoje(int diaAtual, int proximoDia) {
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                  ACADEMIA DE BOXE - DIA DE DESCANSO                  ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
        console.printlnConsole("  O árbitro principal cruza os braços na entrada do ringue:\n");
        console.printlnConsole("  \"Hoje não há lutas no card oficial, campeão!");
        console.printlnConsole("   Os ringues estão reservados para manutenção e limpeza de rotina.\"");
        console.printlnConsole("");
        console.printlnConsole("  ► Dia Atual   : Dia " + diaAtual);
        console.printlnConsole("  ► Próxima Luta: Dia " + proximoDia + " (As lutas ocorrem a cada 2 dias)");
        console.printlnConsole("  ► Sugestão    : Aproveite para treinar na Academia ou descansar em Casa!\n");

        console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
        console.printlnConsole("       │              [ ENTER ]  Voltar ao Mapa                 │");
        console.printlnConsole("       └────────────────────────────────────────────────────────┘");
        console.esperarEnter("");
        gameController.voltarLocal();
        sceneController.trocarCena(new MapaView(console, sceneController, gameController));
    }

    private void renderCardLuta(Player player, int diaAtual, InfoOponente oponente) {
        Atributo attr = player.getAtributos();

        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                  ACADEMIA DE BOXE - CARD OFICIAL                     ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");

        console.printlnConsole("  ┌────────────────────────────────────────────────────────────────────┐");
        console.printlnConsole("  │ ADVERSÁRIO : " + String.format("%-54s", oponente.nome() + " \"" + oponente.apelido() + "\"") + "│");
        console.printlnConsole("  │ Estilo     : " + String.format("%-54s", oponente.estilo().getNome()) + "│");
        console.printlnConsole("  │ Prêmio Luta: " + String.format("%-54s", oponente.premio() + " Reais (em caso de vitória)") + "│");
        console.printlnConsole("  │ Perfil     : " + String.format("%-54s", oponente.descricao()) + "│");
        console.printlnConsole("  ├────────────────────────────────────────────────────────────────────┤");
        console.printlnConsole("  │ SEU STATUS : Saúde: " + String.format("%-3d", attr.getSaude()) + "/100  |  Energia: "
                + String.format("%-3d", attr.getEnergia()) + "/100  |  Dinheiro: " + player.getDinheiro() + " Reais" + " ".repeat(15) + "│");
        console.printlnConsole("  └────────────────────────────────────────────────────────────────────┘\n");

        CEscolha[] opcoes = new CEscolha[] {
                new CEscolha("Subir no Ringue (Iniciar Combate)", 0),
                new CEscolha("Recuar e Voltar ao Mapa", 1)
        };

        try {
            CMultiplaEscolha menu = new CMultiplaEscolha(console);
            CEscolha escolha = menu.escolha(opcoes);

            if (escolha.index == 1) {
                gameController.voltarLocal();
                sceneController.trocarCena(new MapaView(console, sceneController, gameController));
                return;
            }

            // Validação física pré-luta
            if (attr.getSaude() < 30 || attr.getEnergia() < 25) {
                console.printlnConsole("\n  [!] O médico da comissão vetou sua participação!");
                console.printlnConsole("      Você está debilitado demais para subir no ringue.");
                console.printlnConsole("      (Mínimo exigido: 30 de Saúde e 25 de Energia)");
                console.printlnConsole("  ► Vá até sua Casa descansar e dormir para recuperar forças!\n");
                console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
                console.printlnConsole("       │                 [ ENTER ]  Voltar                      │");
                console.printlnConsole("       └────────────────────────────────────────────────────────┘");
                console.esperarEnter("");
                sceneController.trocarCena(this);
                return;
            }

            // Escolha tática de postura antes de iniciar o combate
            EstiloLuta posturaEscolhida = escolherPostura();
            iniciarLuta(player, diaAtual, oponente, posturaEscolhida);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private EstiloLuta escolherPostura() throws IOException {
        console.clearConsole();
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                    ESCOLHA SUA POSTURA DE LUTA                       ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
        console.printlnConsole("  Cada postura altera suas capacidades com bônus e penalidades percentuais:\n");

        console.printlnConsole("  ┌────────────────────────────────────────────────────────────────────┐");
        console.printlnConsole("  │ 1. VELOCISTA (Out-Boxer)                                           │");
        console.printlnConsole("  │    ▲ Agilidade  : Bônus de +15% a +75% (conforme sua agilidade)    │");
        console.printlnConsole("  │    ▲ Energia    : Bônus de +20% (mais fôlego durante os rounds)    │");
        console.printlnConsole("  │    ▼ Força      : Penalidade de -20% (golpes mais leves)           │");
        console.printlnConsole("  ├────────────────────────────────────────────────────────────────────┤");
        console.printlnConsole("  │ 2. NOCAUTEADOR (Slugger)                                           │");
        console.printlnConsole("  │    ▲ Força      : Bônus de +15% a +75% (conforme sua força atual)  │");
        console.printlnConsole("  │    ▼ Agilidade  : Penalidade de -20% (movimentação mais pesada)    │");
        console.printlnConsole("  │    ▼ Energia    : Penalidade de -20% (gasto maior ao golpear)      │");
        console.printlnConsole("  ├────────────────────────────────────────────────────────────────────┤");
        console.printlnConsole("  │ 3. CONTRAGOLPEADOR (Counter-Puncher)                               │");
        console.printlnConsole("  │    ▲ Resistência: Bônus de +15% a +75% (guarda mais impenetrável)  │");
        console.printlnConsole("  │    ▲ Inteligên. : Bônus de +15% a +75% (leitura tática de brechas) │");
        console.printlnConsole("  │    ▼ Agilidade  : Penalidade de -15% (postura mais plantada)       │");
        console.printlnConsole("  └────────────────────────────────────────────────────────────────────┘\n");

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

    private void iniciarLuta(Player player, int diaAtual, InfoOponente oponenteInfo, EstiloLuta posturaEscolhida) {
        Lutador lutadorPlayer = new Lutador(player, player.getAtributos(), posturaEscolhida);
        Lutador lutadorOponente = oponenteInfo.criarLutador();

        MatchController matchController = new MatchController();
        Match match = matchController.executarPartida(lutadorPlayer, lutadorOponente);

        Consumer<ResultadoLuta> callbackPosLuta = resultado -> {
            player.registrarLutaBoxe(diaAtual);
            gameController.getDataDia().avancarMinutos(120);
            player.getAtributos().setEnergia(Math.max(0, player.getAtributos().getEnergia() - 25));

            console.printlnConsole("");
            if (resultado.isVencedor(lutadorPlayer)) {
                player.setDinheiro(player.getDinheiro() + oponenteInfo.premio());
                console.printlnConsole("  ╔══════════════════════════════════════════════════════════════════════╗");
                console.printlnConsole("  ║                       VITÓRIA NO RINGUE!                             ║");
                console.printlnConsole("  ╚══════════════════════════════════════════════════════════════════════╝\n");
                console.printlnConsole("  ► Parabéns! Você dominou o ringue e venceu o combate!");
                console.printlnConsole("  ► Prêmio recebido  : +" + oponenteInfo.premio() + " Reais em Dinheiro");
                console.printlnConsole("  ► Dinheiro total   : " + player.getDinheiro() + " Reais");
                console.printlnConsole("  ► Cansaço de luta  : -25 de Energia\n");
            } else if (resultado.isEmpate()) {
                int meioPremio = oponenteInfo.premio() / 2;
                player.setDinheiro(player.getDinheiro() + meioPremio);
                player.getAtributos().setSaude(Math.max(15, player.getAtributos().getSaude() - 20));
                console.printlnConsole("  ╔══════════════════════════════════════════════════════════════════════╗");
                console.printlnConsole("  ║                         EMPATE TÉCNICO                               ║");
                console.printlnConsole("  ╚══════════════════════════════════════════════════════════════════════╝\n");
                console.printlnConsole("  ► Luta dura até o gongo final! Os juízes declararam empate.");
                console.printlnConsole("  ► Prêmio dividido  : +" + meioPremio + " Reais em Dinheiro");
                console.printlnConsole("  ► Desgaste sofrido : -20 de Saúde e -25 de Energia\n");
            } else {
                player.getAtributos().setSaude(Math.max(15, player.getAtributos().getSaude() - 40));
                console.printlnConsole("  ╔══════════════════════════════════════════════════════════════════════╗");
                console.printlnConsole("  ║                       DERROTA NO RINGUE                              ║");
                console.printlnConsole("  ╚══════════════════════════════════════════════════════════════════════╝\n");
                console.printlnConsole("  ► Você foi superado no ringue e sofreu golpes contundentes.");
                console.printlnConsole("  ► Ferimentos       : -40 de Saúde");
                console.printlnConsole("  ► Cansaço de luta  : -25 de Energia");
                console.printlnConsole("  ► Vá para sua Casa no mapa descansar e dormir para se recuperar!\n");
            }
        };

        View destinoRetorno = new MapaView(console, sceneController, gameController);
        sceneController.trocarCena(new MatchView(console, sceneController, gameController, match,
                lutadorPlayer, lutadorOponente, destinoRetorno, callbackPosLuta));
    }
}
