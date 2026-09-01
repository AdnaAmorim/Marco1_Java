package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.view.SceneManager;
import com.exa863.anselmo_adna.view.View;
import java.io.IOException;

public class JogoView implements View {

    private final Console console;
    private final SceneManager sceneManager;
    private final GameController gameController;

    public JogoView(Console console, SceneManager sceneManager, GameController gameController) {
        this.console = console;
        this.sceneManager = sceneManager;
        this.gameController = gameController;
    }

    @Override
    public void render() {
        console.printlnConsole("=== " + gameController.getDataDia().getDiaFormatado() + " | " + gameController.getDataDia().getHoraFormatada() + " ===");

        CMultiplaEscolha menuJogo = new CMultiplaEscolha(console);
        CEscolha[] opcoes = {
                new CEscolha("Treinar Boxe (Gasta 2 horas)", 0),
                new CEscolha("Dormir (Gasta 8 horas)", 1),
                new CEscolha("Voltar ao Menu Principal", 2)
        };

        try {
            CEscolha escolha = menuJogo.escolha(opcoes);

            switch (escolha.index) {
                case 0 -> {
                    gameController.getDataDia().avancarMinutos(120);
                    sceneManager.trocarCena(this);
                }
                case 1 -> {
                    gameController.getDataDia().avancarMinutos(480);
                    sceneManager.trocarCena(this);
                }
                case 2 -> sceneManager.trocarCena(new MenuView(console, sceneManager, gameController));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
