package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.model.character.Cores;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.character.Sexo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrologoRoteiroTest {
    @Test
    void apresentaSeisQuadrosComNomeDoPersonagem() {
        Player p = new Player(1, "Maria", "Teste", Cores.VERDE, Sexo.FEMININO);
        var quadros = PrologoRoteiro.criar(p);
        assertEquals(6, quadros.size());
        assertEquals("ANOS ATRÁS", quadros.get(0).titulo());
        assertTrue(quadros.get(0).texto().contains("Maria"));
        assertTrue(quadros.get(4).texto().contains("Ela decidiu"));
    }

    @Test
    void ajustaPronomeMasculino() {
        Player p = new Player(1, "João", "Teste", Cores.PRETO, Sexo.MASCULINO);
        assertTrue(PrologoRoteiro.criar(p).get(4).texto().contains("Ele decidiu"));
    }
}
