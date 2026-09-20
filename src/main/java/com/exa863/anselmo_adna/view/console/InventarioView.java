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
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                        INVENTÁRIO DO ATLETA                          ║");
        console.printlnConsole("╠══════════════════════════════════════════════════════════════════════╣");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("║  ► MOCHILA DE ITENS : Itens, suprimentos e recursos carregados.      ║");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");
        console.printlnConsole("");

        if (itens.isEmpty()) {
            console.printlnConsole("  [i] Sua mochila de itens está vazia no momento.\n");
            console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
            console.printlnConsole("       │              [ ENTER ]  Voltar ao Mapa                 │");
            console.printlnConsole("       └────────────────────────────────────────────────────────┘");
            console.esperarEnter("");
            sceneController.trocarCena(new MapaView(console, sceneController, gameController));
            return;
        }

        console.printlnConsole("  Selecione um item com [ ▲ / ▼ ] para ver detalhes ou usar:\n");

        List<Item> listaItens = List.copyOf(itens.keySet());
        CEscolha[] opcoes = new CEscolha[listaItens.size() + 1];

        for (int i = 0; i < listaItens.size(); i++) {
            Item item = listaItens.get(i);
            int quantidade = itens.get(item);

            String rotulo = item.getNome() + " (x" + quantidade + ")";
            if (item.isConsumivel()) {
                rotulo += " • +" + item.getCura() + " de Saúde";
            } else {
                rotulo += " • [Item Especial]";
            }

            opcoes[i] = new CEscolha(rotulo, i);
        }

        opcoes[listaItens.size()] = new CEscolha("◄ Voltar ao Mapa", -1);

        CMultiplaEscolha menu = new CMultiplaEscolha(console);

        try {
            CEscolha escolha = menu.escolha(opcoes);

            if (escolha.index == -1) {
                sceneController.trocarCena(new MapaView(console, sceneController, gameController));
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
        console.printlnConsole("  ┌────────────────────────────────────────────────────────────────────┐");
        console.printlnConsole("  │ Item: " + String.format("%-61s", item.getNome()) + "│");
        console.printlnConsole("  │ Info: " + String.format("%-61s", item.getDescricao()) + "│");
        console.printlnConsole("  └────────────────────────────────────────────────────────────────────┘");
        console.printlnConsole("");

        if (!item.isConsumivel()) {
            console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
            console.printlnConsole("       │                 [ ENTER ]  Voltar                      │");
            console.printlnConsole("       └────────────────────────────────────────────────────────┘");
            console.esperarEnter("");
            sceneController.trocarCena(this);
            return;
        }

        CEscolha[] opcoesItem = new CEscolha[] {
                new CEscolha("Consumir Item (+" + item.getCura() + " de Saúde)", 0),
                new CEscolha("Voltar ao Inventário", 1)
        };

        CMultiplaEscolha menuItem = new CMultiplaEscolha(console);
        CEscolha escolha = menuItem.escolha(opcoesItem);

        if (escolha.index == 0) {
            player.getInventario().usarItem(item, player);
            console.printlnConsole("");
            console.printlnConsole("  [✓] Você utilizou " + item.getNome() + "!");
            console.printlnConsole("  ► Saúde atual do atleta: " + player.getAtributos().getSaude() + " / 100\n");
            console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
            console.printlnConsole("       │                 [ ENTER ]  Continuar                   │");
            console.printlnConsole("       └────────────────────────────────────────────────────────┘");
            console.esperarEnter("");
        }

        sceneController.trocarCena(this);
    }
}