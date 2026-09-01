package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.view.SceneManager;
import com.exa863.anselmo_adna.view.View;
import com.exa863.anselmo_adna.view.console.art.MenuInicialASCII;

public class SplashScreenView implements View {

    private final Console console;
    private final SceneManager sceneManager;
    private final GameController gameController;

    public SplashScreenView(Console console, SceneManager sceneManager, GameController gameController) {
        this.console = console;
        this.sceneManager = sceneManager;
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

            sceneManager.trocarCena(new MenuView(console, sceneManager, gameController));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
