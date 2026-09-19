package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.world.Local;
import com.exa863.anselmo_adna.view.View;

import java.io.IOException;
import java.util.List;

public class MapaView implements View {

    private final Console console;
    private final SceneController sceneController;
    private final GameController gameController;

    public MapaView(Console console, SceneController sceneController, GameController gameController) {
        this.console = console;
        this.sceneController = sceneController;
        this.gameController = gameController;
    }

    @Override
    public void render() {
        Local localAtual = gameController.getLocalAtual();

        console.clearConsole();
        console.printlnConsole("========================================");
        console.printlnConsole("           " + localAtual.getNome());
        console.printlnConsole("========================================");
        console.printlnConsole("");
        console.printlnConsole(localAtual.getDescricao());
        console.printlnConsole("");

        List<Local> subLocais = localAtual.getSubLocais();
        CEscolha[] opcoes = new CEscolha[subLocais.size()];

        for (int i = 0; i < subLocais.size(); i++) {
            Local local = subLocais.get(i);
            String rotulo = local.getNome();

            if (local.getCustoAcesso() > 0) {
                rotulo += " (Entrada: " + local.getCustoAcesso() + " reais)";
            }

            opcoes[i] = new CEscolha(rotulo, i);
        }

        CMultiplaEscolha menu = new CMultiplaEscolha(console);

        try {
            CEscolha escolha = menu.escolha(opcoes);
            Local localEscolhido = subLocais.get(escolha.index);

            if (!podeAcessar(localEscolhido)) {
                sceneController.trocarCena(new MapaView(console, sceneController, gameController));
                return;
            }

            gameController.entrarLocal(localEscolhido);

            String nomeDoLocal = localEscolhido.getNome();

            if (nomeDoLocal.equals("Casa")) {
                sceneController.trocarCena(new JogoView(console, sceneController, gameController));
            } else if (nomeDoLocal.equals("Loja")) {
                sceneController.trocarCena(new LojaView(console, sceneController, gameController));
            } else {
                sceneController.trocarCena(new MapaView(console, sceneController, gameController));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Verifica se o jogador pode pagar a entrada do local
    private boolean podeAcessar(Local local) throws IOException {
        int custo = local.getCustoAcesso();

        if (custo <= 0 || local.isLocalDeSaida() || local.isAcessoLiberado()) {
            return true;
        }

        Player player = gameController.getPlayer();
        int dinheiro = player.getDinheiro();

        if (dinheiro < custo) {
            console.printlnConsole("");
            console.printlnConsole("Você não tem dinheiro suficiente para entrar em " + local.getNome() + ".");
            console.printlnConsole("Necessário: " + custo + " reais | Você tem: " + dinheiro + " reais");
            console.esperarEnter("Pressione ENTER para voltar...");
            return false;
        }

        console.printlnConsole("");
        console.printlnConsole("Entrar em " + local.getNome() + " custa " + custo + " reais.");
        console.printlnConsole("Você tem " + dinheiro + " reais.");
        console.printlnConsole("");

        CEscolha[] opcoesPagamento = new CEscolha[] {
                new CEscolha("Pagar e entrar", 0),
                new CEscolha("Voltar", 1)
        };

        CMultiplaEscolha menuPagamento = new CMultiplaEscolha(console);
        CEscolha escolha = menuPagamento.escolha(opcoesPagamento);

        if (escolha.index != 0) {
            return false;
        }

        player.setDinheiro(dinheiro - custo);
        local.setAcessoLiberado(true);
        return true;
    }
}
