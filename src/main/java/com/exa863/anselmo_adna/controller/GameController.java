package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.world.DataDia;
import com.exa863.anselmo_adna.model.world.EstadoGame;
import com.exa863.anselmo_adna.model.world.Local;
import com.exa863.anselmo_adna.controller.cenas.SplashScreenController;
import com.exa863.anselmo_adna.view.console.Console;
import com.exa863.anselmo_adna.view.console.SplashScreenView;

public class GameController {

    private DataDia dataDia;
    private Local[] locais;
    private Local localAtual;
    private EstadoGame estadoGame;
    private Player player;
    private Console console;
    private SceneController sceneController;

    public GameController(Console console, SceneController sceneController) {
        this.console = console;
        this.sceneController = sceneController;
        this.dataDia = new DataDia();
        this.estadoGame = EstadoGame.INICIANDO;
        this.locais = new Local[5];

        configurarMundo();
    }

    public void startGame() {
        SplashScreenController splashController = new SplashScreenController();
        sceneController.trocarCena(new SplashScreenView(console, sceneController, this, splashController));
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