package com.exa863.anselmo_adna.game_Contoller;

import com.exa863.anselmo_adna.personagem.Cores;
import com.exa863.anselmo_adna.personagem.Sexo;
import com.exa863.anselmo_adna.atributos.Atributo;

public class Controller {
    private Data_dia dataDia;
    private Local[] locais;
    private Local localAtual;
    private Estadogame estadoGame;

    private Player player;

    public Controller() {
        this.dataDia = new Data_dia();
        this.estadoGame = Estadogame.INICIANDO;

        this.locais = new Local[10];

        configurarMundo();
    }

    private void configurarMundo() {
        Atributo atributosIniciais = new Atributo();

        this.player = new Player();

        locais[0] = new Local();

        this.localAtual = locais[0];

        this.estadoGame = Estadogame.EXPLORANDO;
    }

    public Data_dia getDataDia() { return dataDia; }
    public Local getLocalAtual() { return localAtual; }
    public Estadogame getEstadoGame() { return estadoGame; }
    public Player getPlayer() { return player; }
}