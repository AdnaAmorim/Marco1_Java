package com.exa863.anselmo_adna.view.console;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;

import java.io.IOException;
import java.util.function.Consumer;

import static org.jline.utils.InfoCmp.Capability.key_down;
import static org.jline.utils.InfoCmp.Capability.key_up;

/**
 * Componente de menu interativo com seleção por teclado (setas ↑↓ e Enter).
 * Exibe as opções no console e retorna a escolha do usuário.
 *
 * @author Anselmo e Adna
 */
public class CMultiplaEscolha {

    private static final String KEY_UP = "UP";
    private static final String KEY_DOWN = "DOWN";
    private static final String KEY_ENTER = "ENTER";

    private final Console console;
    private final Terminal terminal;
    private final BindingReader reader;
    private final KeyMap<String> keys;

    public CMultiplaEscolha(Console console) {
        this.console = console;
        this.terminal = console.getTerminal();

        this.reader = new BindingReader(terminal.reader());

        this.keys = new KeyMap<>();
        this.keys.bind(KEY_UP, KeyMap.key(terminal, key_up), "\033[A");
        this.keys.bind(KEY_DOWN, KeyMap.key(terminal, key_down), "\033[B");
        this.keys.bind(KEY_ENTER, "\r", "\n");
    }

    public CEscolha escolha(CEscolha[] listaEscolha) throws IOException {
        return escolha(listaEscolha, null);
    }

    public CEscolha escolha(CEscolha[] listaEscolha, Consumer<CEscolha> mostrarDetalhes) throws IOException {
        if (listaEscolha.length == 0) {
            throw new IllegalArgumentException(
                    "Lista de escolhas vazia"
            );
        }

        console.limparBufferTeclado();

        Attributes atributosOriginais = terminal.enterRawMode();
        console.setCursorInvisivel();

        int selecionado = proximaOpcao(listaEscolha, -1, 1);

        try {
            while (true) {
                if (mostrarDetalhes != null) {
                    mostrarDetalhes.accept(listaEscolha[selecionado]);
                }
                printEscolhas(listaEscolha, selecionado);

                String key = reader.readBinding(keys);

                if (mostrarDetalhes == null) {
                    limparLinhas(listaEscolha.length);
                }

                if (key == null) {
                    continue;
                }

                switch (key) {
                    case KEY_UP ->
                            selecionado = proximaOpcao(listaEscolha, selecionado, -1);

                    case KEY_DOWN ->
                            selecionado = proximaOpcao(listaEscolha, selecionado, 1);

                    case KEY_ENTER -> {
                        return listaEscolha[selecionado];
                    }
                }
            }
        } finally {
            console.setCursorVisivel();
            terminal.setAttributes(atributosOriginais);
            terminal.flush();
        }
    }

    /** Anda na lista de forma circular, ignorando os separadores. */
    private int proximaOpcao(CEscolha[] listaEscolha, int atual, int passo) {
        int tamanho = listaEscolha.length;
        int proximo = atual;
        for (int i = 0; i < tamanho; i++) {
            proximo = (proximo + passo + tamanho) % tamanho;
            if (!listaEscolha[proximo].isSeparador()) {
                return proximo;
            }
        }
        return Math.max(atual, 0);
    }

    private void printEscolhas(
            CEscolha[] listaEscolha,
            int atual
    ) {
        for (int i = 0; i < listaEscolha.length; i++) {
            if (listaEscolha[i].isSeparador()) {
                console.printlnConsole("");
                continue;
            }

            String caracterSelecao = atual == i ? "❯ " : "  ";

            console.printlnConsole(
                    Console.espacamento + caracterSelecao + listaEscolha[i].titulo
            );
        }
    }

    private void limparLinhas(int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            console.printConsole("\033[1A\r\033[2K");
        }
    }
}
