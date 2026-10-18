package com.exa863.anselmo_adna.model.stats;

import com.exa863.anselmo_adna.model.character.Personagem;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Guarda o nível de amizade entre o jogador e um personagem.
 *
 * @author Anselmo e Adna
 */
public class Relacionamentos {
    private Personagem personagem;
    private int nivelAmizade;

    @JsonCreator
    public Relacionamentos(@JsonProperty("personagem") Personagem personagem,
                           @JsonProperty("nivelAmizade") int nivelAmizade) {
        this.personagem = personagem;
        this.nivelAmizade = nivelAmizade;
    }

    public Personagem getPersonagem() {
        return personagem;
    }
    public int getNivelAmizade() {
        return nivelAmizade;
    }
    public void setNivelAmizade(int nivelAmizade) {
        this.nivelAmizade = nivelAmizade;
    }
}
