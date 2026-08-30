package com.exa863.anselmo_adna.narrador;

import com.exa863.anselmo_adna.personagem.Cores;
import com.exa863.anselmo_adna.personagem.Personagem;
import com.exa863.anselmo_adna.personagem.Sexo;

public class Narrador extends Personagem {
    private Dialogos dialogos;

    public Narrador(int id, String nome, String descricao, Cores corOlhos, Sexo sexo, Dialogos dialogos) {
        super(id, nome, descricao, corOlhos, sexo);
        this.dialogos = dialogos;
    }
}