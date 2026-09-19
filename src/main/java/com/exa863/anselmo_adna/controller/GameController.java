package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.world.DataDia;
import com.exa863.anselmo_adna.model.world.EstadoGame;
import com.exa863.anselmo_adna.model.world.Local;

public class GameController {

    private DataDia dataDia;

    private Local mapaGlobal;
    private Local localAtual;

    private EstadoGame estadoGame;
    private Player player;

    public GameController() {

        this.dataDia = new DataDia();
        this.estadoGame = EstadoGame.INICIANDO;

        configurarMundo();
    }

    private void configurarMundo() {


        // MAPA GLOBAL

        mapaGlobal = new Local(
                "Mapa Global",
                "Mapa principal do mundo."
        );



        // CIDADE NATAL

        Local cidadeNatal = new Local(
                "Cidade Natal",
                "A cidade onde sua história começa."
        );


        Local academia = new Local(
                "Academia",
                "Lugar onde você pode treinar e evoluir."
        );

        Local casa = new Local(
                "Casa",
                "Seu lugar de descanso."
        );

        Local academiaBoxe = new Local(
                "Academia de Boxe",
                "Lugar onde acontecem grandes lutas."
        );



        // CIDADE A


        Local cidadeA = new Local(
                "Cidade A",
                "Uma cidade mais pobre."
        );
        cidadeA.setCustoAcesso(400);

        Local loja = new Local(
                "Loja",
                "Uma loja onde você pode comprar itens."
        );

        Local academiaProfissional = new Local(
                "Academia Profissional",
                "Uma academia para lutadores profissionais."
        );



        // CIDADE B

        Local cidadeB = new Local(
                "Cidade B",
                "Uma cidade rica e movimentada."
        );
        cidadeB.setCustoAcesso(1000);

        Local campeonatoMundial = new Local(
                "Campeonato Mundial",
                "O maior campeonato de boxe do mundo."
        );

        Local abrirAcademia = new Local(
                "Abrir uma Academia",
                "Construa sua própria academia."
        );

        cidadeNatal.adicionarSubLocal(academia);
        cidadeNatal.adicionarSubLocal(casa);
        cidadeNatal.adicionarSubLocal(academiaBoxe);
        cidadeNatal.adicionarSubLocal(cidadeA);
        cidadeNatal.adicionarSubLocal(cidadeB);
        cidadeNatal.adicionarSaida();

        academia.adicionarSaida();
        casa.adicionarSaida();
        academiaBoxe.adicionarSaida();

        cidadeA.adicionarSubLocal(loja);
        cidadeA.adicionarSubLocal(academiaProfissional);
        cidadeA.adicionarSaida();

        loja.adicionarSaida();
        academiaProfissional.adicionarSaida();

        cidadeB.adicionarSubLocal(campeonatoMundial);
        cidadeB.adicionarSubLocal(abrirAcademia);
        cidadeB.adicionarSaida();

        campeonatoMundial.adicionarSaida();
        abrirAcademia.adicionarSaida();

        mapaGlobal.adicionarSubLocal(cidadeNatal);

        localAtual = cidadeNatal;
        estadoGame = EstadoGame.EXPLORANDO;
    }

    public DataDia getDataDia() {
        return dataDia;
    }

    public Local getLocalAtual() {
        return localAtual;
    }

    public Local getMapaGlobal() {
        return mapaGlobal;
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

    public void entrarLocal(Local local) {

        if (local == null) {
            return;
        }

        if (local.isLocalDeSaida()) {

            voltarLocal();

        } else {

            localAtual = local;
        }
    }

    //Volta para o local pai.

    public void voltarLocal() {

        if (localAtual.getLocalPai() != null) {

            localAtual = localAtual.getLocalPai();
        }
    }
}
