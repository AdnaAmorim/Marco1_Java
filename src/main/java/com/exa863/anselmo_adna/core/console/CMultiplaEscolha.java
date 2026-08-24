package com.exa863.anselmo_adna.core.console;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.io.IOException;

import static org.jline.utils.InfoCmp.Capability.key_down;
import static org.jline.utils.InfoCmp.Capability.key_up;

public class CMultiplaEscolha {

    // Esse mapeamento de keys aqui no incicio é para utilizarmos para mover o menu
    private static final String KEY_UP = "UP";
    private static final String KEY_DOWN = "DOWN";
    private static final String KEY_ENTER = "ENTER";

    // Classe principal utilitario de console
    private final Console console;
    // Classe do pacote de terminal, para utilizarmos ANSI
    private final Terminal terminal;
    // Utilizado pra ler as teclas (ou seja, as setas)
    private final BindingReader reader;
    // Mapeamento das teclas
    private final KeyMap<String> keys;

    public CMultiplaEscolha(Console console) {
        this.console = console;
        this.terminal = console.getTerminal();

        this.reader = new BindingReader(terminal.reader());

        this.keys = new KeyMap<>();
        this.keys.bind(KEY_UP, KeyMap.key(terminal, key_up));
        this.keys.bind(KEY_DOWN, KeyMap.key(terminal, key_down));
        this.keys.bind(KEY_ENTER, "\r", "\n");
    }

    public CEscolha escolha(CEscolha[] listaEscolha) throws IOException {
        if (listaEscolha.length == 0) {
            throw new IllegalArgumentException(
                    "Lista de escolhas vazia"
            );
        }

        Attributes atributosOriginais = terminal.enterRawMode();

        // Esconde o cursor
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
            // Mostra o cursor novamente
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
            console.printConsole("\033[1A");
            console.printConsole("\033[2K");
        }
    }
}