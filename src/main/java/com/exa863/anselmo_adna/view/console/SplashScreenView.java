package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.view.View;
import com.exa863.anselmo_adna.view.console.art.MenuInicialASCII;

public class SplashScreenView implements View {

    private final Console console;
    private final SceneController sceneController;
    private final GameController gameController;

    public SplashScreenView(Console console, SceneController sceneController, GameController gameController) {
        this.console = console;
        this.sceneController = sceneController;
        this.gameController = gameController;
    }

    @Override
    public void render() {
        try {
            console.setCursorInvisivel();
            console.clearConsole();
            Thread.sleep(1000);
            console.clearConsole();
            Thread.sleep(2000);
            console.printlnConsole(MenuInicialASCII.boxeadorArt);
            Thread.sleep(2000);
            console.clearConsole();
            console.printlnConsole(MenuInicialASCII.tituloArt);
            Thread.sleep(2000);
            console.clearConsole();

            // ALTERAÇÃO: Ao terminar a logo, vai pro Menu!
            sceneController.trocarCena(
                    new MenuView(
                            console,
                            sceneController,
                            gameController
                    )
            );

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}