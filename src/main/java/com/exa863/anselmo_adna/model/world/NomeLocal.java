package com.exa863.anselmo_adna.model.world;

/**
 * Nomes e identificadores dos locais visitáveis no mapa do jogo.
 *
 * @author Anselmo e Adna
 */
public enum NomeLocal {
    CIDADE_NATAL("Cidade Natal"),
    ACADEMIA("Academia"),
    CASA("Casa"),
    ACADEMIA_BOXE("Academia de Boxe"),
    CLUBE_LUTA("Clube de Luta"),
    CIDADE_A("Cidade A"),
    LOJA("Loja"),
    ACADEMIA_PROFISSIONAL("Academia Profissional"),
    CIDADE_B("Cidade B"),
    CAMPEONATO_MUNDIAL("Campeonato Mundial");

    private final String nomeFormatado;

    NomeLocal(String nomeFormatado) {
        this.nomeFormatado = nomeFormatado;
    }

    public String getNome() {
        return nomeFormatado;
    }

    public boolean corresponde(String outroNome) {
        return outroNome != null && (outroNome.equalsIgnoreCase(this.nomeFormatado) || outroNome.equalsIgnoreCase(this.name()));
    }

    @Override
    public String toString() {
        return nomeFormatado;
    }
}
