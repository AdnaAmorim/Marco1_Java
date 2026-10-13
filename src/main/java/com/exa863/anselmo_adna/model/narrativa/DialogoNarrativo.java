package com.exa863.anselmo_adna.model.narrativa;

import com.exa863.anselmo_adna.model.character.Personagem;

public class DialogoNarrativo extends Dialogo {

    public DialogoNarrativo(Personagem personagem, String texto) {
        super(personagem, texto);
    }

    public DialogoNarrativo(TipoEmissor tipoEmissor, String texto) {
        super(tipoEmissor, texto);
    }

    public static DialogoNarrativo narrador(String texto) {
        return new DialogoNarrativo(TipoEmissor.NARRADOR, texto);
    }

    public static DialogoNarrativo sistema(String texto) {
        return new DialogoNarrativo(TipoEmissor.SISTEMA, texto);
    }
}
