package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CasaView implements View {

    private final Console console;
    private final SceneController sceneController;
    private final GameController gameController;
    private final Random random = new Random();

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
        console.printlnConsole("  O que você deseja fazer em sua casa? [ ▲ / ▼ ] e [ ENTER ]:\n");

        CMultiplaEscolha menu = new CMultiplaEscolha(console);
        CEscolha[] opcoes = new CEscolha[]{
                new CEscolha("Dormir e Descansar (Avança 8h e Restaura Energia)", 0),
                new CEscolha("Ver Estatísticas dos Atributos", 1),
                new CEscolha("Sair de Casa (Voltar para a Cidade)", 2)
        };

        try {
            CEscolha escolha = menu.escolha(opcoes);

            switch (escolha.index) {
                case 0 -> dormir(attr);
                case 1 -> verEstatisticas(player, attr);
                case 2 -> {
                    gameController.voltarLocal();
                    sceneController.trocarCena(new MapaView(console, sceneController, gameController));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dormir(Atributo attr) {
        console.clearConsole();

        if (attr.getEnergia() >= 100) {
            console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
            console.printlnConsole("║                          SEM SONO / ENERGIA CHEIA                    ║");
            console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
            console.printlnConsole("  [!] Você se deitou, mas rolou de um lado para o outro sem conseguir dormir.");
            console.printlnConsole("  [!] Seu corpo está com energia cheia (100 / 100).");
            console.printlnConsole("  ► Você precisa gastar energia (treinar na Academia) antes de descansar!\n");

            console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
            console.printlnConsole("       │              [ ENTER ]  Voltar                         │");
            console.printlnConsole("       └────────────────────────────────────────────────────────┘");
            console.esperarEnter("");
            sceneController.trocarCena(this);
            return;
        }

        // Recupera 100% de energia e saúde
        attr.setEnergia(Atributo.MAX_ENERGIA);
        attr.setSaude(Atributo.MAX_SAUDE);
        gameController.getDataDia().avancarMinutos(480);

        // 60% de chance de perder 1 ponto em um atributo aleatório por excesso de descanso/destreinamento
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

        console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
        console.printlnConsole("       │              [ ENTER ]  Levantar da Cama               │");
        console.printlnConsole("       └────────────────────────────────────────────────────────┘");
        console.esperarEnter("");
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

        console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
        console.printlnConsole("       │              [ ENTER ]  Voltar                         │");
        console.printlnConsole("       └────────────────────────────────────────────────────────┘");
        console.esperarEnter("");
        sceneController.trocarCena(this);
    }
}
