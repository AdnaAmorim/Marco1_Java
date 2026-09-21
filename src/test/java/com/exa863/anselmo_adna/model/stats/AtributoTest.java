package com.exa863.anselmo_adna.model.stats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes dos atributos dos personagens (vida, energia e habilidades).
 * Verifica se os limites mínimos e máximos são respeitados.
 *
 * @author Anselmo e Adna
 */
public class AtributoTest {

    private Atributo atributo;

    @BeforeEach
    public void setUp() {
        atributo = new Atributo();
    }

    @Test
    public void testValoresIniciaisPadrao() {
        // Valores iniciais padrão do jogo
        assertEquals(100, atributo.getSaude());
        assertEquals(100, atributo.getEnergia());
        assertEquals(1, atributo.getForca());
        assertEquals(1, atributo.getAgilidade());
        assertEquals(1, atributo.getResistencia());
        assertEquals(1, atributo.getInteligencia());
    }

    @Test
    public void testNaoUltrapassaLimiteMaximoDeHabilidade() {
        // O teto das habilidades é 10
        atributo.setForca(20);
        atributo.setAgilidade(15);
        atributo.setResistencia(99);
        atributo.setInteligencia(12);

        assertEquals(10, atributo.getForca());
        assertEquals(10, atributo.getAgilidade());
        assertEquals(10, atributo.getResistencia());
        assertEquals(10, atributo.getInteligencia());
    }

    @Test
    public void testNaoFicaComValorNegativo() {
        // Nenhuma habilidade nem vida pode ser negativa
        atributo.setForca(-5);
        atributo.setAgilidade(-1);
        atributo.setSaude(-50);
        atributo.setEnergia(-10);

        assertEquals(0, atributo.getForca());
        assertEquals(0, atributo.getAgilidade());
        assertEquals(0, atributo.getSaude());
        assertEquals(0, atributo.getEnergia());
    }

    @Test
    public void testSaudeEEnergiaNaoUltrapassamCem() {
        // Vida e energia têm limite máximo de 100
        atributo.setSaude(150);
        atributo.setEnergia(200);

        assertEquals(100, atributo.getSaude());
        assertEquals(100, atributo.getEnergia());
    }

    @Test
    public void testConstrutorComValoresCustomizados() {
        Atributo customizado = new Atributo(80, 7, 8, 6, 5, 90);

        assertEquals(80, customizado.getSaude());
        assertEquals(7, customizado.getForca());
        assertEquals(8, customizado.getAgilidade());
        assertEquals(6, customizado.getResistencia());
        assertEquals(5, customizado.getInteligencia());
        assertEquals(90, customizado.getEnergia());
    }
}
