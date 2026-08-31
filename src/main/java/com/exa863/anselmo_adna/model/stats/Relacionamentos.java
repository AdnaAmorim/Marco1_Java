package com.exa863.anselmo_adna.model.stats;

import com.exa863.anselmo_adna.model.character.Personagem;

public class Relacionamentos {
    private Personagem personagem;
    private int nivelAmizade;

    public Relacionamentos(Personagem personagem, int nivelAmizade) {
        this.personagem = personagem;
        this.nivelAmizade = nivelAmizade;
    }

    public Personagem getPersonagem() { return personagem; }
    public int getNivelAmizade() { return nivelAmizade; }
    public void setNivelAmizade(int nivelAmizade) { this.nivelAmizade = nivelAmizade; }
}
