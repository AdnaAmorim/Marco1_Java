package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.view.View;
import com.exa863.anselmo_adna.view.console.Console;

public class SceneController {

    private final Console console;
    private View cenaAtual;

    public SceneController(Console console) {
        this.console = console;
    }

    public void trocarCena(View novaCena) {
        console.clearConsole();
        this.cenaAtual = novaCena;
        if (this.cenaAtual != null) {
            this.cenaAtual.render();
        }
    }

    public View getCenaAtual() {
        return cenaAtual;
    }
}
