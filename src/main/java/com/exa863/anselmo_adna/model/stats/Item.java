package com.exa863.anselmo_adna.model.stats;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Um item do jogo com nome, descrição, tipo, preço e quanto cura de vida/energia.
 *
 * @author Anselmo e Adna
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class Item {
    private final String nome;
    private final String descricao;
    private final TipoItem tipo;
    private final int preco;
    private final int cura;
    private final int energia;

    @JsonCreator
    public Item(@JsonProperty("nome") String nome, @JsonProperty("descricao") String descricao,
                @JsonProperty("tipo") TipoItem tipo, @JsonProperty("preco") int preco,
                @JsonProperty("cura") int cura, @JsonProperty("energia") int energia) {
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
