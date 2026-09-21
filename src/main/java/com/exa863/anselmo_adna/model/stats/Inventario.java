package com.exa863.anselmo_adna.model.stats;

import com.exa863.anselmo_adna.model.character.Player;

import java.util.LinkedHashMap;
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