package com.exa863.anselmo_adna.view.fx;

/**
 * Lista as músicas de fundo do jogo em resources/audios.
 *
 * @author Anselmo e Adna
 */
public enum Musica {

    INTRO("intro"),
    MENU("background");

    private static final String PASTA_AUDIOS = "/audios/";
    private static final String EXTENSAO = ".mp3";

    private final String caminhoRelativo;

    Musica(String caminhoRelativo) {
        this.caminhoRelativo = caminhoRelativo;
    }

    public String getCaminho() {
        return PASTA_AUDIOS + caminhoRelativo + EXTENSAO;
    }
}
