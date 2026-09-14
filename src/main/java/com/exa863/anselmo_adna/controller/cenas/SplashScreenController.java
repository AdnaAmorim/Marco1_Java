package com.exa863.anselmo_adna.controller.cenas;

public class SplashScreenController {

    public static final int LARGURA_MINIMA_PADRAO = 80;
    public static final int ALTURA_MINIMA_PADRAO = 32;

    private final int larguraMinima;
    private final int alturaMinima;

    public SplashScreenController() {
        this(LARGURA_MINIMA_PADRAO, ALTURA_MINIMA_PADRAO);
    }

    public SplashScreenController(int larguraMinima, int alturaMinima) {
        this.larguraMinima = larguraMinima;
        this.alturaMinima = alturaMinima;
    }

    public boolean validarDimensoes(int largura, int altura) {
        return largura >= larguraMinima && altura >= alturaMinima;
    }

    private boolean forcarContinuacao = false;

    public void setForcarContinuacao(boolean forcarContinuacao) {
        this.forcarContinuacao = forcarContinuacao;
    }

    public boolean isContinuacaoForcada() {
        return forcarContinuacao;
    }

    public boolean podeProsseguir(int largura, int altura) {
        return forcarContinuacao || validarDimensoes(largura, altura);
    }

    public int getLarguraMinima() {
        return larguraMinima;
    }

    public int getAlturaMinima() {
        return alturaMinima;
    }
}
