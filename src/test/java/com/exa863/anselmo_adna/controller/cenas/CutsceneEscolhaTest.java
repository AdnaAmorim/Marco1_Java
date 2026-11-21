package com.exa863.anselmo_adna.controller.cenas;

import com.exa863.anselmo_adna.model.GameState;
import com.exa863.anselmo_adna.model.character.Cores;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.character.Sexo;
import com.exa863.anselmo_adna.model.narrativa.Dialogo;
import com.exa863.anselmo_adna.model.narrativa.capitulos.Capitulo01;
import com.exa863.anselmo_adna.model.stats.Itens;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CutsceneEscolhaTest {
    @Test
    void registraEscolhaEConcluiCapitulo() {
        var gameState = new GameState();
        Player p = new Player(1, "Maria", "Teste", Cores.VERDE, Sexo.FEMININO);
        gameState.setPlayer(p);
        var controller = new CutsceneController(new Capitulo01(), gameState);
        Dialogo escolha = controller.getDialogos().stream()
                .filter(Dialogo::isEscolha).findFirst().orElseThrow();
        assertFalse(p.getInventario().temItem(Itens.LUVA_DO_PAI));
        controller.processarEscolha(escolha, 0);
        assertTrue(p.getInventario().temItem(Itens.LUVA_DO_PAI));
        assertEquals(1, gameState.getInteracoes().size());
        controller.finalizarCapitulo();
        assertTrue(p.isCapituloConcluido(Capitulo01.ID));
    }
}
