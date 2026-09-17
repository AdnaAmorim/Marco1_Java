package com.exa863.anselmo_adna.model.combat;

import com.exa863.anselmo_adna.model.character.Personagem;
import com.exa863.anselmo_adna.model.stats.Atributo;

public class Lutador {
    private final Personagem personagem;
    private final EstiloLuta estiloLuta;
    private final AtributosEfetivos atributosEfetivos;

    public Lutador(Personagem personagem, Atributo atributosBase, EstiloLuta estiloLuta) {
        this.personagem = personagem;
        this.estiloLuta = estiloLuta;
        // Calcula e armazena os atributos efetivos de forma definitiva para essa luta
        this.atributosEfetivos = estiloLuta.aplicarModificadores(atributosBase);
    }

    public Personagem getPersonagem() {
        return personagem;
    }

    public EstiloLuta getEstiloLuta() {
        return estiloLuta;
    }

    public AtributosEfetivos getAtributosEfetivos() {
        return atributosEfetivos;
    }

    public String getNome() {
        return personagem.getNome();
    }
}
