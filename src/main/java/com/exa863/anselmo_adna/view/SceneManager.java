package com.exa863.anselmo_adna.view;

import com.exa863.anselmo_adna.view.console.Console;

public class SceneManager {

    private final Console console;
    private View cenaAtual;

    public SceneManager(Console console) {
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
