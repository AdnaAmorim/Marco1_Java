package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.view.View;

import java.io.IOException;
import java.util.Random;

public class AcademiaView implements View {

    private static final int CUSTO_TREINO_DINHEIRO = 50;

    private final Console console;
    private final SceneController sceneController;
    private final GameController gameController;
    private final Random random = new Random();

    public AcademiaView(Console console, SceneController sceneController, GameController gameController) {
        this.console = console;
        this.sceneController = sceneController;
        this.gameController = gameController;
    }

    @Override
    public void render() {
        Player player = gameController.getPlayer();
        Atributo attr = player.getAtributos();
        int diaAtual = gameController.getDataDia().getDias();
        boolean jaTreinou = player.jaTreinouHoje(diaAtual);

        String dataHora = gameController.getDataDia().getDiaFormatado() + " • " + gameController.getDataDia().getHoraFormatada();
        String statusTreino = jaTreinou ? "[✓ Já Treinou Hoje]" : "[★ Disponível (1 por dia)]";

        console.clearConsole();
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                    ACADEMIA DE TREINAMENTO                           ║");
        console.printlnConsole("╠══════════════════════════════════════════════════════════════════════╣");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("║  ► ATLETA  : " + String.format("%-20s", player.getNome()) + " │ DATA   : " + String.format("%-24s", dataHora) + "║");
        console.printlnConsole("║  ► SAÚDE   : " + String.format("%-20s", attr.getSaude() + " / 100") + " │ ENERGIA: " + String.format("%-24s", attr.getEnergia() + " / 100") + "║");
        console.printlnConsole("║  ► FORÇA   : " + String.format("%-20s", attr.getForca() + " / 10") + " │ AGILID : " + String.format("%-24s", attr.getAgilidade() + " / 10") + "║");
        console.printlnConsole("║  ► RESIST  : " + String.format("%-20s", attr.getResistencia() + " / 10") + " │ INTEL  : " + String.format("%-24s", attr.getInteligencia() + " / 10") + "║");
        console.printlnConsole("║  ► STATUS  : " + String.format("%-20s", statusTreino) + " │ DINHEIRO: " + String.format("%-23s", player.getDinheiro() + " Reais") + "║");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");
        console.printlnConsole("");
        console.printlnConsole("  ► REGRAS : 1 treino por dia. Exige Energia cheia (100) e 50 Reais.");
        console.printlnConsole("  Escolha a sua sessão com [ ▲ / ▼ ] e [ ENTER ]:\n");

        CMultiplaEscolha menu = new CMultiplaEscolha(console);
        CEscolha[] opcoes = new CEscolha[]{
                new CEscolha("Treino de Força (Saco Pesado & Halteres)    [-25 EN, 50 Reais, 50% Chance +1 FOR, 2h]", 0),
                new CEscolha("Treino de Agilidade (Corda & Esquiva)       [-25 EN, 50 Reais, 50% Chance +1 AGI, 2h]", 1),
                new CEscolha("Treino de Resistência (Circuito Cardio)     [-25 EN, 50 Reais, 50% Chance +1 RES, 2h]", 2),
                new CEscolha("Treino Tático & Sombra (Estudo de Boxe)     [-25 EN, 50 Reais, 50% Chance +1 INT, 2h]", 3),
                new CEscolha("Sair da Academia (Voltar para a Cidade)", 4)
        };

        try {
            CEscolha escolha = menu.escolha(opcoes);

            switch (escolha.index) {
                case 0 -> treinarAtributo("Força", 25, () -> attr.setForca(attr.getForca() + 1), attr.getForca());
                case 1 -> treinarAtributo("Agilidade", 25, () -> attr.setAgilidade(attr.getAgilidade() + 1), attr.getAgilidade());
                case 2 -> treinarAtributo("Resistência", 25, () -> attr.setResistencia(attr.getResistencia() + 1), attr.getResistencia());
                case 3 -> treinarAtributo("Inteligência", 25, () -> attr.setInteligencia(attr.getInteligencia() + 1), attr.getInteligencia());
                case 4 -> {
                    gameController.voltarLocal();
                    sceneController.trocarCena(new MapaView(console, sceneController, gameController));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void treinarAtributo(String nome, int custoEnergia, Runnable incremento, int valorAtual) {
        Player player = gameController.getPlayer();
        Atributo attr = player.getAtributos();
        int diaAtual = gameController.getDataDia().getDias();

        console.clearConsole();

        // 1. Validação de Limite Diário (Apenas 1 treino por dia)
        if (player.jaTreinouHoje(diaAtual)) {
            console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
            console.printlnConsole("║                       TREINO DIÁRIO JÁ REALIZADO                     ║");
            console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
            console.printlnConsole("  [!] O treinador barrou você: você já realizou seu treino do Dia " + diaAtual + "!");
            console.printlnConsole("  [!] Treinar mais de uma vez ao dia sobrecarrega as fibras musculares.");
            console.printlnConsole("  ► Vá para sua Casa no mapa descansar e dormir para avançar o dia!\n");

            console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
            console.printlnConsole("       │              [ ENTER ]  Voltar                         │");
            console.printlnConsole("       └────────────────────────────────────────────────────────┘");
            console.esperarEnter("");
            sceneController.trocarCena(this);
            return;
        }

        // 2. Validação de Energia a 100%
        if (attr.getEnergia() < 100) {
            console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
            console.printlnConsole("║                  ENERGIA INSUFICIENTE (EXIGE ENERGIA CHEIA)          ║");
            console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
            console.printlnConsole("  [!] O treinador barrou você na entrada do ringue de treino!");
            console.printlnConsole("  [!] Para render no ápice e não lesionar, seu atleta precisa de Energia cheia (100).");
            console.printlnConsole("  • Sua Energia atual : " + attr.getEnergia() + " / 100");
            console.printlnConsole("  ► Vá para sua Casa no mapa para dormir e restaurar suas forças!\n");

            console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
            console.printlnConsole("       │              [ ENTER ]  Voltar                         │");
            console.printlnConsole("       └────────────────────────────────────────────────────────┘");
            console.esperarEnter("");
            sceneController.trocarCena(this);
            return;
        }

        // 3. Validação de Dinheiro (50 Reais)
        if (player.getDinheiro() < CUSTO_TREINO_DINHEIRO) {
            console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
            console.printlnConsole("║                  DINHEIRO INSUFICIENTE (EXIGE 50 REAIS)              ║");
            console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
            console.printlnConsole("  [!] O treinador barrou você na recepção da academia!");
            console.printlnConsole("  [!] A taxa de uso do ringue e equipamentos é de " + CUSTO_TREINO_DINHEIRO + " Reais.");
            console.printlnConsole("  • Seu Dinheiro atual : " + player.getDinheiro() + " Reais\n");

            console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
            console.printlnConsole("       │              [ ENTER ]  Voltar                         │");
            console.printlnConsole("       └────────────────────────────────────────────────────────┘");
            console.esperarEnter("");
            sceneController.trocarCena(this);
            return;
        }

        // 4. Validação de Limite Máximo
        if (valorAtual >= Atributo.MAX_HABILIDADE) {
            console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
            console.printlnConsole("║                          NÍVEL MÁXIMO ATINGIDO                       ║");
            console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
            console.printlnConsole("  [i] Sua " + nome + " já atingiu o ápice possível de desenvolvimento (" + Atributo.MAX_HABILIDADE + "/10)!");
            console.printlnConsole("  ► Foque em outros fundamentos para tornar seu atleta completo.\n");

            console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
            console.printlnConsole("       │              [ ENTER ]  Voltar                         │");
            console.printlnConsole("       └────────────────────────────────────────────────────────┘");
            console.esperarEnter("");
            sceneController.trocarCena(this);
            return;
        }

        // 5. Execução do treino diário: Consome energia, desconta dinheiro, avança o tempo e marca como treinado hoje
        attr.setEnergia(attr.getEnergia() - custoEnergia);
        player.setDinheiro(player.getDinheiro() - CUSTO_TREINO_DINHEIRO);
        gameController.getDataDia().avancarMinutos(120);
        player.setUltimoDiaTreinado(diaAtual);

        // 6. Chance de evolução (50% de chance de sucesso)
        boolean sucessoEvolucao = random.nextInt(100) < 50;

        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                    SESSÃO DE TREINO CONCLUÍDA!                       ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");

        if (sucessoEvolucao) {
            incremento.run();
            console.printlnConsole("  ✓ EXCELENTE DESEMPENHO! (Sucesso nos 50% de chance de evolução)");
            console.printlnConsole("  ✓ " + nome + " evoluiu com sucesso para: " + (valorAtual + 1) + " / " + Atributo.MAX_HABILIDADE);
        } else {
            console.printlnConsole("  • O treino foi árduo, mas seu corpo não absorveu o ganho desta vez (Falha nos 50% de chance).");
            console.printlnConsole("  • Não houve ganho de atributo desta vez (" + nome + " permanece em: " + valorAtual + " / " + Atributo.MAX_HABILIDADE + ").");
            console.printlnConsole("  ► Mantenha a disciplina nos próximos dias para consolidar sua evolução!");
        }

        console.printlnConsole("");
        console.printlnConsole("  • Custo do Treino  : -" + CUSTO_TREINO_DINHEIRO + " Reais (Dinheiro restante: " + player.getDinheiro() + " Reais)");
        console.printlnConsole("  • Gasto de Energia : -" + custoEnergia + " (Restante: " + attr.getEnergia() + "/100)");
        console.printlnConsole("  • Treino do Dia    : Concluído (Você só poderá treinar novamente amanhã)");
        console.printlnConsole("  • Tempo decorrido  : +2 Horas (" + gameController.getDataDia().getHoraFormatada() + ")\n");

        console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
        console.printlnConsole("       │              [ ENTER ]  Continuar na Academia          │");
        console.printlnConsole("       └────────────────────────────────────────────────────────┘");
        console.esperarEnter("");
        sceneController.trocarCena(this);
    }
}
