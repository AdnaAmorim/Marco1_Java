package com.exa863.anselmo_adna.controller.cenas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SplashScreenControllerTest {

    @Test
    @DisplayName("Deve validar corretamente as dimensões mínimas padrão (80x32)")
    void testValidarDimensoesPadrao() {
        SplashScreenController controller = new SplashScreenController();

        assertEquals(80, controller.getLarguraMinima());
        assertEquals(32, controller.getAlturaMinima());

        // Tamanho exato
        assertTrue(controller.validarDimensoes(80, 32));

        // Tamanho maior (válido)
        assertTrue(controller.validarDimensoes(120, 40));
        assertTrue(controller.validarDimensoes(81, 32));
        assertTrue(controller.validarDimensoes(80, 33));

        // Largura insuficiente
        assertFalse(controller.validarDimensoes(79, 32));

        // Altura insuficiente
        assertFalse(controller.validarDimensoes(80, 31));

        // Ambos insuficientes
        assertFalse(controller.validarDimensoes(60, 20));
    }

    @Test
    @DisplayName("Deve validar corretamente quando dimensões customizadas forem informadas")
    void testValidarDimensoesCustomizadas() {
        SplashScreenController controller = new SplashScreenController(100, 50);

        assertEquals(100, controller.getLarguraMinima());
        assertEquals(50, controller.getAlturaMinima());

        assertTrue(controller.validarDimensoes(100, 50));
        assertFalse(controller.validarDimensoes(99, 50));
        assertFalse(controller.validarDimensoes(100, 49));
    }

    @Test
    @DisplayName("Deve permitir prosseguir quando continuação forçada for ativada mesmo com tela pequena")
    void testForcarContinuacao() {
        SplashScreenController controller = new SplashScreenController();

        assertFalse(controller.podeProsseguir(40, 10));
        assertFalse(controller.isContinuacaoForcada());

        controller.setForcarContinuacao(true);

        assertTrue(controller.isContinuacaoForcada());
        assertTrue(controller.podeProsseguir(40, 10));
    }
}
