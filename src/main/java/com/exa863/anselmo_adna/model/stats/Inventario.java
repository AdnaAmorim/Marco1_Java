package com.exa863.anselmo_adna.model.stats;

import com.exa863.anselmo_adna.model.character.Player;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Mochila de itens do jogador.
 * Controla a quantidade de cada item e o uso para recuperar vida e energia.
 *
 * @author Anselmo e Adna
 */
public class Inventario {

    private final Map<Item, Integer> itens;

    public Inventario() {
        this.itens = new LinkedHashMap<>();
    }

    @JsonCreator
    public Inventario(@JsonProperty("entradas") List<Entrada> entradas) {
        this();
        if (entradas != null) {
            for (Entrada entrada : entradas) {
                if (entrada.item() == null || entrada.quantidade() <= 0) {
                    throw new IllegalArgumentException("Item ou quantidade inválida no inventário salvo.");
                }
                adicionarItem(entrada.item(), entrada.quantidade());
            }
        }
    }

    // XML precisa preservar o objeto Item e sua quantidade, não uma chave de Map convertida em texto.
    @JsonProperty("entradas")
    public List<Entrada> getEntradasSalvas() {
        return itens.entrySet().stream()
                .map(entrada -> new Entrada(entrada.getKey(), entrada.getValue()))
                .toList();
    }

    public record Entrada(Item item, int quantidade) {
    }

    public void adicionarItem(Item item) {
        adicionarItem(item, 1);
    }

    public void adicionarItem(Item item, int quantidade) {
        itens.merge(item, quantidade, Integer::sum);
    }

    public boolean temItem(Item item) {
        return getQuantidade(item) > 0;
    }

    public int getQuantidade(Item item) {
        return itens.getOrDefault(item, 0);
    }

    public boolean removerItem(Item item) {
        int atual = getQuantidade(item);

        if (atual <= 0) {
            return false;
        }

        if (atual == 1) {
            itens.remove(item);
        } else {
            itens.put(item, atual - 1);
        }

        return true;
    }

    @JsonIgnore
    public Map<Item, Integer> getItens() {
        return itens;
    }

    /**
     * Usa um item da mochila para recuperar vida ou energia do jogador.
     *
     * @param item Item a ser usado.
     * @param player Jogador que vai receber o efeito.
     * @return true se o jogador tinha o item e conseguiu usar.
     */
    public boolean usarItem(Item item, Player player) {
        if (!removerItem(item)) {
            return false;
        }

        int saudeAtual = player.getAtributos().getSaude();
        player.getAtributos().setSaude(saudeAtual + item.getCura());

        int energiaAtual = player.getAtributos().getEnergia();
        player.getAtributos().setEnergia(energiaAtual + item.getEnergia());

        return true;
    }
}
