package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.stats.Item;
import com.exa863.anselmo_adna.model.stats.Itens;
import com.exa863.anselmo_adna.view.View;

import java.io.IOException;

public class LojaView implements View {

    private final Console console;
    private final SceneController sceneController;
    private final GameController gameController;

    public LojaView(Console console, SceneController sceneController, GameController gameController) {
        this.console = console;
        this.sceneController = sceneController;
        this.gameController = gameController;
    }

    @Override
    public void render() {
        Player player = gameController.getPlayer();

        console.clearConsole();
        console.printlnConsole("========================================");
        console.printlnConsole("                 LOJA");
        console.printlnConsole("========================================");
        console.printlnConsole("");
        console.printlnConsole("Seu dinheiro: " + player.getDinheiro() + " reais");
        console.printlnConsole("");

        Item[] itensLoja = Itens.ITENS_LOJA;
        CEscolha[] opcoes = new CEscolha[itensLoja.length + 1];

        for (int i = 0; i < itensLoja.length; i++) {
            Item item = itensLoja[i];
            opcoes[i] = new CEscolha(
                    item.getNome() + " - " + item.getPreco() + " reais (+" + item.getCura() + " saúde)",
                    i
            );
        }

        opcoes[itensLoja.length] = new CEscolha("Sair da loja", -1);

        CMultiplaEscolha menu = new CMultiplaEscolha(console);

        try {
            CEscolha escolha = menu.escolha(opcoes);

            if (escolha.index == -1) {
                sceneController.trocarCena(new MapaView(console, sceneController, gameController));
                return;
            }

            comprarItem(player, itensLoja[escolha.index]);

            // volta pra loja pra poder comprar mais coisas
            sceneController.trocarCena(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void comprarItem(Player player, Item item) {
        if (player.getDinheiro() < item.getPreco()) {
            console.printlnConsole("");
            console.printlnConsole("Dinheiro insuficiente para comprar " + item.getNome() + ".");
            console.esperarEnter("Pressione ENTER para continuar...");
            return;
        }

        player.setDinheiro(player.getDinheiro() - item.getPreco());
        player.getInventario().adicionarItem(item);

        console.printlnConsole("");
        console.printlnConsole("Você comprou: " + item.getNome() + "!");
        console.esperarEnter("Pressione ENTER para continuar...");
    }
}