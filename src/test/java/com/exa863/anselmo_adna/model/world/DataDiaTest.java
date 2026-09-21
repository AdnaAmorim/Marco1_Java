package com.exa863.anselmo_adna.model.world;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes do relógio e calendário do jogo.
 * Verifica a passagem do tempo, virada de hora e passagem dos dias.
 *
 * @author Anselmo e Adna
 */
public class DataDiaTest {

    private DataDia data;

    @BeforeEach
    public void setUp() {
        data = new DataDia();
    }

    @Test
    public void testHorarioInicialDoJogo() {
        // O jogo deve começar no Dia 1 às 06:00
        assertEquals(1, data.getDias());
        assertEquals(6, data.getHoras());
        assertEquals(0, data.getMinutos());
        assertEquals("06:00", data.getHoraFormatada());
        assertEquals("Dia 1", data.getDiaFormatado());
    }

    @Test
    public void testAvancoDeMinutosNaMesmaHora() {
        data.avancarMinutos(30);

        assertEquals(6, data.getHoras());
        assertEquals(30, data.getMinutos());
        assertEquals("06:30", data.getHoraFormatada());
    }

    @Test
    public void testViradaDeHora() {
        // Avança 75 minutos a partir das 06:00 -> deve virar para 07:15
        data.avancarMinutos(75);

        assertEquals(7, data.getHoras());
        assertEquals(15, data.getMinutos());
        assertEquals("07:15", data.getHoraFormatada());
    }

    @Test
    public void testViradaDeDia() {
        // Das 06:00 até meia-noite (24:00) são 18 horas (1080 minutos)
        data.avancarMinutos(18 * 60);

        // Deve virar para o Dia 2 às 00:00
        assertEquals(2, data.getDias());
        assertEquals(0, data.getHoras());
        assertEquals(0, data.getMinutos());
        assertEquals("Dia 2", data.getDiaFormatado());
        assertEquals("00:00", data.getHoraFormatada());
    }

    @Test
    public void testAvancarVariosDias() {
        // Avança 48 horas (2 dias completos)
        data.avancarMinutos(48 * 60);

        assertEquals(3, data.getDias());
        assertEquals(6, data.getHoras());
        assertEquals("Dia 3", data.getDiaFormatado());
    }
}
