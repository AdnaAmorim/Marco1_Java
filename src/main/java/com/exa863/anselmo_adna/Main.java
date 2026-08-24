package com.exa863.anselmo_adna;

import com.exa863.anselmo_adna.arts.MenuInicialASCII;
import com.exa863.anselmo_adna.core.console.CEscolha;
import com.exa863.anselmo_adna.core.console.CMultiplaEscolha;
import com.exa863.anselmo_adna.core.console.Console;

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}