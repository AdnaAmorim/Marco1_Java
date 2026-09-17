package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.model.combat.Lutador;
import com.exa863.anselmo_adna.model.combat.Match;
import com.exa863.anselmo_adna.view.console.Console;
import com.exa863.anselmo_adna.view.console.MatchView;

public class MatchSceneController {
    
    private final MatchController matchController;
    private final SceneController sceneController;
    private final GameController gameController;

    public MatchSceneController(SceneController sceneController, GameController gameController) {
        this.matchController = new MatchController();
        this.sceneController = sceneController;
        this.gameController = gameController;
    }

    public void iniciarLuta(Console console, Lutador lutador1, Lutador lutador2) {
        Match match = matchController.executarPartida(lutador1, lutador2);
        MatchView view = new MatchView(console, sceneController, gameController, match, lutador1, lutador2);
        view.render();
    }
}
