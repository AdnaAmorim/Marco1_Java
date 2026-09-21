package com.exa863.anselmo_adna.model.world;

/**
 * Ações gerais disponíveis no menu de exploração dos locais.
 *
 * @author Anselmo e Adna
 */
public enum AcaoMenu {
    ABRIR_MOCHILA("Abrir Mochila (Inventário)", "Acessa os itens, equipamentos e consumíveis."),
    ABRIR_ACADEMIA("Abrir uma Academia", "Construa sua própria academia de boxe profissional."),
    SAIR_LOCAL("Sair daqui", "Retorna para o mapa da região anterior.");

    private final String titulo;
    private final String descricao;

    AcaoMenu(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean corresponde(String texto) {
        return texto != null && (texto.equalsIgnoreCase(this.titulo) || texto.equalsIgnoreCase(this.name()));
    }

    @Override
    public String toString() {
        return titulo;
    }
}
