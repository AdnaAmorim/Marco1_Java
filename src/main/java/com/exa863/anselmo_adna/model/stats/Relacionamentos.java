package com.exa863.anselmo_adna.model.stats;

import com.exa863.anselmo_adna.model.character.Personagem;

/**
 * Guarda o nível de amizade entre o jogador e um personagem.
 *
 * @author Anselmo e Adna
 */
public class Relacionamentos {
    private Personagem personagem;
    private int nivelAmizade;

    public Relacionamentos(Personagem personagem, int nivelAmizade) {
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
