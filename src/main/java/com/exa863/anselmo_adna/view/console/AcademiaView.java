package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.view.View;

import java.io.IOException;
import java.util.Random;

public class AcademiaView implements View {

    private static final int CUSTO_ENTRADA = 50;

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
        console.clearConsole();
        Player player = gameController.getPlayer();
        Atributo attr = player.getAtributos();
        int diaAtual = gameController.getDataDia().getDias();

        // 1. Validação de Limite de Treinos Diários
        if (!player.podeTreinarHoje(diaAtual)) {
            console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
            console.printlnConsole("║                        LIMITE DIÁRIO ATINGIDO                        ║");
            console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
            console.printlnConsole("  [!] O treinador barrou você: você já completou os 5 treinos permitidos hoje!");
            console.printlnConsole("  [!] Seu corpo precisa de repouso absoluto antes de voltar a golpear.");
            console.printlnConsole("  ► Vá para sua Casa no mapa descansar e dormir para avançar o dia!\n");

            console.esperarEnter("       [ ENTER ] Voltar ao Mapa");
            gameController.voltarLocal();
            sceneController.trocarCena(new MapaView(console, sceneController, gameController));
            return;
        }

        // 2. Validação de Energia (Exige apenas o custo do treino: 25)
        if (attr.getEnergia() < 25) {
            console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
            console.printlnConsole("║                  ENERGIA INSUFICIENTE (EXIGE 25)                     ║");
            console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
            console.printlnConsole("  [!] O treinador barrou você na entrada do ringue de treino!");
            console.printlnConsole("  [!] Você precisa de pelo menos 25 de Energia para treinar.");
            console.printlnConsole("  • Sua Energia atual : " + attr.getEnergia() + " / 100");
            console.printlnConsole("  ► Vá para sua Casa no mapa para dormir e restaurar suas forças!\n");

            console.esperarEnter("       [ ENTER ] Voltar ao Mapa");
            gameController.voltarLocal();
            sceneController.trocarCena(new MapaView(console, sceneController, gameController));
            return;
        }

        // 3. Recepção e Pagamento da Taxa
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                       RECEPÇÃO DA ACADEMIA                           ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
        console.printlnConsole("  A catraca de acesso está bloqueada. É necessário pagar a diária.");
        console.printlnConsole("  • Taxa de Entrada: " + CUSTO_ENTRADA + " Reais");
        console.printlnConsole("  • Seu Dinheiro   : " + player.getDinheiro() + " Reais\n");

        CEscolha[] opcoesEntrada = new CEscolha[] {
                new CEscolha("Pagar " + CUSTO_ENTRADA + " Reais e liberar a catraca", 0),
                new CEscolha("Dar meia-volta e Voltar ao Mapa", 1)
        };

