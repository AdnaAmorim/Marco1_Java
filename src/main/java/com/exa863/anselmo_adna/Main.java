package com.exa863.anselmo_adna;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.view.console.Console;
import com.exa863.anselmo_adna.view.console.SplashScreenView;

public class Main {

    public static void main(String[] args) {
        Console console = new Console();
        SceneController sceneController = new SceneController(console);
        GameController gameController = new GameController();

        sceneController.trocarCena(new SplashScreenView(console, sceneController, gameController));
    }
}

