package com.exa863.anselmo_adna.model.combat;

import com.exa863.anselmo_adna.model.character.Cores;
import com.exa863.anselmo_adna.model.character.Personagem;
import com.exa863.anselmo_adna.model.character.Sexo;
import com.exa863.anselmo_adna.model.combat.estilos.Velocista;
import com.exa863.anselmo_adna.model.stats.Atributo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes do objeto de resultado da luta.
 *
 * @author Anselmo e Adna
 */
public class ResultadoLutaTest {

    private Lutador l1;
    private Lutador l2;

    @BeforeEach
    public void setUp() {
        Velocista estilo = new Velocista();
        l1 = new Lutador(new Personagem(1, "A", "desc", Cores.PRETO, Sexo.MASCULINO), new Atributo(), estilo);
        l2 = new Lutador(new Personagem(2, "B", "desc", Cores.AZUL, Sexo.MASCULINO), new Atributo(), estilo);
    }

    @Test
    public void testResultadoComVencedor() {
        ResultadoLuta resultado = new ResultadoLuta(l1, l2, l1, l2, TipoVitoria.NOCAUTE, 80.0, 40.0, 40.0, "Venceu por KO", 3);

        assertFalse(resultado.isEmpate());
        assertTrue(resultado.isVencedor(l1));
        assertFalse(resultado.isVencedor(l2));
        assertEquals(l1, resultado.getVencedor());
        assertEquals(l2, resultado.getPerdedor());
        assertEquals(TipoVitoria.NOCAUTE, resultado.getTipoVitoria());
        assertEquals(3, resultado.getTurnos());
        assertEquals(40.0, resultado.getSaldoFinal());
    }

    @Test
    public void testResultadoEmpate() {
        ResultadoLuta resultado = new ResultadoLuta(l1, l2, null, null, TipoVitoria.EMPATE, 50.0, 50.0, 0.0, "Empate", 10);

        assertTrue(resultado.isEmpate());
        assertNull(resultado.getVencedor());
        assertNull(resultado.getPerdedor());
        assertFalse(resultado.isVencedor(l1));
        assertFalse(resultado.isVencedor(l2));
    }
}
