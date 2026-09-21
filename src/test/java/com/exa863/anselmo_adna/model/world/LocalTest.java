package com.exa863.anselmo_adna.model.world;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes da estrutura de locais e sublocais navegáveis do jogo.
 *
 * @author Anselmo e Adna
 */
public class LocalTest {

    private Local local;

    @BeforeEach
    public void setUp() {
        local = new Local("Cidade Natal", "Cidade onde a jornada começa.");
    }

    @Test
    public void testCriacaoLocalBase() {
        assertEquals("Cidade Natal", local.getNome());
        assertEquals("Cidade onde a jornada começa.", local.getDescricao());
        assertNull(local.getLocalPai());
        assertTrue(local.getSubLocais().isEmpty());
        assertFalse(local.isLocalDeSaida());
        assertEquals(0, local.getCustoAcesso());
    }

    @Test
    public void testAdicionarSubLocalVinculaLocalPai() {
        Local academia = new Local("Academia", "Lugar de treino");

        local.adicionarSubLocal(academia);

        assertEquals(1, local.getSubLocais().size());
        assertEquals(academia, local.getSubLocais().get(0));
        assertEquals(local, academia.getLocalPai());
    }

    @Test
    public void testAdicionarSaida() {
        local.adicionarSaida();

        assertEquals(1, local.getSubLocais().size());
        Local saida = local.getSubLocais().get(0);
        assertTrue(saida.isLocalDeSaida());
        assertEquals("Sair daqui", saida.getNome());
        assertEquals(local, saida.getLocalPai());
    }

    @Test
    public void testControleDeAcessoECusto() {
        assertFalse(local.isAcessoLiberado());
        local.setCustoAcesso(50);
        assertEquals(50, local.getCustoAcesso());

        local.setAcessoLiberado(true);
        assertTrue(local.isAcessoLiberado());
    }
}
