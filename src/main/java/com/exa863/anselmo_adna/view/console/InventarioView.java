package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.stats.Item;
import com.exa863.anselmo_adna.view.View;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class InventarioView implements View {

    private final Console console;
    private final SceneController sceneController;
    private final GameController gameController;

    public InventarioView(Console console, SceneController sceneController, GameController gameController) {
        this.console = console;
        this.sceneController = sceneController;
        this.gameController = gameController;
    }

    @Override
    public void render() {
        Player player = gameController.getPlayer();
        Map<Item, Integer> itens = player.getInventario().getItens();

        console.clearConsole();
        console.printlnConsole("========================================");
        console.printlnConsole("               INVENTARIO");
        console.printlnConsole("========================================");
        console.printlnConsole("");
        console.printlnConsole("Dinheiro: " + player.getDinheiro() + " reais");
        console.printlnConsole("");

        if (itens.isEmpty()) {
            console.printlnConsole("Seu inventario esta vazio.");
            console.printlnConsole("");
            console.esperarEnter("Pressione ENTER para voltar...");
            sceneController.trocarCena(new JogoView(console, sceneController, gameController));
            return;
        }

        List<Item> listaItens = List.copyOf(itens.keySet());
        CEscolha[] opcoes = new CEscolha[listaItens.size() + 1];

        for (int i = 0; i < listaItens.size(); i++) {
            Item item = listaItens.get(i);
            int quantidade = itens.get(item);

            String rotulo = item.getNome() + " (x" + quantidade + ")";
            if (item.isConsumivel()) {
                rotulo += " - +" + item.getCura() + " saude";
            }

            opcoes[i] = new CEscolha(rotulo, i);
        }

        opcoes[listaItens.size()] = new CEscolha("Voltar", -1);

        CMultiplaEscolha menu = new CMultiplaEscolha(console);

        try {
            CEscolha escolha = menu.escolha(opcoes);

            if (escolha.index == -1) {
                sceneController.trocarCena(new JogoView(console, sceneController, gameController));
                return;
            }

            Item itemEscolhido = listaItens.get(escolha.index);
            detalharItem(player, itemEscolhido);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Mostra a descrição do item e, se for consumivel, da a opcao de usar
    private void detalharItem(Player player, Item item) throws IOException {
        console.printlnConsole("");
        console.printlnConsole(item.getNome() + ": " + item.getDescricao());
        console.printlnConsole("");

        if (!item.isConsumivel()) {
            console.esperarEnter("Pressione ENTER para voltar...");
            sceneController.trocarCena(this);
            return;
        }

        CEscolha[] opcoesItem = new CEscolha[] {
                new CEscolha("Usar (+" + item.getCura() + " saude)", 0),
                new CEscolha("Voltar", 1)
        };

        CMultiplaEscolha menuItem = new CMultiplaEscolha(console);
        CEscolha escolha = menuItem.escolha(opcoesItem);

        if (escolha.index == 0) {
            player.getInventario().usarItem(item, player);
            console.printlnConsole("");
            console.printlnConsole("Você usou " + item.getNome() + ". Saude atual: "
                    + player.getAtributos().getSaude());
            console.esperarEnter("Pressione ENTER para continuar...");
        }

        sceneController.trocarCena(this);
    }
}