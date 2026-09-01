package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.view.SceneManager;
import com.exa863.anselmo_adna.view.View;
import java.io.IOException;

public class MenuView implements View {

    private final Console console;
    private final SceneManager sceneManager;
    private final GameController gameController;

    public MenuView(Console console, SceneManager sceneManager, GameController gameController) {
        this.console = console;
        this.sceneManager = sceneManager;
        this.gameController = gameController;
    }

    @Override
    public void render() {
        CMultiplaEscolha menu = new CMultiplaEscolha(console);
        CEscolha[] opcoes = new CEscolha[]{
                new CEscolha("Jogar", 0),
                new CEscolha("Tutorial", 1),
                new CEscolha("Créditos", 2),
                new CEscolha("Sair", 3)
        };

        try {
            CEscolha escolha = menu.escolha(opcoes);

            switch (escolha.index) {
                case 0 -> sceneManager.trocarCena(new JogoView(console, sceneManager, gameController));
                case 1 -> {
                    console.printlnConsole("=== TUTORIAL ===");
                    console.printlnConsole("Treine boxe para evoluir os atributos do seu boxeador.");
                    console.printlnConsole("Durma para descansar e recuperar energia.");
                    console.printlnConsole("Pressione Enter para voltar ao menu...");
                    console.getEntrada("");
                    sceneManager.trocarCena(this);
                }
                case 2 -> {
                    console.printlnConsole("=== CRÉDITOS ===");
                    console.printlnConsole("Desenvolvido por: Anselmo e Adna");
                    console.printlnConsole("Algoritmos em Java - Marco 1");
                    console.printlnConsole("Pressione Enter para voltar ao menu...");
                    console.getEntrada("");
                    sceneManager.trocarCena(this);
                }
                case 3 -> {
                    console.printlnConsole("Saindo do jogo... Até mais!");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
