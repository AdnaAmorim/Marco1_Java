package com.exa863.anselmo_adna.controller.cenas;

import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.GameState;
import com.exa863.anselmo_adna.model.narrativa.Capitulo;
import com.exa863.anselmo_adna.model.narrativa.Dialogo;

import java.util.List;

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

    public void processarEscolha(Dialogo dialogo, int indiceOpcao) {
        Dialogo.Opcao opcao = dialogo.getOpcoes().get(indiceOpcao);
        opcao.executarAcao(getPlayer());
        gameState.registrarInteracao(capitulo.getId(), dialogo.getTexto(), indiceOpcao, opcao.getTexto());
    }

    public void finalizarCapitulo() {
        if (capitulo != null && getPlayer() != null) {
            capitulo.finalizar(getPlayer());
        }
    }
}
