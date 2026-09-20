package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.controller.cenas.CutsceneController;
import com.exa863.anselmo_adna.model.narrativa.Dialogo;
import com.exa863.anselmo_adna.model.narrativa.TipoEmissor;
import com.exa863.anselmo_adna.view.View;

import java.io.IOException;
import java.util.List;

public class CutsceneView implements View {

    private final Console console;
    private final SceneController sceneController;
    private final CutsceneController cutsceneController;
    private final View proximaCena;

    public CutsceneView(Console console, SceneController sceneController,
                        CutsceneController cutsceneController, View proximaCena) {
        this.console = console;
        this.sceneController = sceneController;
        this.cutsceneController = cutsceneController;
        this.proximaCena = proximaCena;
    }

    public CutsceneController getCutsceneController() {
        return cutsceneController;
    }

    @Override
    public void render() {
        console.clearConsole();
        imprimirCabecalho();

        List<Dialogo> dialogos = cutsceneController.getDialogos();
        for (Dialogo dialogo : dialogos) {
            processarDialogo(dialogo);
        }

        cutsceneController.finalizarCapitulo();

        console.printlnConsole("");
        console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
        console.printlnConsole("       │       [ ENTER ]  Capítulo Concluído : Continuar        │");
        console.printlnConsole("       └────────────────────────────────────────────────────────┘");
        console.esperarEnter("");

        if (sceneController != null && proximaCena != null) {
            sceneController.trocarCena(proximaCena);
        }
    }

    private void imprimirCabecalho() {
        String titulo = cutsceneController.getCapitulo().getTitulo().toUpperCase();
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║" + centralizar("HISTÓRIA : " + titulo, 70) + "║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
    }

    private void processarDialogo(Dialogo dialogo) {
        if (dialogo.isEscolha()) {
            exibirEscolha(dialogo);
        } else {
            exibirFala(dialogo);
        }
    }

    private void exibirFala(Dialogo dialogo) {
        String linha;
        if (dialogo.getTipoEmissor() == TipoEmissor.NARRADOR) {
            linha = "  ◈ [Narrador] " + dialogo.getTexto();
        } else if (dialogo.getTipoEmissor() == TipoEmissor.SISTEMA) {
            linha = "  ✦ [Sistema] " + dialogo.getTexto();
        } else {
            linha = "  ► " + dialogo.getNomeEmissor() + ": " + dialogo.getTexto();
        }

        console.printDigitado(linha);
        console.esperarEnter("  [ENTER]");
        console.printlnConsole("");
    }

    private void exibirEscolha(Dialogo escolhaDialogo) {
        console.printlnConsole("\n  ► " + escolhaDialogo.getTexto() + "\n");

        List<Dialogo.Opcao> opcoes = escolhaDialogo.getOpcoes();
        CEscolha[] escolhasUI = new CEscolha[opcoes.size()];

        for (int i = 0; i < opcoes.size(); i++) {
            escolhasUI[i] = new CEscolha(opcoes.get(i).getTexto(), i);
        }

        CMultiplaEscolha menu = new CMultiplaEscolha(console);
        try {
            CEscolha selecionada = menu.escolha(escolhasUI);
            Dialogo.Opcao opcaoEscolhida = opcoes.get(selecionada.index);

            console.printlnConsole("\n  [✓ Escolha]: " + opcaoEscolhida.getTexto() + "\n");

            // O controller processa a ação da escolha
            cutsceneController.processarEscolha(opcaoEscolhida);

            // Exibe os diálogos de desfecho
            for (Dialogo falaDesfecho : opcaoEscolhida.getDesfecho()) {
                exibirFala(falaDesfecho);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String centralizar(String texto, int largura) {
        if (texto.length() >= largura) {
            return texto.substring(0, largura);
        }
        int espacosTotais = largura - texto.length();
        int espacosEsquerda = espacosTotais / 2;
        int espacosDireita = espacosTotais - espacosEsquerda;
        return " ".repeat(espacosEsquerda) + texto + " ".repeat(espacosDireita);
    }
}
