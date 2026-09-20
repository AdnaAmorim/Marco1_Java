package com.exa863.anselmo_adna.model.stats;

public class Item {
    private final String nome;
    private final String descricao;
    private final TipoItem tipo;
    private final int preco;
    private final int cura;
    private final int energia;

    public Item(String nome, String descricao, TipoItem tipo, int preco, int cura, int energia) {
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.preco = preco;
        this.cura = cura;
        this.energia = energia;
    }

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public TipoItem getTipo() { return tipo; }
    public int getPreco() { return preco; }
    public int getCura() { return cura; }
    public int getEnergia() { return energia; }

    public boolean isConsumivel() {
        return cura > 0 || energia > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return java.util.Objects.equals(nome, item.nome);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hashCode(nome);
    }
}