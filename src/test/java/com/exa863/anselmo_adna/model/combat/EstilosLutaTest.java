package com.exa863.anselmo_adna.model.combat;

import com.exa863.anselmo_adna.model.combat.estilos.Contragolpeador;
import com.exa863.anselmo_adna.model.combat.estilos.Nocauteador;
import com.exa863.anselmo_adna.model.combat.estilos.Velocista;
import com.exa863.anselmo_adna.model.stats.Atributo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes dos estilos de boxe e seus modificadores de atributos.
 * Verifica bônus e penalidades específicos de cada arquétipo.
 *
 * @author Anselmo e Adna
 */
public class EstilosLutaTest {

    private Atributo base;

    @BeforeEach
    public void setUp() {
        base = new Atributo(100, 5, 5, 5, 5, 50);
    }

    @Test
    public void testModificadoresVelocista() {
        Velocista velocista = new Velocista();
        AtributosEfetivos eff = velocista.aplicarModificadores(base);

        // Força perde 20%: 5 * 0.8 = 4.0
        assertEquals(4.0, eff.forca, 0.001);
        // Energia ganha 20%: 50 * 1.2 = 60.0
        assertEquals(60.0, eff.energia, 0.001);
        // Agilidade recebe bônus do estilo
        assertTrue(eff.agilidade > 5.0, "Agilidade deve aumentar com o estilo Velocista");
    }

    @Test
    public void testModificadoresNocauteador() {
        Nocauteador nocauteador = new Nocauteador();
        AtributosEfetivos eff = nocauteador.aplicarModificadores(base);

        // Agilidade perde 20%: 5 * 0.8 = 4.0
        assertEquals(4.0, eff.agilidade, 0.001);
        // Energia perde 20%: 50 * 0.8 = 40.0
        assertEquals(40.0, eff.energia, 0.001);
        // Força recebe bônus do estilo
        assertTrue(eff.forca > 5.0, "Força deve aumentar com o estilo Nocauteador");
    }

    @Test
    public void testModificadoresContragolpeador() {
        Contragolpeador contra = new Contragolpeador();
        AtributosEfetivos eff = contra.aplicarModificadores(base);

        // Agilidade perde 15%: 5 * 0.85 = 4.25
        assertEquals(4.25, eff.agilidade, 0.001);
        // Resistência e Inteligência recebem bônus
        assertTrue(eff.resistencia > 5.0, "Resistência deve aumentar");
        assertTrue(eff.inteligencia > 5.0, "Inteligência deve aumentar");
    }

    @Test
    public void testEscalaDeBonusPorNivelDoAtributo() {
        Velocista velocista = new Velocista();

        // 2 de 10 (20%) -> nível iniciante (15% de bônus)
        assertEquals(EstiloLuta.BONUS_INICIANTE, velocista.calcularPercentualBonus(2, 10), 0.001);

        // 5 de 10 (50%) -> nível intermediário (30% de bônus)
        assertEquals(EstiloLuta.BONUS_INTERMEDIARIO, velocista.calcularPercentualBonus(5, 10), 0.001);

        // 8 de 10 (80%) -> nível avançado (45% de bônus)
        assertEquals(EstiloLuta.BONUS_AVANCADO, velocista.calcularPercentualBonus(8, 10), 0.001);

        // 8.5 de 10 (85%) -> nível mestre (60% de bônus)
        assertEquals(EstiloLuta.BONUS_MESTRE, velocista.calcularPercentualBonus(8.5, 10), 0.001);

        // 10 de 10 (100%) -> nível elite (75% de bônus)
        assertEquals(EstiloLuta.BONUS_ELITE, velocista.calcularPercentualBonus(10, 10), 0.001);
    }
}
