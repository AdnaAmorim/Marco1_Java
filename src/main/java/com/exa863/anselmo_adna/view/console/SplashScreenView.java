package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.controller.cenas.SplashScreenController;
import com.exa863.anselmo_adna.utils.AsciiBox;
import com.exa863.anselmo_adna.view.View;
import com.exa863.anselmo_adna.view.console.art.MenuInicialASCII;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SplashScreenView implements View {

    private final Console console;
    private final SceneController sceneController;
    private final GameController gameController;
    private final SplashScreenController controller;

    public SplashScreenView(Console console, SceneController sceneController, GameController gameController) {
        this(console, sceneController, gameController, new SplashScreenController());
    }

    public SplashScreenView(Console console, SceneController sceneController, GameController gameController, SplashScreenController controller) {
        this.console = console;
        this.sceneController = sceneController;
        this.gameController = gameController;
        this.controller = controller;
    }

    @Override
    public void render() {
        try {
            validarTamanho();

            console.setCursorInvisivel();
            console.clearConsole();
            Thread.sleep(400);
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

    private void validarTamanho() throws InterruptedException {
        // se a tela ja comecou no tamanho certo, a gente pula o aviso e ja vai direto
        if (controller.validarDimensoes(console.getLargura(), console.getAltura())) {
            return;
        }

        int ultimaLargura = -1;
        int ultimaAltura = -1;

        while (!controller.validarDimensoes(console.getLargura(), console.getAltura())) {
            int largura = console.getLargura();
            int altura = console.getAltura();

            // fica checando o tamanho em tempo real enquanto o jogador redimensiona a janela
            if (largura != ultimaLargura || altura != ultimaAltura) {
                ultimaLargura = largura;
                ultimaAltura = altura;
                exibirTabela(false);
            }

            // deixa o cara forcar o inicio com enter, mesmo com a tela muito apertada
            if (console.enterPressionado()) {
                return;
            }

            Thread.sleep(100);
        }

        // se a janela finalmente ficou do tamanho ideal, da o sinal verde pro jogador
        exibirTabela(true);
        Thread.sleep(2500);
    }

    private void exibirTabela(boolean valido) {
        console.clearConsole();

        List<String> linhasMeio = new ArrayList<>();
        List<String> linhasBaixo = new ArrayList<>();

        if (valido) {
            linhasMeio.add(String.format("Dimensões: %d x %d  (Mínimo: %d x %d)",
                    console.getLargura(), console.getAltura(), controller.getLarguraMinima(), controller.getAlturaMinima()));

            linhasBaixo.add("\u001B[32;1mTudo pronto!\u001B[0m");
            linhasBaixo.add("Iniciando o jogo...");
        } else {
            linhasMeio.add("O jogo foi desenvolvido para terminais maiores");
            linhasMeio.add("");
            linhasMeio.add(String.format("Tamanho atual: %d x %d  |  Mínimo recomendado: %d x %d",
                    console.getLargura(), console.getAltura(), controller.getLarguraMinima(), controller.getAlturaMinima()));

            linhasBaixo.add("\u001B[33;1mJanela pequena!\u001B[0m");
            linhasBaixo.add("Redimensione a janela ou aperte [ENTER] para continuar");
        }

        int totalLinhas = Math.max(controller.getAlturaMinima() - 3, 8);
        String[] conteudo = new String[totalLinhas];
        Arrays.fill(conteudo, "");

        // centraliza o texto de informacao bem no meio da nossa caixa ascii
        int inicioMeio = Math.max(0, (totalLinhas / 2) - (linhasMeio.size() / 2));
        for (int i = 0; i < linhasMeio.size(); i++) {
            conteudo[inicioMeio + i] = linhasMeio.get(i);
        }

        // o aviso mais importante fica colado no rodape pra ninguem ter que dar scroll pra ler
        int inicioBaixo = Math.max(0, totalLinhas - linhasBaixo.size() - 1);
        for (int i = 0; i < linhasBaixo.size(); i++) {
            conteudo[inicioBaixo + i] = linhasBaixo.get(i);
        }

        String cor = valido ? "\u001B[32m" : "\u001B[33m";
        String reset = "\u001B[0m";

        String caixa = new AsciiBox()
                .size(controller.getLarguraMinima())
                .borders(cor + "━" + reset, cor + "┃" + reset)
                .corners(cor + "┏" + reset, cor + "┓" + reset, cor + "┗" + reset, cor + "┛" + reset)
                .render(conteudo);

        console.printlnConsole(caixa);
    }
}