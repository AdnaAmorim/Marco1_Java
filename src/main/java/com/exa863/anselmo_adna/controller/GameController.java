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
        this.locais = new Local[10];

        configurarMundo();
    }

    public void startGame() {
        SplashScreenController splashController = new SplashScreenController();
        sceneController.trocarCena(new SplashScreenView(console, sceneController, this, splashController));
    }

    private void configurarMundo() {
        this.player = new Player();
        this.locais[0] = new Local("Academia de Boxe", "Onde os campeões são moldados.");
        this.localAtual = locais[0];
        this.estadoGame = EstadoGame.EXPLORANDO;
    }

    public DataDia getDataDia() { return dataDia; }
    public Local getLocalAtual() { return localAtual; }
    public EstadoGame getEstadoGame() { return estadoGame; }
    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
}
