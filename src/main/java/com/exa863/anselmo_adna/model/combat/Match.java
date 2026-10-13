package com.exa863.anselmo_adna.model.combat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Match implements Iterable<Round> {
    private ResultadoLuta resultadoLuta;
    private List<Round> rounds;

    public Match(ResultadoLuta resultadoLuta) {
        this.resultadoLuta = resultadoLuta;
        this.rounds = new ArrayList<>();
    }

    public void addRound(Round round) {
        this.rounds.add(round);
    }

    public ResultadoLuta getResultadoLuta() {
        return resultadoLuta;
    }

    @Override
    public Iterator<Round> iterator() {
        return rounds.iterator();
    }
}
