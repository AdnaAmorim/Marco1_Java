package com.exa863.anselmo_adna;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.view.console.CEscolha;
import com.exa863.anselmo_adna.view.console.CMultiplaEscolha;
import com.exa863.anselmo_adna.view.console.Console;
import com.exa863.anselmo_adna.view.console.art.MenuInicialASCII;

import java.io.IOException;

public class Main {

    private static final Console console = new Console();

    public static void main(String[] args) throws InterruptedException {
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

        CMultiplaEscolha a = new CMultiplaEscolha(console);
        CEscolha[] cEscolhaList = new CEscolha[]{
                new CEscolha("Jogar", 0),
                new CEscolha("Tutorial", 1),
                new CEscolha("Créditos", 2),
                new CEscolha("Sair", 3)
        };

        try {
            CEscolha escolha = a.escolha(cEscolhaList);

            console.printConsole(Console.espacamento + "Você escolheu: " + escolha.titulo + " [" + escolha.index + "]");

            if (escolha.index == 0) {
                iniciarJogo();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void iniciarJogo() throws InterruptedException {
        GameController game = new GameController();
        CMultiplaEscolha menuJogo = new CMultiplaEscolha(console);
        boolean rodando = true;

        while (rodando) {
            console.clearConsole();
            console.printlnConsole("=== " + game.getDataDia().getDiaFormatado() + " | " + game.getDataDia().getHoraFormatada() + " ===");

            CEscolha[] opcoes = {
                    new CEscolha("Treinar Boxe (Gasta 2 horas)", 0),
                    new CEscolha("Dormir (Gasta 8 horas)", 1)
            };

            try {
                CEscolha escolha = menuJogo.escolha(opcoes);

                if (escolha.index == 0) {
                    game.getDataDia().avancarMinutos(120); // Avança 2 horas
                } else if (escolha.index == 1) {
                    game.getDataDia().avancarMinutos(480); // Avança 8 horas
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
