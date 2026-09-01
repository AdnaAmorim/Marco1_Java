package com.exa863.anselmo_adna;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.view.SceneManager;
import com.exa863.anselmo_adna.view.console.Console;
import com.exa863.anselmo_adna.view.console.SplashScreenView;

public class Main {

    public static void main(String[] args) {
        Console console = new Console();
        SceneManager sceneManager = new SceneManager(console);
        GameController gameController = new GameController();

        sceneManager.trocarCena(new SplashScreenView(console, sceneManager, gameController));
    }
}
