package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.GameState;
import com.exa863.anselmo_adna.model.world.DataDia;
import com.exa863.anselmo_adna.model.world.EstadoGame;
import com.exa863.anselmo_adna.model.world.Local;
import com.exa863.anselmo_adna.model.narrativa.Capitulo;
import com.exa863.anselmo_adna.model.narrativa.GrafoCapitulos;
import com.exa863.anselmo_adna.model.narrativa.capitulos.*;

import java.util.List;
import java.util.Objects;

/**
 * Controlador principal do jogo. Opera sobre o GameState da partida ativa
 * e reconstrói os controladores de regras ao carregar uma partida.
 *
 * @author Anselmo e Adna
 */
public class GameController {

    private GameState gameState;
    private NarrativaController narrativaController;

    public GameController() {
        this(new GameState());
    }

    public GameController(GameState gameState) {
        carregarGameState(gameState);
        configurarNarrativa();
    }

    public GameState getGameState() {
        return gameState;
    }

    /** Substitui a partida ativa pelos dados carregados, sem reiniciar relógio ou mapa. */
    public void carregarGameState(GameState gameState) {
        this.gameState = Objects.requireNonNull(gameState, "O estado da partida é obrigatório.");
    }

    private void configurarNarrativa() {
        List<Capitulo> listaCapitulos = List.of(
                new Capitulo01(), new Capitulo02(), new Capitulo03(),
                new Capitulo04(), new Capitulo05(), new Capitulo06(),
                new Capitulo07(), new Capitulo08(), new Capitulo09(),
                new Capitulo10()
        );

        GrafoCapitulos grafo = new GrafoCapitulos(listaCapitulos);
        this.narrativaController = new NarrativaController(grafo);
    }

    public DataDia getDataDia() {
        return gameState.getDataDia();
    }

    public Local getLocalAtual() {
        return gameState.getLocalAtual();
    }

    public Local getMapaGlobal() {
        return gameState.getMapaGlobal();
    }

    public EstadoGame getEstadoGame() {
        return gameState.getEstadoGame();
    }

    public Player getPlayer() {
        return gameState.getPlayer();
    }

    public void setPlayer(Player player) {
        gameState.setPlayer(player);
    }

    public NarrativaController getNarrativaController() {
        return narrativaController;
    }

    /**
     * Muda o jogador para o local escolhido, ou volta um nível se for uma saída.
     *
     * @param local Local para onde o jogador quer ir.
     */
    public void entrarLocal(Local local) {
        if (local == null) {
            return;
        }

        if (local.isLocalDeSaida()) {
            voltarLocal();
        } else {
            gameState.setLocalAtual(local);
        }
    }

    /**
     * Volta para o local anterior no mapa (local pai).
     */
    public void voltarLocal() {
        Local localAtual = gameState.getLocalAtual();
        if (localAtual.getLocalPai() != null) {
            gameState.setLocalAtual(localAtual.getLocalPai());
        }
    }
}
