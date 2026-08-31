package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.world.DataDia;
import com.exa863.anselmo_adna.model.world.EstadoGame;
import com.exa863.anselmo_adna.model.world.Local;

public class GameController {
    private DataDia dataDia;
    private Local[] locais;
    private Local localAtual;
    private EstadoGame estadoGame;
    private Player player;

    public GameController() {
        this.dataDia = new DataDia();
        this.estadoGame = EstadoGame.INICIANDO;
        this.locais = new Local[10];

        configurarMundo();
    }

    private void configurarMundo() {
        this.player = new Player();
        locais[0] = new Local("Academia de Boxe", "Onde os campeões são moldados.");
        this.localAtual = locais[0];
        this.estadoGame = EstadoGame.EXPLORANDO;
    }

    public DataDia getDataDia() { return dataDia; }
    public Local getLocalAtual() { return localAtual; }
    public EstadoGame getEstadoGame() { return estadoGame; }
    public Player getPlayer() { return player; }
}
