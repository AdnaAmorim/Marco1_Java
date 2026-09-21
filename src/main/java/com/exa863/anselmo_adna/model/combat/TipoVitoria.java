package com.exa863.anselmo_adna.model.combat;

/**
 * Como a luta terminou: nocaute, decisão unânime, decisão dividida ou empate.
 *
 * @author Anselmo e Adna
 */
public enum TipoVitoria {
    // diferenca de pontos gigante
    NOCAUTE("Nocaute (KO)"),

    // um lutador foi bem melhor na luta e venceu com folga
    DECISAO_UNANIME("Decisão Unânime dos Juízes"),

    // luta bem apertada e parelha, venceu por pouca diferenca
    DECISAO_DIVIDIDA("Decisão Dividida dos Juízes"),

    // diferenca menor que a zona mortal, entao da empate
    EMPATE("Empate");

    private final String descricao;

    TipoVitoria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
