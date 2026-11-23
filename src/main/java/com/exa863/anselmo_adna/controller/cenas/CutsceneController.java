package com.exa863.anselmo_adna.controller.cenas;

import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.GameState;
import com.exa863.anselmo_adna.model.narrativa.Capitulo;
import com.exa863.anselmo_adna.model.narrativa.Dialogo;
import com.exa863.anselmo_adna.model.narrativa.DialogoNarrativo;
import com.exa863.anselmo_adna.model.stats.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controla a exibição dos diálogos e escolhas de um capítulo.
 * Aplica o resultado das escolhas do jogador e marca o capítulo como concluído no final.
 *
 * @author Anselmo e Adna
 */
public class CutsceneController {

    private final Capitulo capitulo;
    private final GameState gameState;

    public CutsceneController(Capitulo capitulo, GameState gameState) {
        this.capitulo = capitulo;
        this.gameState = gameState;
    }

    public Capitulo getCapitulo() {
        return capitulo;
    }

    public Player getPlayer() {
        return gameState.getPlayer();
    }

    public GameState getGameState() {
        return gameState;
    }

    public List<Dialogo> getDialogos() {
        return capitulo.getDialogos(getPlayer());
    }

    /** Aplica a escolha e devolve um aviso para cada item cuja quantidade aumentou no inventário. */
    public List<Dialogo> processarEscolha(Dialogo dialogo, int indiceOpcao) {
        Dialogo.Opcao opcao = dialogo.getOpcoes().get(indiceOpcao);
        Map<Item, Integer> itens = getPlayer().getInventario().getItens();
        Map<Item, Integer> antes = new HashMap<>(itens);
        opcao.executarAcao(getPlayer());
        gameState.registrarInteracao(capitulo.getId(), dialogo.getTexto(), indiceOpcao, opcao.getTexto());
        List<Dialogo> avisos = new ArrayList<>();
        itens.forEach((item, quantidade) -> {
            int ganho = quantidade - antes.getOrDefault(item, 0);
            if (ganho > 0) {
                avisos.add(DialogoNarrativo.sistema("Você recebeu: " + item.getNome()
                        + (ganho > 1 ? " ×" + ganho : "") + ". Confira no inventário."));
            }
        });
        return avisos;
    }

    public void finalizarCapitulo() {
        if (capitulo != null && getPlayer() != null) {
            capitulo.finalizar(getPlayer());
        }
    }
}
