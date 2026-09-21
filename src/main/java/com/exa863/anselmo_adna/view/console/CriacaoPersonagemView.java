package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.model.character.Cores;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.character.Sexo;
import com.exa863.anselmo_adna.view.View;

import java.io.IOException;

public class CriacaoPersonagemView implements View {

    private final Console console;
    private final SceneController sceneController;
    private final GameController gameController;

    public CriacaoPersonagemView(
            Console console,
            SceneController sceneController,
            GameController gameController
    ) {
        this.console = console;
        this.sceneController = sceneController;
        this.gameController = gameController;
    }

    @Override
    public void render() {
        console.clearConsole();

        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                       CRIAÇÃO DO PERSONAGEM                          ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");
        console.printlnConsole("");
        console.printlnConsole("  ► Antes de entrar no ringue e traçar sua jornada,");
        console.printlnConsole("    defina a identidade e as características do seu boxeador:");
        console.printlnConsole("");
        console.printlnConsole("  ┌────────────────────────────────────────────────────────────────────┐");
        console.printlnConsole("  │ Digite o nome do seu boxeador:                                     │");
        console.printlnConsole("  └────────────────────────────────────────────────────────────────────┘");

        String nome = console.getEntrada("  ► Nome:");

        while (nome.trim().isEmpty()) {
            console.printlnConsole("  [!] O nome do boxeador não pode ficar vazio.");
            nome = console.getEntrada("  ► Digite novamente:");
        }

        Sexo sexo = escolherSexo(nome.trim());
        Cores corOlhos = escolherCorOlhos(nome.trim(), sexo);

        Player player = new Player(
                1,
                nome.trim(),
                "Jovem determinado a seguir os passos do pai nos ringues de boxe.",
                corOlhos,
                sexo
        );

        if (Console.MODO_DEV) {
            player.ativarModoDev();
        }

        gameController.setPlayer(player);

        mostrarPersonagem();

        console.printlnConsole("");
        console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
        console.printlnConsole("       │          [ ENTER ]  Iniciar História Inicial           │");
        console.printlnConsole("       └────────────────────────────────────────────────────────┘");
        console.esperarEnter("");

        sceneController.trocarCena(
                new HistoriaInicialView(
                        console,
                        sceneController,
                        gameController
                )
        );
    }

    private Sexo escolherSexo(String nome) {
        console.clearConsole();
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                       CRIAÇÃO DO PERSONAGEM                          ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");
        console.printlnConsole("");
        console.printlnConsole("  ► Atleta: " + nome);
        console.printlnConsole("  ► Escolha o sexo do personagem [ ▲ / ▼ ] e tecle [ ENTER ]:\n");

        CMultiplaEscolha menu = new CMultiplaEscolha(console);
        CEscolha[] opcoes = new CEscolha[]{
                new CEscolha("Masculino", 0),
                new CEscolha("Feminino", 1)
        };

        try {
            CEscolha escolha = menu.escolha(opcoes);
            return escolha.index == 0 ? Sexo.MASCULINO : Sexo.FEMININO;
        } catch (IOException e) {
            e.printStackTrace();
            return Sexo.MASCULINO;
        }
    }

    private Cores escolherCorOlhos(String nome, Sexo sexo) {
        console.clearConsole();
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                       CRIAÇÃO DO PERSONAGEM                          ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");
        console.printlnConsole("");
        String generoTexto = sexo == Sexo.MASCULINO ? "Masculino" : "Feminino";
        console.printlnConsole("  ► Atleta: " + nome + " (" + generoTexto + ")");
        console.printlnConsole("  ► Escolha a cor dos olhos [ ▲ / ▼ ] e tecle [ ENTER ]:\n");

        CMultiplaEscolha menu = new CMultiplaEscolha(console);
        CEscolha[] opcoes = new CEscolha[]{
                new CEscolha("Castanho", 0),
                new CEscolha("Preto", 1),
                new CEscolha("Verde", 2),
                new CEscolha("Azul", 3)
        };

        try {
            CEscolha escolha = menu.escolha(opcoes);
            return switch (escolha.index) {
                case 0 -> Cores.CASTANHO;
                case 1 -> Cores.PRETO;
                case 2 -> Cores.VERDE;
                case 3 -> Cores.AZUL;
                default -> Cores.CASTANHO;
            };
        } catch (IOException e) {
            e.printStackTrace();
            return Cores.CASTANHO;
        }
    }

    private void mostrarPersonagem() {
        Player player = gameController.getPlayer();
        console.clearConsole();

        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                      FICHA OFICIAL DO BOXEADOR                       ║");
        console.printlnConsole("╠══════════════════════════════════════════════════════════════════════╣");
        console.printlnConsole("║                                                                      ║");
        printLinhaFicha("► DADOS DO ATLETA:");
        printLinhaFicha("  • Nome          : " + player.getNome());
        printLinhaFicha("  • Sexo          : " + player.getSexo());
        printLinhaFicha("  • Cor dos Olhos : " + player.getCorOlhos());
        console.printlnConsole("║                                                                      ║");
        printLinhaFicha("► ATRIBUTOS INICIAIS:");
        printLinhaFicha("  • Saúde: " + player.getAtributos().getSaude() + "/100     • Energia: " + player.getAtributos().getEnergia() + "/100");
        printLinhaFicha("  • Força: " + player.getAtributos().getForca() + "/10        • Agilidade: " + player.getAtributos().getAgilidade() + "/10");
        printLinhaFicha("  • Resistência: " + player.getAtributos().getResistencia() + "/10  • Inteligência: " + player.getAtributos().getInteligencia() + "/10");
        console.printlnConsole("║                                                                      ║");
        printLinhaFicha("► HISTÓRICO:");
        printLinhaFicha("  • Jovem determinado a honrar o legado do pai nos ringues.");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");
    }

    private void printLinhaFicha(String texto) {
        if (texto.length() > 66) {
            texto = texto.substring(0, 66);
        }
        console.printlnConsole("║  " + String.format("%-66s", texto) + "  ║");
    }
}