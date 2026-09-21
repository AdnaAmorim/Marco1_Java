package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.model.combat.Lutador;
import com.exa863.anselmo_adna.model.combat.ResultadoLuta;
import com.exa863.anselmo_adna.model.combat.Match;
import com.exa863.anselmo_adna.model.combat.Round;

/**
 * Junta a matemática do combate com a narração dos rounds.
 * Roda a luta e monta a partida com os textos de cada round.
 * 
 * @author Anselmo e Adna
 */
public class MatchController {
    private FightController motorDeLuta;
    private MatchNarratorController narrador;

    public MatchController() {
        this.motorDeLuta = new FightController();
        this.narrador = new MatchNarratorController();
    }

    /**
     * Roda a luta entre dois lutadores e gera a partida completa com a narração dos rounds.
     *
     * @param lutador1 Primeiro lutador.
     * @param lutador2 Segundo lutador.
     * @return Partida montada com todos os rounds narrados.
     */
    public Match executarPartida(Lutador lutador1, Lutador lutador2) {
        ResultadoLuta resultado = motorDeLuta.resolverCombate(lutador1, lutador2);

        Match match = new Match(resultado);
        int totalRounds = resultado.getTurnos();

        for (int i = 0; i < totalRounds; i++) {
            match.addRound(new Round(i + 1, false));
        }
        match.addRound(new Round(totalRounds + 1, true));

        narrador.adicionarNarracao(match);

        return match;
    }
}