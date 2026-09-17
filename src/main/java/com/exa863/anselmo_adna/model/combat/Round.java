package com.exa863.anselmo_adna.model.combat;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private int numero;
    private boolean ultimo;
    private List<String> eventos;

    public Round(int numero, boolean ultimo) {
        this.numero = numero;
        this.ultimo = ultimo;
        this.eventos = new ArrayList<>();
    }

    public void addEvento(String evento) {
        this.eventos.add(evento);
    }

    public int getNumero() {
        return numero;
    }

    public boolean isUltimo() {
        return ultimo;
    }

    public List<String> getEventos() {
        return eventos;
    }
}
