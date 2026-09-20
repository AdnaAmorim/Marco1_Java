package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.narrativa.Capitulo;
import com.exa863.anselmo_adna.model.narrativa.GatilhoNarrativo;
import com.exa863.anselmo_adna.model.narrativa.GrafoCapitulos;
import com.exa863.anselmo_adna.model.narrativa.TipoGatilho;

import java.util.Optional;

public class NarrativaController {

    private final GrafoCapitulos grafo;

    public NarrativaController(GrafoCapitulos grafo) {
        this.grafo = grafo;
    }

    public Optional<Capitulo> obterCapituloDisponivel(TipoGatilho tipo, String alvo, Player player) {
        if (player == null) {
            return Optional.empty();
        }

        return grafo.getTodosCapitulos().stream()
                .filter(c -> grafo.estaDisponivel(c.getId(), player.getCapitulosConcluidos()))
                .filter(c -> c.podeIniciar(player))
                .filter(c -> matchGatilho(c.getGatilho(), tipo, alvo))
                .findFirst();
    }

    private boolean matchGatilho(GatilhoNarrativo gatilho, TipoGatilho tipo, String alvo) {
        if (gatilho == null || gatilho.tipo() != tipo) {
            return false;
        }
        if (gatilho.alvo() == null || alvo == null) {
            return true;
        }
        return gatilho.alvo().equalsIgnoreCase(alvo);
    }

    public boolean isLocalBloqueadoPelaNarrativa(String nomeLocal, Player player) {
        if (player == null || nomeLocal == null) {
            return false;
        }

        // Verifica se algum capítulo ativo/pendente no grafo bloqueia este local
        return grafo.getTodosCapitulos().stream()
                .filter(c -> grafo.estaDisponivel(c.getId(), player.getCapitulosConcluidos()))
                .anyMatch(c -> c.isLocalBloqueado(nomeLocal));
    }

    public GrafoCapitulos getGrafo() {
        return grafo;
    }
}