package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.character.Sexo;
import com.exa863.anselmo_adna.view.View;

public class HistoriaInicialView implements View {

    private final Console console;
    private final SceneController sceneController;
    private final GameController gameController;

    public HistoriaInicialView(
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

        Player player = gameController.getPlayer();

        cena1(player);
        cena2(player);
        cena3(player);
        cena4(player);
        cena5(player);
        cena6(player);

        sceneController.trocarCena(
                new JogoView(
                        console,
                        sceneController,
                        gameController
                )
        );
    }

    private void esperar() {
        console.printlnConsole("");
        console.printlnConsole("Pressione ENTER para continuar...");
        console.getEntrada("");
        console.clearConsole();
    }


    private void imprimirDigitando(String texto) {
        for (char c : texto.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(20); // Velocidade: 80 milissegundos por letra
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }

    private void cena1(Player player) {

        console.clearConsole();

        console.printlnConsole("========================================");
        console.printlnConsole("              ANOS ATRÁS");
        console.printlnConsole("========================================");
        console.printlnConsole("");

        imprimirDigitando("Tudo começou quando " + player.getNome());
        imprimirDigitando("ainda era apenas uma criança.");
        console.printlnConsole("");

        imprimirDigitando("Naquela época, seus pais eram");
        imprimirDigitando("as pessoas mais importantes de sua vida.");

        esperar();
    }

    private void cena2(Player player) {

        console.printlnConsole("========================================");
        console.printlnConsole("             O SONHO DO PAI");
        console.printlnConsole("========================================");
        console.printlnConsole("");

        imprimirDigitando("Seu pai tinha um grande sonho.");
        console.printlnConsole("");

        imprimirDigitando("Ele queria se tornar um dos maiores");
        imprimirDigitando("lutadores de boxe de sua geração.");
        console.printlnConsole("");

        imprimirDigitando("Todos os dias ele treinava.");
        imprimirDigitando("Sempre dando o seu melhor.");
        console.printlnConsole("");

        imprimirDigitando("Mas a vida tinha outros planos...");

        esperar();
    }

    private void cena3(Player player) {

        console.printlnConsole("========================================");
        console.printlnConsole("              O ACIDENTE");
        console.printlnConsole("========================================");
        console.printlnConsole("");

        imprimirDigitando("Certa noite, enquanto voltavam para casa,");
        imprimirDigitando("um acidente mudou tudo.");

        console.printlnConsole("");

        imprimirDigitando("Os pais de " + player.getNome());
        imprimirDigitando("não sobreviveram.");

        console.printlnConsole("");

        imprimirDigitando("A vida daquela criança nunca mais");
        imprimirDigitando("seria a mesma.");

        esperar();
    }

    private void cena4(Player player) {

        console.printlnConsole("========================================");
        console.printlnConsole("             UMA NOVA CASA");
        console.printlnConsole("========================================");
        console.printlnConsole("");

        imprimirDigitando(
                player.getNome()
                        + " passou a morar com os avós maternos."
        );

        console.printlnConsole("");

        imprimirDigitando("Os avós fizeram tudo o que podiam");
        imprimirDigitando("para dar uma vida boa à criança.");

        console.printlnConsole("");

        imprimirDigitando("O tempo passou.");

        esperar();
    }

    private void cena5(Player player) {

        console.printlnConsole("========================================");
        console.printlnConsole("              18 ANOS DEPOIS");
        console.printlnConsole("========================================");
        console.printlnConsole("");

        imprimirDigitando("Agora, aos 18 anos, " + player.getNome());
        imprimirDigitando("já não é mais uma criança.");

        console.printlnConsole("");

        imprimirDigitando("Mas nunca esqueceu o sonho do pai.");

        console.printlnConsole("");

        if (player.getSexo() == Sexo.MASCULINO) {

            imprimirDigitando(
                    "Ele decidiu seguir o mesmo caminho."
            );

        } else {

            imprimirDigitando(
                    "Ela decidiu seguir o mesmo caminho."
            );
        }

        esperar();
    }

    private void cena6(Player player) {

        console.printlnConsole("========================================");
        console.printlnConsole("            O INÍCIO DO SONHO");
        console.printlnConsole("========================================");
        console.printlnConsole("");

        imprimirDigitando("Seu objetivo é simples:");

        console.printlnConsole("");

        imprimirDigitando("Tornar-se um grande lutador de boxe.");

        console.printlnConsole("");

        imprimirDigitando("Mas para chegar ao topo,");
        imprimirDigitando("você terá que treinar,");
        imprimirDigitando("fazer escolhas");
        imprimirDigitando("e enfrentar muitos desafios.");

        console.printlnConsole("");

        imprimirDigitando("O sonho do seu pai agora");
        imprimirDigitando("também é o seu.");

        esperar();
    }
}