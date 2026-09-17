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

        console.printlnConsole("========================================");
        console.printlnConsole("        CRIAÇÃO DO PERSONAGEM");
        console.printlnConsole("========================================");
        console.printlnConsole("");
        console.printlnConsole("Antes de começar essa história,");
        console.printlnConsole("crie o seu personagem.");
        console.printlnConsole("");

        String nome = console.getEntrada("Digite o nome do personagem:");

        while (nome.trim().isEmpty()) {
            console.printlnConsole("");
            console.printlnConsole("O nome não pode ficar vazio.");
            nome = console.getEntrada("Digite o nome do personagem:");
        }

        Sexo sexo = escolherSexo();
        Cores corOlhos = escolherCorOlhos();

        Player player = new Player(
                1,
                nome,
                "Jovem que deseja seguir o caminho do pai no boxe.",
                corOlhos,
                sexo
        );

        gameController.setPlayer(player);

        mostrarPersonagem();

        console.printlnConsole("");
        console.printlnConsole("Pressione ENTER para continuar...");

        console.getEntrada("");

        sceneController.trocarCena(
                new HistoriaInicialView(
                        console,
                        sceneController,
                        gameController
                )
        );
    }

    private Sexo escolherSexo() {

        CMultiplaEscolha menu = new CMultiplaEscolha(console);

        CEscolha[] opcoes = new CEscolha[]{
                new CEscolha("Masculino", 0),
                new CEscolha("Feminino", 1)
        };

        try {

            console.printlnConsole("");
            console.printlnConsole("Escolha o sexo do personagem:");

            CEscolha escolha = menu.escolha(opcoes);

            if (escolha.index == 0) {
                return Sexo.MASCULINO;
            }

            return Sexo.FEMININO;

        } catch (IOException e) {

            e.printStackTrace();

            return Sexo.MASCULINO;
        }
    }

    private Cores escolherCorOlhos() {

        CMultiplaEscolha menu = new CMultiplaEscolha(console);

        CEscolha[] opcoes = new CEscolha[]{
                new CEscolha("Castanho", 0),
                new CEscolha("Preto", 1),
                new CEscolha("Verde", 2),
                new CEscolha("Azul", 3)
        };

        try {

            console.printlnConsole("");
            console.printlnConsole("Escolha a cor dos olhos:");

            CEscolha escolha = menu.escolha(opcoes);

            switch (escolha.index) {
                case 0:
                    return Cores.CASTANHO;

                case 1:
                    return Cores.PRETO;

                case 2:
                    return Cores.VERDE;

                case 3:
                    return Cores.AZUL;

                default:
                    return Cores.CASTANHO;
            }

        } catch (IOException e) {

            e.printStackTrace();

            return Cores.CASTANHO;
        }
    }

    private void mostrarPersonagem() {

        Player player = gameController.getPlayer();

        console.clearConsole();

        console.printlnConsole("========================================");
        console.printlnConsole("          PERSONAGEM CRIADO!");
        console.printlnConsole("========================================");
        console.printlnConsole("");

        console.printlnConsole("Nome: " + player.getNome());
        console.printlnConsole("Sexo: " + player.getSexo());
        console.printlnConsole("Cor dos olhos: " + player.getCorOlhos());

        console.printlnConsole("");
        console.printlnConsole("Seu personagem está pronto.");
    }
}