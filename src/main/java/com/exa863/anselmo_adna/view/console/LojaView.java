package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.stats.Item;
import com.exa863.anselmo_adna.model.stats.Itens;
import com.exa863.anselmo_adna.view.View;

import java.io.IOException;

/**
 * Tela da loja de itens.
 * Permite comprar itens de recuperação de vida e energia com o dinheiro do jogador.
 *
 * @author Anselmo e Adna
 */
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
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                 CONVENIÊNCIA & SUPLEMENTOS (LOJA)                    ║");
        console.printlnConsole("╠══════════════════════════════════════════════════════════════════════╣");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("║  ► Dinheiro do Atleta : " + String.format("%-44s", player.getDinheiro() + " Reais") + "║");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");
        console.printlnConsole("");
        console.printlnConsole("  Escolha o item que deseja adquirir com [ ▲ / ▼ ] e [ ENTER ]:\n");

        Item[] itensLoja = Itens.ITENS_LOJA;
        CEscolha[] opcoes = new CEscolha[itensLoja.length + 1];

        for (int i = 0; i < itensLoja.length; i++) {
            Item item = itensLoja[i];

            String efeitos = "";
            if (item.getCura() > 0) efeitos += "+" + item.getCura() + " Saúde ";
            if (item.getEnergia() > 0) efeitos += "+" + item.getEnergia() + " Energia ";

            opcoes[i] = new CEscolha(
                    item.getNome() + " • " + item.getPreco() + " Reais (" + efeitos.trim() + ")",
                    i
            );
        }

        opcoes[itensLoja.length] = new CEscolha("◄ Sair da Loja", -1);

        CMultiplaEscolha menu = new CMultiplaEscolha(console);

        try {
            CEscolha escolha = menu.escolha(opcoes);

            if (escolha.index == -1) {
                gameController.voltarLocal();
                sceneController.trocarCena(new MapaView(console, sceneController, gameController));
                return;
            }

            comprarItem(player, itensLoja[escolha.index]);
            sceneController.trocarCena(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void comprarItem(Player player, Item item) {
        if (player.getDinheiro() < item.getPreco()) {
            console.printlnConsole("");
            console.printlnConsole("  [!] Dinheiro insuficiente para adquirir " + item.getNome() + ".");
            console.printlnConsole("      Preço: " + item.getPreco() + " Reais | Seu Dinheiro: " + player.getDinheiro() + " Reais\n");
            console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
            console.printlnConsole("       │                 [ ENTER ]  Continuar                   │");
            console.printlnConsole("       └────────────────────────────────────────────────────────┘");
            console.esperarEnter("");
            return;
        }

        player.setDinheiro(player.getDinheiro() - item.getPreco());
        player.getInventario().adicionarItem(item);

        console.printlnConsole("");
        console.printlnConsole("  [✓] Compra realizada: " + item.getNome() + " adicionado à mochila!");
        console.printlnConsole("  ► Dinheiro restante: " + player.getDinheiro() + " Reais\n");
        console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
        console.printlnConsole("       │                 [ ENTER ]  Continuar                   │");
        console.printlnConsole("       └────────────────────────────────────────────────────────┘");
        console.esperarEnter("");
    }
}