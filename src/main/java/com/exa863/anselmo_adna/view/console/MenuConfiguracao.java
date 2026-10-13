package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.persistencia.Configuracao;
import com.exa863.anselmo_adna.persistencia.ConfiguracaoXML;
import java.io.IOException;

public class MenuConfiguracao {

    private final Console console;
    private final Configuracao config;

    public MenuConfiguracao(Console console, Configuracao config) {
        this.console = console;
        this.config = config;
    }

    public void mostrar() {

        CMultiplaEscolha menu = new CMultiplaEscolha(console);
        boolean aberto = true;

        while (aberto) {

            console.clearConsole();

            console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
            console.printlnConsole("║                        CONFIGURAÇÕES DO JOGO                         ║");
            console.printlnConsole("╠══════════════════════════════════════════════════════════════════════╣");
            console.printlnConsole("║                                                                      ║");
            console.printlnConsole("║  ► PERSONALIZE SUA EXPERIÊNCIA                                       ║");
            console.printlnConsole("║                                                                      ║");
            console.printlnConsole("║    Escolha como deseja aproveitar sua jornada no jogo.               ║");
            console.printlnConsole("║                                                                      ║");
            console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");

            console.printlnConsole("");

            console.printlnConsole("    • Dificuldade: " + config.dificuldade);

            String animacao;
            String cutscene;

            if (config.pularAnimacaoCombate) {
                animacao = "SIM";
            } else {
                animacao = "NÃO";
            }

            if (config.pularCutscene) {
                cutscene = "SIM";
            } else {
                cutscene = "NÃO";
            }


            console.printlnConsole("");
            console.printlnConsole("  Navegue com [ ▲ / ▼ ] e confirme com [ ENTER ]:");
            console.printlnConsole("");

            CEscolha[] opcoes = {
                    new CEscolha("Alterar dificuldade", 0),
                    new CEscolha("Pular animação de combate: " + animacao, 1),
                    new CEscolha("Pular cutscenes: " + cutscene, 2),
                    new CEscolha("Voltar e salvar", 3)
            };

            try {
                int opcao = menu.escolha(opcoes).index;

                switch (opcao) {

                    case 0 -> {
                        console.clearConsole();

                        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
                        console.printlnConsole("║                         NÍVEL DE DIFICULDADE                         ║");
                        console.printlnConsole("╠══════════════════════════════════════════════════════════════════════╣");
                        console.printlnConsole("║                                                                      ║");
                        console.printlnConsole("║  ► Escolha qual nível voce deseja jogar.                             ║");
                        console.printlnConsole("║                                                                      ║");
                        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");

                        console.printlnConsole("");
                        console.printlnConsole("  Navegue com [ ▲ / ▼ ] e confirme com [ ENTER ]:");
                        console.printlnConsole("");

                        CEscolha[] dificuldades = {
                                new CEscolha("Fácil", 0),
                                new CEscolha("Normal", 1),
                                new CEscolha("Difícil", 2)
                        };

                        int nivel = menu.escolha(dificuldades).index;

                        if (nivel == 0) {
                            config.dificuldade = "Facil";
                        } else if (nivel == 1) {
                            config.dificuldade = "Normal";
                        } else if (nivel == 2) {
                            config.dificuldade = "Dificil";
                        }
                    }

                    case 1 -> {
                        config.pularAnimacaoCombate = !config.pularAnimacaoCombate;
                    }

                    case 2 -> {
                        config.pularCutscene = !config.pularCutscene;
                    }

                    case 3 -> {
                        try {
                            ConfiguracaoXML xml = new ConfiguracaoXML();

                            xml.salvar(config);

                            console.printlnConsole("");
                            console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
                            console.printlnConsole("║                  CONFIGURAÇÕES SALVAS COM SUCESSO!                   ║");
                            console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");

                            console.esperarEnter("");

                            aberto = false;

                        } catch (IOException e) {
                            console.printlnConsole("Erro ao salvar configurações!");
                            console.esperarEnter("");
                        }
                    }
                }

            } catch (IOException e) {
                console.printlnConsole("Erro ao ler a escolha.");
                aberto = false;
            }
        }
    }
}