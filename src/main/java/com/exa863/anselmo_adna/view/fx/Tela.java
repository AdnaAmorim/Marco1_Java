package com.exa863.anselmo_adna.view.fx;

/**
 * Lista as telas do jogo e o caminho dos seus arquivos FXML.
 *
 * @author Anselmo e Adna
 */
public enum Tela {

    SPLASH_SCREEN("SplashScreen"),
    MENU_INICIAL("MenuInicial", Musica.MENU),
    CRIACAO_PERSONAGEM("CriacaoPersonagem", Musica.MENU),
    CUTSCENE("Cutscene", Musica.CUTSCENE),
    MAPA("Mapa", Musica.MENU),
    CASA("Casa", Musica.MENU),
    ACADEMIA("Academia", Musica.MENU),
    ACADEMIA_BOXE("AcademiaBoxe", Musica.MENU),
    CLUBE_DE_LUTA("ClubeDeLuta", Musica.MENU),
    LOJA("Loja", Musica.MENU),
    ACADEMIA_VENDA("AcademiaVenda", Musica.MENU),
    LUTA("Luta", Musica.CUTSCENE),
    INVENTARIO("Inventario"),
    CONQUISTAS("Conquistas"),
    SAVES("Saves"),
    TUTORIAL("Tutorial"),
    CONFIGURACOES("Configuracoes"),
    CREDITOS("Creditos"),
    CONFIRMACAO("modais/Confirmacao"),
    MENU_PAUSA("modais/MenuPausa");

    private static final String PASTA_VIEWS = "/views/";
    private static final String EXTENSAO = ".fxml";

    private final String caminhoRelativo;
    private final Musica musica;

    Tela(String caminhoRelativo) {
        this(caminhoRelativo, null);
    }

    Tela(String caminhoRelativo, Musica musica) {
        this.caminhoRelativo = caminhoRelativo;
        this.musica = musica;
    }

    public String getCaminhoFxml() {
        return PASTA_VIEWS + caminhoRelativo + EXTENSAO;
    }

    public Musica getMusica() {
        return musica;
    }
}
