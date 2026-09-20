package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.MatchController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.model.character.Cores;
import com.exa863.anselmo_adna.model.character.Personagem;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.character.Sexo;
import com.exa863.anselmo_adna.model.combat.EstiloLuta;
import com.exa863.anselmo_adna.model.combat.Lutador;
import com.exa863.anselmo_adna.model.combat.Match;
import com.exa863.anselmo_adna.model.combat.ResultadoLuta;
import com.exa863.anselmo_adna.model.combat.estilos.Contragolpeador;
import com.exa863.anselmo_adna.model.combat.estilos.Nocauteador;
import com.exa863.anselmo_adna.model.combat.estilos.Velocista;
import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.model.stats.Relacionamentos;
import com.exa863.anselmo_adna.view.View;

import java.io.IOException;
import java.util.function.Consumer;

public class CampeonatoMundialView implements View {

    // Só libera o Mundial depois que o personagem disputou as 2 lutas estaduais e as 2 nacionais
    private static final int LUTAS_ESTADUAIS_NECESSARIAS = 2;
    private static final int LUTAS_NACIONAIS_NECESSARIAS = 2;

    private final Console console;
    private final SceneController sceneController;
    private final GameController gameController;

    public CampeonatoMundialView(Console console, SceneController sceneController, GameController gameController) {
        this.console = console;
        this.sceneController = sceneController;
        this.gameController = gameController;
    }

