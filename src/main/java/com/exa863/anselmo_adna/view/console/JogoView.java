package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.view.View;
import java.io.IOException;

public class JogoView implements View {

    private final Console console;
    private final SceneController sceneController;
    private final GameController gameController;

    public JogoView(Console console, SceneController sceneController, GameController gameController) {
        this.console = console;
        this.sceneController = sceneController;
        this.gameController = gameController;
    }

    @Override
    public void render() {
        console.clearConsole();

        Player player = gameController.getPlayer();
        String nomePlayer = player != null ? player.getNome() : "Boxeador";
        int saude = player != null ? player.getAtributos().getSaude() : 100;
        int energia = player != null ? player.getAtributos().getEnergia() : 100;
        int dinheiro = player != null ? player.getDinheiro() : 0;
        String localNome = gameController.getLocalAtual() != null ? gameController.getLocalAtual().getNome() : "Casa";
        String dataHora = gameController.getDataDia().getDiaFormatado() + " • " + gameController.getDataDia().getHoraFormatada();

        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║               CENTRO DE TREINAMENTO & ROTINA (HUB)                   ║");
        console.printlnConsole("╠══════════════════════════════════════════════════════════════════════╣");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("║  ► ATLETA  : " + String.format("%-20s", nomePlayer) + " │ DATA   : " + String.format("%-24s", dataHora) + "║");
        console.printlnConsole("║  ► SAÚDE   : " + String.format("%-20s", saude + " / 100") + " │ LOCAL  : " + String.format("%-24s", localNome) + "║");
        console.printlnConsole("║  ► ENERGIA : " + String.format("%-20s", energia + " / 100") + " │ BOLSA  : " + String.format("%-24s", dinheiro + " Reais") + "║");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");
        console.printlnConsole("");
        console.printlnConsole("  O que deseja fazer hoje? Escolha com [ ▲ / ▼ ] e [ ENTER ]:\n");

        CMultiplaEscolha menuJogo = new CMultiplaEscolha(console);
        CEscolha[] opcoes = {
                new CEscolha("Treinar na Academia (Gasta 2h)", 0),
                new CEscolha("Dormir e Descansar (Avança 8h)", 1),
                new CEscolha("Explorar o Mapa (Cidades e Locais)", 2),
                new CEscolha("Abrir Inventário", 3),
                new CEscolha("Voltar ao Menu Principal", 4)
        };
        try {
            CEscolha escolha = menuJogo.escolha(opcoes);
            switch (escolha.index) {

                case 0 -> {gameController.getDataDia().avancarMinutos(120);
                    sceneController.trocarCena(this);
                }
                case 1 -> {gameController.getDataDia().avancarMinutos(480);sceneController.trocarCena(this);
                }
                case 2 -> {sceneController.trocarCena(new MapaView(console, sceneController, gameController));
                }
                case 3 -> sceneController.trocarCena(new InventarioView(console, sceneController, gameController));
                case 4 ->
                        sceneController.trocarCena(new MenuView(console, sceneController, gameController));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
