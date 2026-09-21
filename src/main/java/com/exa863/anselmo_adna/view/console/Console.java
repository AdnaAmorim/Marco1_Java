package com.exa863.anselmo_adna.view.console;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Abstração do terminal do jogo.
 * Centraliza todas as operações de leitura, escrita, limpeza de tela, controle de cursor e delays de animação.
 *
 * @author Anselmo e Adna
 */
public class Console {

    // MODO DEV: quando true, anula todos os delays de animação e digitação, imprimindo instantaneamente
    public static boolean MODO_DEV = false;

    private final Terminal terminal;
    private final BufferedReader reader;
    private final LineReader lineReader;

    private static final int espacamentoPadrao = 4;
    public static String espacamento = " ".repeat(espacamentoPadrao);

    public Console() {
        try {
            this.terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();

            this.reader = new BufferedReader(terminal.reader());
            this.lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Não foi possível inicializar o terminal.",
                    e
            );
        }
    }

    public void printConsole(String output) {
        terminal.writer().print(output);
        terminal.writer().flush();
    }

    public void printlnConsole(String output) {
        terminal.writer().println(output);
        terminal.writer().flush();
    }

    public String getEntrada(String input) {
        return getEntrada(input, " ");
    }

    public String getEntrada(String input, String separacao) {
        setCursorVisivel();
        if (lineReader != null) {
            try {
                return lineReader.readLine(input + separacao);
            } catch (Exception ignored) {
            }
        }
        printConsole(input + separacao);
        return getEntradaString();
    }

    public void setCursorInvisivel() {
        terminal.puts(InfoCmp.Capability.cursor_invisible);
        terminal.flush();
    }

    public void setCursorVisivel() {
        terminal.puts(InfoCmp.Capability.cursor_visible);
        terminal.flush();
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public int getLargura() {
        return terminal.getWidth();
    }

    public int getAltura() {
        return terminal.getHeight();
    }

    // Métodos utilitários

    private String getEntradaString() {
        setCursorVisivel();
        if (lineReader != null) {
            try {
                return lineReader.readLine();
            } catch (Exception ignored) {
            }
        }
        try {
            return reader.readLine();
        } catch (IOException e) {
            return "";
        }
    }

    public Integer getEntradaInt() {
        String entradaString = getEntradaString();

        try {
            return Integer.parseInt(entradaString.trim());
        } catch (Exception e) {
            return null;
        }
    }

    public Integer getEntradaInt(String prompt) {
        String entradaString = getEntrada(prompt);
        try {
            return Integer.parseInt(entradaString.trim());
        } catch (Exception e) {
            return null;
        }
    }

    public void limparBufferTeclado() {
        try {
            while (terminal.reader().ready()) {
                terminal.reader().read();
            }
        } catch (Exception ignored) {
        }
    }

    public boolean enterPressionado() {
        try {
            if (terminal.reader().ready()) {
                while (terminal.reader().ready()) {
                    terminal.reader().read();
                }
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    public void esperarEnter(String mensagem) {
        setCursorInvisivel();
        limparBufferTeclado();
        printConsole(mensagem);
        while (!enterPressionado()) {
            try { Thread.sleep(50); } catch (Exception e) {}
        }
        printConsole("\r" + " ".repeat(mensagem.length() + 10) + "\r");
    }

    public void clearConsole() {
        printConsole(espacamento);
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.writer().print("\033[H\033[2J\033[3J");
        terminal.writer().flush();
    }

    public void animarFramesTempo(String[] frames, long duracaoMs, long intervaloMs) {
        if (MODO_DEV) {
            return;
        }
        setCursorInvisivel();
        long endTime = System.currentTimeMillis() + duracaoMs;
        int frameIndex = 0;
        int maxLen = 0;

        for (String f : frames) {
            if (f != null && f.length() > maxLen) {
                maxLen = f.length();
            }
        }

        while (System.currentTimeMillis() < endTime) {
            String frame = frames[frameIndex];
            int pad = maxLen - (frame != null ? frame.length() : 0);
            printConsole("\r" + frame + (pad > 0 ? " ".repeat(pad) : ""));
            frameIndex = (frameIndex + 1) % frames.length;
            try { Thread.sleep(intervaloMs); } catch (InterruptedException ignored) {}
        }
        setCursorVisivel();
    }

    public void animarFramesAteEnter(String[] frames, long intervaloMs, String sufixo) {
        setCursorInvisivel();
        enterPressionado(); // Limpa o buffer antes de começar
        int frameIndex = 0;
        int maxLen = 0;

        for (String f : frames) {
            if (f != null && f.length() > maxLen) {
                maxLen = f.length();
            }
        }

        while (!enterPressionado()) {
            String frame = frames[frameIndex];
            int pad = maxLen - (frame != null ? frame.length() : 0);
            printConsole("\r" + frame + (pad > 0 ? " ".repeat(pad) : "") + sufixo);
            frameIndex = (frameIndex + 1) % frames.length;
            try { Thread.sleep(intervaloMs); } catch (InterruptedException ignored) {}
        }
        setCursorVisivel();
    }

    public void animarTextoSequencial(String[] blocos, long intervaloLetraMs, long intervaloBlocoMs) {
        if (MODO_DEV) {
            for (String bloco : blocos) {
                printConsole(bloco);
            }
            return;
        }
        for (String bloco : blocos) {
            for (char ch : bloco.toCharArray()) {
                printConsole(String.valueOf(ch));
                try { Thread.sleep(intervaloLetraMs); } catch (Exception e) {}
            }
            try { Thread.sleep(intervaloBlocoMs); } catch (Exception e) {}
        }
    }

    public void printDigitado(String texto, long delayMs) {
        setCursorInvisivel();
        if (MODO_DEV || delayMs <= 0) {
            printlnConsole(texto);
            return;
        }
        for (char c : texto.toCharArray()) {
            printConsole(String.valueOf(c));
            try {
                if (c == '.' || c == '!' || c == '?') {
                    Thread.sleep(150);
                } else if (c == ',' || c == ':') {
                    Thread.sleep(80);
                } else {
                    Thread.sleep(delayMs);
                }
            } catch (InterruptedException ignored) {}
        }
        printlnConsole("");
    }

    public void printDigitado(String texto) {
        printDigitado(texto, 35);
    }
}