    @Override
    public void render() {
        console.clearConsole();
        Player player = gameController.getPlayer();
        Atributo attr = player.getAtributos();

        if (!requisitosCumpridos(player)) {
            renderAcessoNegado(player);
            return;
        }

        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                  CAMPEONATO MUNDIAL - O GRANDE PALCO                 ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
        console.printlnConsole("  A arena lotada grita o seu nome. As câmeras de TV focam-se no ringue.");
        console.printlnConsole("  É a disputa definitiva contra o melhor lutador do planeta.\n");

        console.printlnConsole("  ┌────────────────────────────────────────────────────────────────────┐");
        console.printlnConsole("  │ ADVERSÁRIO : Anthony \"O Trovão\"                                  │");
        console.printlnConsole("  │ Estilo     : Nocauteador (Slugger)                                 │");
        console.printlnConsole("  │ Status     : Atributos no nível máximo.                            │");
        console.printlnConsole("  ├────────────────────────────────────────────────────────────────────┤");
        console.printlnConsole("  │ SEU STATUS : Saúde: " + String.format("%-3d", attr.getSaude()) + "/100  |  Energia: "
                + String.format("%-3d", attr.getEnergia()) + "/100  |  Dinheiro: " + player.getDinheiro() + " Reais" + " ".repeat(15) + "│");
        console.printlnConsole("  └────────────────────────────────────────────────────────────────────┘\n");

        int taxa = 3000;
        int premio = 15000;

        CEscolha[] opcoes = new CEscolha[] {
                new CEscolha("Disputar o Cinturão Mundial (Taxa: " + taxa + " Reais | Prêmio: " + premio + ")", 0),
                new CEscolha("Estou com medo. Voltar ao Mapa", 1)
        };

        try {
            CMultiplaEscolha menu = new CMultiplaEscolha(console);
            CEscolha escolha = menu.escolha(opcoes);

            if (escolha.index == 1) {
                gameController.voltarLocal();
                sceneController.trocarCena(new MapaView(console, sceneController, gameController));
                return;
            }

            if (player.getDinheiro() < taxa) {
                console.printlnConsole("\n  [!] Você não tem dinheiro para pagar os " + taxa + " Reais da organização do Mundial.");
                console.esperarEnter("       [ ENTER ] Voltar");
                sceneController.trocarCena(this);
                return;
            }

            if (attr.getSaude() < 70 || attr.getEnergia() < 70) {
                console.printlnConsole("\n  [!] A comissão barrou você! Para lutar no mundial, precisa de pelo menos 70 de Saúde e Energia.");
                console.esperarEnter("       [ ENTER ] Voltar");
                sceneController.trocarCena(this);
                return;
            }

            player.setDinheiro(player.getDinheiro() - taxa);

            Personagem pAnthony = new Personagem(6, "Anthony", "Campeão Mundial absoluto. Nocauteador implacável.", Cores.CASTANHO, Sexo.MASCULINO);
            Atributo attrAnthony = new Atributo(100, 10, 9, 8, 8, 100);
            EstiloLuta estiloAnthony = new Nocauteador();

            EstiloLuta posturaEscolhida = escolherPostura();
            iniciarLutaMundial(player, premio, pAnthony, attrAnthony, estiloAnthony, posturaEscolhida);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean requisitosCumpridos(Player player) {
        return player.getLutasEstaduais() >= LUTAS_ESTADUAIS_NECESSARIAS
                && player.getLutasNacionais() >= LUTAS_NACIONAIS_NECESSARIAS;
    }

    private void renderAcessoNegado(Player player) {
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                  CAMPEONATO MUNDIAL - ACESSO NEGADO                  ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
        console.printlnConsole("  Um segurança da organização barra sua entrada na arena:\n");
        console.printlnConsole("  \"Você ainda não tem credenciais para o Mundial, campeão.");
        console.printlnConsole("   É preciso provar seu valor nos circuitos oficiais primeiro.\"\n");
        console.printlnConsole("  ► Lutas Estaduais disputadas : " + player.getLutasEstaduais() + "/" + LUTAS_ESTADUAIS_NECESSARIAS);
        console.printlnConsole("  ► Lutas Nacionais disputadas : " + player.getLutasNacionais() + "/" + LUTAS_NACIONAIS_NECESSARIAS);
        console.printlnConsole("  ► Sugestão                   : Vá até a Academia de Boxe e complete o circuito.\n");

        console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
        console.printlnConsole("       │              [ ENTER ]  Voltar ao Mapa                 │");
        console.printlnConsole("       └────────────────────────────────────────────────────────┘");
        console.esperarEnter("");
        gameController.voltarLocal();
        sceneController.trocarCena(new MapaView(console, sceneController, gameController));
    }

    // Mostra qual caminho de relacionamento o jogador seguiu e a afinidade final com cada personagem
    private void exibirResumoRelacionamentos(Player player) {
        console.printlnConsole("\n╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("  ║                       RELACIONAMENTOS FINAIS                         ║");
        console.printlnConsole("  ╚══════════════════════════════════════════════════════════════════════╝");

        if (player.possuiRomance()) {
            console.printlnConsole("  ✦ Romance : Você seguiu o caminho do relacionamento com " + player.getPersonagemRomance() + ".");
        } else {
            console.printlnConsole("  ✦ Romance : Você manteve o foco apenas na carreira. Nenhum romance foi seguido.");
        }

        console.printlnConsole("");
        console.printlnConsole("  ✦ Afinidades finais:");
        boolean algumaAfinidade = false;
        for (Relacionamentos relacionamento : player.getRelacionamentos()) {
            if (relacionamento != null && relacionamento.getPersonagem() != null) {
                algumaAfinidade = true;
                console.printlnConsole("     - " + relacionamento.getPersonagem().getNome() + ": " + relacionamento.getNivelAmizade());
            }
        }
        if (!algumaAfinidade) {
            console.printlnConsole("     Nenhum relacionamento foi construído durante a jornada.");
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

    private void iniciarLutaMundial(Player player, int premio, Personagem pOponente, Atributo attrOponente, EstiloLuta estiloOponente, EstiloLuta posturaEscolhida) {
        Lutador lutadorPlayer = new Lutador(player, player.getAtributos(), posturaEscolhida);
        Lutador lutadorOponente = new Lutador(pOponente, attrOponente, estiloOponente);

        MatchController matchController = new MatchController();
        Match match = matchController.executarPartida(lutadorPlayer, lutadorOponente);

        // Aqui transferimos o processamento do final do jogo baseado na mecânica real
        Consumer<ResultadoLuta> callbackPosLuta = resultado -> {
            boolean venceuLuta = resultado.isVencedor(lutadorPlayer);

            // Avaliar o Contrato com Noor (ID 5)
            Personagem victorNoor = new Personagem(5, "Victor Noor", "", Cores.PRETO, Sexo.MASCULINO);
            boolean aceitouContrato = player.getAfinidade(victorNoor) >= 50;
            boolean investigacaoCompleta = player.isCapituloConcluido("INVESTIGACAO_COMPLETA");

            console.clearConsole();
            console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
            console.printlnConsole("║                        O DESFECHO DA JORNADA                         ║");
            console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");

            if (aceitouContrato) {
                if (!venceuLuta) {
                    console.printlnConsole("  ◈ [Narrador] Você cumpriu a ordem vergonhosa e caiu na lona. Sua parte do acordo sujo foi feita.");
                    console.printlnConsole("  ◈ [Narrador] Mas as perigosas organizações que apostaram milhões em você não perdoaram a sua covardia.");
                    console.printlnConsole("  ◈ [Narrador] O dinheiro maldito nunca chegou a ser gasto na sua curta vida.");
                    console.printlnConsole("\n  ✦ FIM 1: A ALMA VENDIDA (Derrota Opcional)");
                } else {
                    console.printlnConsole("  ◈ [Narrador] O orgulho falou mais alto e você se recusou a cair na lona. Você aniquilou Anthony brilhantemente.");
                    console.printlnConsole("  ◈ [Narrador] Porém, você quebrou o letal contrato de Noor. A sua glória perante a multidão foi eterna,");
                    console.printlnConsole("  ◈ [Narrador] mas sua vida terminou tragicamente naquela mesma noite chuvosa, em um beco atrás da arena.");
                    console.printlnConsole("\n  ✦ FIM 2: A ALMA VENDIDA (Vitória Trágica)");
                }
            } else {
                if (venceuLuta) {
                    player.setDinheiro(player.getDinheiro() + premio);
                    console.printlnConsole("  ◈ [Narrador] O ringue sangrento silencia antes da explosão de gritos. O juiz levanta o seu braço com convicção.");
                    console.printlnConsole("  ► " + player.getNome() + ": 'Pai, onde quer que você esteja... nós finalmente conseguimos! O nosso legado de sangue está salvo!'");
                    console.printlnConsole("\n  ✦ FIM 3: O CAMPEÃO DO MUNDO");
                } else {
                    console.printlnConsole("  ◈ [Narrador] Anthony simplesmente foi superior técnico. Seus músculos falharam e a lona gelada beija o seu rosto.");
                    console.printlnConsole("  ► " + player.getNome() + ": (Anos depois, treinando pacientemente na sua modesta academia) 'Eu não venci o mundial naquele dia, mas o legado vive aqui.'");
                    console.printlnConsole("\n  ✦ FIM 4: O VERDADEIRO LEGADO");
                }

                if (investigacaoCompleta) {
                    console.printlnConsole("\n  ◈ [Narrador] Dias depois, um jornal é entregue à sua porta: a reportagem de Alexandra Cruz estampa a manchete.");
                    console.printlnConsole("  ✦ [Sistema] Victor Noor foi preso pela polícia estadual, acusado de fraude e manipulação de resultados no boxe.");
                }
            }

            exibirResumoRelacionamentos(player);

            console.printlnConsole("\n     ┌────────────────────────────────────────────────────────┐");
            console.printlnConsole("       │           [ ENTER ]  Voltar ao Menu Principal          │");
            console.printlnConsole("       └────────────────────────────────────────────────────────┘");
            console.esperarEnter("");

            // Reinicia o jogo voltando ao menu principal com um estado limpo
            sceneController.trocarCena(new MenuView(console, sceneController, new GameController()));
        };

        View destinoRetorno = new CampeonatoMundialView(console, sceneController, gameController);
        sceneController.trocarCena(new MatchView(console, sceneController, gameController, match,
                lutadorPlayer, lutadorOponente, destinoRetorno, callbackPosLuta));
    }
}