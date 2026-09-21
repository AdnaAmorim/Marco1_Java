package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.view.View;
import com.exa863.anselmo_adna.model.narrativa.Dialogos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Tela da casa do jogador.
 * Permite descansar para recuperar energia e passar o tempo, fazer treinos caseiros
 * e conversar com os avós.
 *
 * @author Anselmo e Adna
 */
public class CasaView implements View {

    private static final int CHANCE_FALA_AVOS = 40;

    private final Console console;
    private final SceneController sceneController;
    private final GameController gameController;
    private final Random random = new Random();

    // Sorteia a fala dos avós só uma vez por visita
    private boolean falaAvosJaTentada = false;

    public CasaView(Console console, SceneController sceneController, GameController gameController) {
        this.console = console;
        this.sceneController = sceneController;
        this.gameController = gameController;
    }

    @Override
    public void render() {
        Player player = gameController.getPlayer();
        Atributo attr = player.getAtributos();
        String dataHora = gameController.getDataDia().getDiaFormatado() + " • " + gameController.getDataDia().getHoraFormatada();
        int diaAtual = gameController.getDataDia().getDias();

        console.clearConsole();
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                          CASA DO ATLETA                              ║");
        console.printlnConsole("╠══════════════════════════════════════════════════════════════════════╣");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("║  ► ATLETA  : " + String.format("%-20s", player.getNome()) + " │ DATA   : " + String.format("%-24s", dataHora) + "║");
        console.printlnConsole("║  ► SAÚDE   : " + String.format("%-20s", attr.getSaude() + " / 100") + " │ ENERGIA: " + String.format("%-24s", attr.getEnergia() + " / 100") + "║");
        console.printlnConsole("║  ► DINHEIRO: " + String.format("%-46s", player.getDinheiro() + " Reais") + "║");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");
        console.printlnConsole("");

        exibirFalaAleatoriaAvosSeAplicavel();

        console.printlnConsole("  O que você deseja fazer em sua casa? [ ▲ / ▼ ] e [ ENTER ]:");
        console.printlnConsole("  [ Treinos diários restantes: " + player.getTreinosRestantes(diaAtual) + " / " + Player.MAX_TREINOS_POR_DIA + " ]\n");

        CMultiplaEscolha menu = new CMultiplaEscolha(console);
        CEscolha[] opcoes = new CEscolha[]{
                new CEscolha("Dormir e Descansar (Avança 8h e Restaura Energia)", 0),
                new CEscolha("Treinar no Saco Caseiro [-25 EN, Grátis, 20% de chance para cada Atributo]", 1),
                new CEscolha("Ver Estatísticas dos Atributos", 2),
                new CEscolha("Sair de Casa (Voltar para a Cidade)", 3)
        };

        try {
            CEscolha escolha = menu.escolha(opcoes);

            switch (escolha.index) {
                case 0 -> dormir(attr);
                case 1 -> treinarEmCasa(player, attr, diaAtual);
                case 2 -> verEstatisticas(player, attr);
                case 3 -> {
                    gameController.voltarLocal();
                    sceneController.trocarCena(new MapaView(console, sceneController, gameController));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Sorteia (uma única vez por visita) se uma fala aleatória dos avós vai aparecer
    private void exibirFalaAleatoriaAvosSeAplicavel() {
        if (falaAvosJaTentada) {
            return;
        }
        falaAvosJaTentada = true;

        if (random.nextInt(100) < CHANCE_FALA_AVOS) {
            console.printlnConsole("  ◈ " + Dialogos.obterDialogoAleatorioAvos());
            console.printlnConsole("");
        }
    }

    private void treinarEmCasa(Player player, Atributo attr, int diaAtual) {
        console.clearConsole();

        if (!player.podeTreinarHoje(diaAtual)) {
            console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
            console.printlnConsole("║                        LIMITE DIÁRIO ATINGIDO                        ║");
            console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
            console.printlnConsole("  [!] Você já completou os 5 treinos permitidos hoje.");
            console.printlnConsole("  ► Vá dormir para avançar o dia e recuperar as energias!\n");

            console.esperarEnter("       [ ENTER ] Voltar");
            sceneController.trocarCena(this);
            return;
        }

        if (attr.getEnergia() < 25) {
            console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
            console.printlnConsole("║                  ENERGIA INSUFICIENTE (EXIGE 25)                     ║");
            console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
            console.printlnConsole("  [!] Você não tem energia suficiente para render no treino caseiro.");
            console.printlnConsole("  • Sua Energia atual : " + attr.getEnergia() + " / 100");
            console.printlnConsole("  ► Deite-se na cama para descansar antes de voltar a treinar.\n");

            console.esperarEnter("       [ ENTER ] Voltar");
            sceneController.trocarCena(this);
            return;
        }

        attr.setEnergia(attr.getEnergia() - 25);
        gameController.getDataDia().avancarMinutos(120);
        player.registrarTreino(diaAtual);

        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                 TREINO NO SACO CASEIRO CONCLUÍDO!                    ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");

        boolean evoluiuAlgo = false;

        if (random.nextInt(100) < 20 && attr.getForca() < Atributo.MAX_HABILIDADE) {
            attr.setForca(attr.getForca() + 1);
            console.printlnConsole("  ✓ Ganho de FORÇA! Nível atual: " + attr.getForca() + " / 10");
            evoluiuAlgo = true;
        }
        if (random.nextInt(100) < 20 && attr.getAgilidade() < Atributo.MAX_HABILIDADE) {
            attr.setAgilidade(attr.getAgilidade() + 1);
            console.printlnConsole("  ✓ Ganho de AGILIDADE! Nível atual: " + attr.getAgilidade() + " / 10");
            evoluiuAlgo = true;
        }
        if (random.nextInt(100) < 20 && attr.getResistencia() < Atributo.MAX_HABILIDADE) {
            attr.setResistencia(attr.getResistencia() + 1);
            console.printlnConsole("  ✓ Ganho de RESISTÊNCIA! Nível atual: " + attr.getResistencia() + " / 10");
            evoluiuAlgo = true;
        }
        if (random.nextInt(100) < 20 && attr.getInteligencia() < Atributo.MAX_HABILIDADE) {
            attr.setInteligencia(attr.getInteligencia() + 1);
            console.printlnConsole("  ✓ Ganho de INTELIGÊNCIA! Nível atual: " + attr.getInteligencia() + " / 10");
            evoluiuAlgo = true;
        }

        if (!evoluiuAlgo) {
            console.printlnConsole("  • Você bateu duro no saco de areia, mas não sentiu ganhos técnicos consideráveis hoje.");
            console.printlnConsole("  • Nenhum atributo subiu de nível. A consistência é a chave do sucesso!");
        }

        console.printlnConsole("\n  • Gasto de Energia : -25 (Restante: " + attr.getEnergia() + "/100)");
        console.printlnConsole("  • Treino do Dia    : Concluído");
        console.printlnConsole("  • Tempo decorrido  : +2 Horas (" + gameController.getDataDia().getHoraFormatada() + ")\n");

        console.esperarEnter("       [ ENTER ] Continuar");
        sceneController.trocarCena(this);
    }


    private void dormir(Atributo attr) {
        console.clearConsole();

        if (attr.getEnergia() >= 100) {
            console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
            console.printlnConsole("║                          SEM SONO / ENERGIA CHEIA                    ║");
            console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
            console.printlnConsole("  [!] Você se deitou, mas rolou de um lado para o outro sem conseguir dormir.");
            console.printlnConsole("  [!] Seu corpo está com energia cheia (100 / 100).");
            console.printlnConsole("  ► Você precisa gastar energia (treinar) antes de descansar!\n");

            console.esperarEnter("       [ ENTER ] Voltar");
            sceneController.trocarCena(this);
            return;
        }

        attr.setEnergia(Atributo.MAX_ENERGIA);
        attr.setSaude(Atributo.MAX_SAUDE);
        gameController.getDataDia().avancarMinutos(480);

        boolean perdeuAtributo = random.nextInt(100) < 60;
        String atributoPerdidoNome = null;
        int novoValorAtributo = 0;

        if (perdeuAtributo) {
            List<String> atributosElegiveis = new ArrayList<>();
            if (attr.getForca() > 1) atributosElegiveis.add("Força");
            if (attr.getAgilidade() > 1) atributosElegiveis.add("Agilidade");
            if (attr.getResistencia() > 1) atributosElegiveis.add("Resistência");
            if (attr.getInteligencia() > 1) atributosElegiveis.add("Inteligência");

            if (!atributosElegiveis.isEmpty()) {
                atributoPerdidoNome = atributosElegiveis.get(random.nextInt(atributosElegiveis.size()));
                switch (atributoPerdidoNome) {
                    case "Força" -> {
                        attr.setForca(attr.getForca() - 1);
                        novoValorAtributo = attr.getForca();
                    }
                    case "Agilidade" -> {
                        attr.setAgilidade(attr.getAgilidade() - 1);
                        novoValorAtributo = attr.getAgilidade();
                    }
                    case "Resistência" -> {
                        attr.setResistencia(attr.getResistencia() - 1);
                        novoValorAtributo = attr.getResistencia();
                    }
                    case "Inteligência" -> {
                        attr.setInteligencia(attr.getInteligencia() - 1);
                        novoValorAtributo = attr.getInteligencia();
                    }
                }
            }
        }

        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                      DESCANSO REVIGORANTE                            ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
        console.printlnConsole("  ► Você deitou em sua cama e teve 8 horas de sono contínuo.");
        console.printlnConsole("  ✓ Energia restaurada : 100 / 100 (Pronto para treinar!)");
        console.printlnConsole("  ✓ Saúde restaurada   : 100 / 100");
        console.printlnConsole("  • Novo Horário       : " + gameController.getDataDia().getDiaFormatado() + " • " + gameController.getDataDia().getHoraFormatada() + "\n");

        if (atributoPerdidoNome != null) {
            console.printlnConsole("  ┌────────────────────────────────────────────────────────────────────┐");
            console.printlnConsole("  │ [!] DESTREINAMENTO POR EXCESSO DE DESCANSO:                        │");
            console.printlnConsole("  │     Dormir por muito tempo fez seu corpo relaxar e perder ritmo!   │");
            console.printlnConsole("  │     -1 Ponto em " + String.format("%-14s", atributoPerdidoNome) + " (Novo nível: " + String.format("%-26s", novoValorAtributo + " / 10)") + "│");
            console.printlnConsole("  └────────────────────────────────────────────────────────────────────┘\n");
        } else {
            console.printlnConsole("  ┌────────────────────────────────────────────────────────────────────┐");
            console.printlnConsole("  │ [✓] CONDICIONAMENTO PRESERVADO:                                    │");
            console.printlnConsole("  │     Você descansou bem e manteve todo o seu condicionamento físico!│");
            console.printlnConsole("  └────────────────────────────────────────────────────────────────────┘\n");
        }

        console.esperarEnter("       [ ENTER ] Levantar da Cama");
        sceneController.trocarCena(this);
    }

    private void verEstatisticas(Player player, Atributo attr) {
        console.clearConsole();
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                     ATRIBUTOS DO BOXEADOR                            ║");
        console.printlnConsole("╠══════════════════════════════════════════════════════════════════════╣");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("║  ► Força        : " + String.format("%-50s", attr.getForca() + " / 10") + "║");
        console.printlnConsole("║  ► Agilidade    : " + String.format("%-50s", attr.getAgilidade() + " / 10") + "║");
        console.printlnConsole("║  ► Resistência  : " + String.format("%-50s", attr.getResistencia() + " / 10") + "║");
        console.printlnConsole("║  ► Inteligência : " + String.format("%-50s", attr.getInteligencia() + " / 10") + "║");
        console.printlnConsole("║  ► Saúde        : " + String.format("%-50s", attr.getSaude() + " / 100") + "║");
        console.printlnConsole("║  ► Energia      : " + String.format("%-50s", attr.getEnergia() + " / 100") + "║");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");

        console.esperarEnter("       [ ENTER ] Voltar");
        sceneController.trocarCena(this);
    }
}