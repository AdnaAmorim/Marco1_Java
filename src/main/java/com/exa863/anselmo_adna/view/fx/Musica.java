package com.exa863.anselmo_adna.view.fx;

/**
 * Lista as músicas de fundo do jogo em resources/audios.
 *
 * @author Anselmo e Adna
 */
public enum Musica {

    INTRO("intro"),
    MENU("background"),
    CUTSCENE("cutscene");

    /** Pasta audios. */
    private static final String PASTA_AUDIOS = "/audios/";
    /** Extensão. */
    private static final String EXTENSAO = ".mp3";

    /** Caminho relativo. */
    private final String caminhoRelativo;

    Musica(String caminhoRelativo) {
        this.caminhoRelativo = caminhoRelativo;
    }

    /** Retorna caminho do recurso no projeto. */
    public String getCaminho() {
        return PASTA_AUDIOS + caminhoRelativo + EXTENSAO;
    }
}
