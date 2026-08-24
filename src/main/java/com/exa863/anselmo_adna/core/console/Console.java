package com.exa863.anselmo_adna.core.console;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.util.Scanner;

public class Console {

    private final Terminal terminal;
    private final Scanner scanner;

    private static final int espacamentoPadrao = 4;
    public static String espacamento = " ".repeat(espacamentoPadrao);

    public Console() {
        try {
            this.terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();

            this.scanner = new Scanner(terminal.reader());

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
        printConsole(input + " ");
        return getEntradaString();
    }

    public String getEntrada(String input, String separacao) {
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

    // Métodos utilitários

    private String getEntradaString() {
        return scanner.nextLine();
    }

    private Integer getEntradaInt() {
        String entradaString = getEntradaString();

        try {
            return Integer.parseInt(entradaString);
        } catch (Exception e) {
            return null;
        }
    }

    public void clearConsole() {
        printConsole(espacamento);
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.flush();
    }
}