        try {
            CMultiplaEscolha menu = new CMultiplaEscolha(console);
            CEscolha escolha = menu.escolha(opcoesEntrada);

            if (escolha.index == 1) {
                gameController.voltarLocal();
                sceneController.trocarCena(new MapaView(console, sceneController, gameController));
                return;
            }

            if (player.getDinheiro() < CUSTO_ENTRADA) {
                console.printlnConsole("\n  [!] Dinheiro insuficiente para pagar a entrada.");
                console.esperarEnter("       [ ENTER ] Voltar ao Mapa");
                gameController.voltarLocal();
                sceneController.trocarCena(new MapaView(console, sceneController, gameController));
                return;
            }

            // Paga a entrada e avança para os aparelhos
            player.setDinheiro(player.getDinheiro() - CUSTO_ENTRADA);
            mostrarAparelhos(player, attr, diaAtual);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAparelhos(Player player, Atributo attr, int diaAtual) throws IOException {
        console.clearConsole();
        String dataHora = gameController.getDataDia().getDiaFormatado() + " • " + gameController.getDataDia().getHoraFormatada();
        int treinosRestantes = player.getTreinosRestantes(diaAtual);

        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                    ACADEMIA DE TREINAMENTO                           ║");
        console.printlnConsole("╠══════════════════════════════════════════════════════════════════════╣");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("║  ► ATLETA  : " + String.format("%-20s", player.getNome()) + " │ DATA   : " + String.format("%-24s", dataHora) + "║");
        console.printlnConsole("║  ► SAÚDE   : " + String.format("%-20s", attr.getSaude() + " / 100") + " │ ENERGIA: " + String.format("%-24s", attr.getEnergia() + " / 100") + "║");
        console.printlnConsole("║  ► FORÇA   : " + String.format("%-20s", attr.getForca() + " / 10") + " │ AGILID : " + String.format("%-24s", attr.getAgilidade() + " / 10") + "║");
        console.printlnConsole("║  ► RESIST  : " + String.format("%-20s", attr.getResistencia() + " / 10") + " │ INTEL  : " + String.format("%-24s", attr.getInteligencia() + " / 10") + "║");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");
        console.printlnConsole("");
        console.printlnConsole("  A catraca está liberada. Escolha o seu foco de treino de hoje:");
        console.printlnConsole("  [ Treinos diários restantes: " + treinosRestantes + " / " + Player.MAX_TREINOS_POR_DIA + " ]\n");

        CMultiplaEscolha menu = new CMultiplaEscolha(console);
        CEscolha[] opcoes = new CEscolha[]{
                new CEscolha("Treino de Força (Saco Pesado & Halteres)    [-25 EN, 50% Chance +1 FOR, 2h]", 0),
                new CEscolha("Treino de Agilidade (Corda & Esquiva)       [-25 EN, 50% Chance +1 AGI, 2h]", 1),
                new CEscolha("Treino de Resistência (Circuito Cardio)     [-25 EN, 50% Chance +1 RES, 2h]", 2),
                new CEscolha("Treino Tático & Sombra (Estudo de Boxe)     [-25 EN, 50% Chance +1 INT, 2h]", 3)
        };

        CEscolha escolha = menu.escolha(opcoes);

        switch (escolha.index) {
            case 0 -> treinarAtributo("Força", 25, () -> attr.setForca(attr.getForca() + 1), attr.getForca(), player, attr, diaAtual);
            case 1 -> treinarAtributo("Agilidade", 25, () -> attr.setAgilidade(attr.getAgilidade() + 1), attr.getAgilidade(), player, attr, diaAtual);
            case 2 -> treinarAtributo("Resistência", 25, () -> attr.setResistencia(attr.getResistencia() + 1), attr.getResistencia(), player, attr, diaAtual);
            case 3 -> treinarAtributo("Inteligência", 25, () -> attr.setInteligencia(attr.getInteligencia() + 1), attr.getInteligencia(), player, attr, diaAtual);
        }
    }

    private void treinarAtributo(String nome, int custoEnergia, Runnable incremento, int valorAtual, Player player, Atributo attr, int diaAtual) {
        console.clearConsole();

        if (valorAtual >= Atributo.MAX_HABILIDADE) {
            console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
            console.printlnConsole("║                          NÍVEL MÁXIMO ATINGIDO                       ║");
            console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
            console.printlnConsole("  [i] Sua " + nome + " já atingiu o ápice possível de desenvolvimento (" + Atributo.MAX_HABILIDADE + "/10)!");
            console.printlnConsole("  ► Você não pode treinar mais este atributo, mas preservou sua energia.\n");

            player.registrarTreino(diaAtual);

            console.esperarEnter("       [ ENTER ] Sair da Academia");
            gameController.voltarLocal();
            sceneController.trocarCena(new MapaView(console, sceneController, gameController));
            return;
        }

        attr.setEnergia(attr.getEnergia() - custoEnergia);
        gameController.getDataDia().avancarMinutos(120);
        player.registrarTreino(diaAtual);

        boolean sucessoEvolucao = random.nextInt(100) < 50;

        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                    SESSÃO DE TREINO CONCLUÍDA!                       ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");

        if (sucessoEvolucao) {
            incremento.run();
            console.printlnConsole("  ✓ EXCELENTE DESEMPENHO! (Sucesso nos 50% de chance de evolução)");
            console.printlnConsole("  ✓ " + nome + " evoluiu com sucesso para: " + (valorAtual + 1) + " / " + Atributo.MAX_HABILIDADE);
        } else {
            console.printlnConsole("  • O treino foi árduo, mas seu corpo não absorveu o ganho desta vez.");
            console.printlnConsole("  • Não houve ganho de atributo (" + nome + " permanece em: " + valorAtual + " / " + Atributo.MAX_HABILIDADE + ").");
        }

        console.printlnConsole("");
        console.printlnConsole("  • Gasto de Energia : -" + custoEnergia + " (Restante: " + attr.getEnergia() + "/100)");
        console.printlnConsole("  • Tempo decorrido  : +2 Horas (" + gameController.getDataDia().getHoraFormatada() + ")\n");

        console.esperarEnter("       [ ENTER ] Sair da Academia");
        gameController.voltarLocal();
        sceneController.trocarCena(new MapaView(console, sceneController, gameController));
    }
}