package com.exa863.anselmo_adna.relacionamentos;

import com.exa863.anselmo_adna.personagem.Personagem;

public class Relacionamentos {
    private  Personagem personagem;
    private int nivelAmizade;

    public Relacionamentos(Personagem personagem, int nivelAmizade) {
        this.personagem = personagem;
        this.nivelAmizade = nivelAmizade;
    }

}