package com.exa863.anselmo_adna.model.stats;

public class Item {
    private final String nome;
    private final String descricao;
    private final TipoItem tipo;
    private final int preco;
    private final int cura;

    public Item(String nome, String descricao, TipoItem tipo, int preco, int cura) {
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.preco = preco;
        this.cura = cura;
    }
    public String getNome() {
        return nome;
    }
    public String getDescricao() {
        return descricao;
    }
    public TipoItem getTipo() {
        return tipo;
    }
    public int getPreco() {
        return preco;
    }
    public int getCura() {
        return cura;
    }
    public boolean isConsumivel() {
        return cura > 0;
    }

}
