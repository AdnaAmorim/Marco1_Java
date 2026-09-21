package com.exa863.anselmo_adna.model.character;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes da classe base de personagens.
 *
 * @author Anselmo e Adna
 */
public class PersonagemTest {

    private Personagem personagem;

    @BeforeEach
    public void setUp() {
        personagem = new Personagem(1, "Anselmo", "Um rapaz determinado", Cores.CASTANHO, Sexo.MASCULINO);
    }

    @Test
    public void testCriacaoDePersonagem() {
        assertEquals(1, personagem.getId());
        assertEquals("Anselmo", personagem.getNome());
        assertEquals("Um rapaz determinado", personagem.getDescricao());
        assertEquals(Cores.CASTANHO, personagem.getCorOlhos());
        assertEquals(Sexo.MASCULINO, personagem.getSexo());
    }

    @Test
    public void testModificacaoDeAtributosDoPersonagem() {
        personagem.setNome("Novo Nome");
        personagem.setDescricao("Nova descricao");
        personagem.setCorOlhos(Cores.VERDE);
        personagem.setSexo(Sexo.FEMININO);

        assertEquals("Novo Nome", personagem.getNome());
        assertEquals("Nova descricao", personagem.getDescricao());
        assertEquals(Cores.VERDE, personagem.getCorOlhos());
        assertEquals(Sexo.FEMININO, personagem.getSexo());
    }
}
