package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SceneController;
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
        console.printlnConsole("=== " + gameController.getDataDia().getDiaFormatado() + " | " + gameController.getDataDia().getHoraFormatada() + " ===");

       CMultiplaEscolha menuJogo = new CMultiplaEscolha(console);
        CEscolha[] opcoes = {
                new CEscolha("Ir para Academia", 0),
                new CEscolha("Dormir (Gasta 8 horas)", 1),
                new CEscolha("Explorar mapa", 2),
                new CEscolha("Ver Inventário", 3),
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
