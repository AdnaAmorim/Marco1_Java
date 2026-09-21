package com.exa863.anselmo_adna.view.console;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;

import java.io.IOException;

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
        if (listaEscolha.length == 0) {
            throw new IllegalArgumentException(
                    "Lista de escolhas vazia"
            );
        }

        console.limparBufferTeclado();

        Attributes atributosOriginais = terminal.enterRawMode();
        console.setCursorInvisivel();

        int selecionado = 0;

        try {
            while (true) {
                printEscolhas(listaEscolha, selecionado);

                String key = reader.readBinding(keys);

                limparLinhas(listaEscolha.length);

                if (key == null) {
                    continue;
                }

                switch (key) {
                    case KEY_UP ->
                            selecionado =
                                    (selecionado - 1 + listaEscolha.length)
                                            % listaEscolha.length;

                    case KEY_DOWN ->
                            selecionado =
                                    (selecionado + 1)
                                            % listaEscolha.length;

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

    private void printEscolhas(
            CEscolha[] listaEscolha,
            int atual
    ) {
        for (int i = 0; i < listaEscolha.length; i++) {
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
