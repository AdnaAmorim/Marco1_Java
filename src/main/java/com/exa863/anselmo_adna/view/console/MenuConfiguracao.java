package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.exception.FalhaSalvamentoException;
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

            String autoSave;
            String animacao;
            String cutscene;

            if (config.autoSave) {
                autoSave = "SIM";
            } else {
                autoSave = "NÃO";
            }

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
                    new CEscolha("Save automatico: " + autoSave, 0),
                    new CEscolha("Pular animação de combate: " + animacao, 1),
                    new CEscolha("Pular cutscenes: " + cutscene, 2),
                    CEscolha.separador(),
                    new CEscolha("Voltar e salvar", 3)
            };

            try {
                int opcao = menu.escolha(opcoes).index;

                switch (opcao) {

                    case 0 -> {
                        config.autoSave = !config.autoSave;
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

                        } catch (FalhaSalvamentoException e) {
                            console.printlnConsole(e.getMessage());
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
