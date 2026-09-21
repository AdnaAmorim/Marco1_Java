package com.exa863.anselmo_adna.controller.cenas;

import com.exa863.anselmo_adna.model.character.Player;
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
    private final Player player;

    public CutsceneController(Capitulo capitulo, Player player) {
        this.capitulo = capitulo;
        this.player = player;
    }

    public Capitulo getCapitulo() {
        return capitulo;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Dialogo> getDialogos() {
        return capitulo.getDialogos(player);
    }

    public void processarEscolha(Dialogo.Opcao opcao) {
        if (opcao != null && player != null) {
            opcao.executarAcao(player);
        }
    }

    public void finalizarCapitulo() {
        if (capitulo != null && player != null) {
            capitulo.finalizar(player);
        }
    }
}
