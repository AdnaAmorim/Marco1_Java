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
        this.locais = new Local[4];

        configurarMundo();
    }

    private void configurarMundo() {

        this.locais[0] = new Local(
                "Academia",
                "Voce ficarar mais forte"
        );

        this.locais[1] = new Local(
                "casa",
                "Seu lugar de descanso"
        );

        this.locais[2] = new Local(
                "Academia de Boxe",
                "Voce farar grandes lutas ou nao"
        );

        this.locais[3] = new Local(
                "Cidade A",
                "Cidade rica"
        );

        this.locais[4] = new Local(
                "Cidade B",
                "Cidade mais pobre"
        );

        this.localAtual = locais[0];

        this.estadoGame = EstadoGame.EXPLORANDO;
    }

    public DataDia getDataDia() {
        return dataDia;
    }

    public Local getLocalAtual() {
        return localAtual;
    }

    public EstadoGame getEstadoGame() {
        return estadoGame;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}