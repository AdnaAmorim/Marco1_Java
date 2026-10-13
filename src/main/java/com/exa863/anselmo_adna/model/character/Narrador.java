package com.exa863.anselmo_adna.model.character;

import com.exa863.anselmo_adna.model.narrative.Dialogos;

public class Narrador extends Personagem {
    private Dialogos dialogos;

    public Narrador(int id, String nome, String descricao, Cores corOlhos, Sexo sexo, Dialogos dialogos) {
        super(id, nome, descricao, corOlhos, sexo);
        this.dialogos = dialogos;
    }

    public Dialogos getDialogos() {
        return dialogos;
    }
}
