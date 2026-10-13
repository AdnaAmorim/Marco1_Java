package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.world.DataDia;
import com.exa863.anselmo_adna.model.world.EstadoGame;
import com.exa863.anselmo_adna.model.world.Local;
import com.exa863.anselmo_adna.model.narrativa.Capitulo;
import com.exa863.anselmo_adna.model.narrativa.GrafoCapitulos;
import com.exa863.anselmo_adna.model.narrativa.capitulos.*;

import java.util.List;

public class GameController {

    private DataDia dataDia;
    private Local mapaGlobal;
    private Local localAtual;
    private EstadoGame estadoGame;
    private Player player;

    // Controlador que eh responsável pela gestão da história
    private NarrativaController narrativaController;

    public GameController() {
        this.dataDia = new DataDia();
        this.estadoGame = EstadoGame.INICIANDO;

        configurarNarrativa();
        configurarMundo();
    }

    private void configurarNarrativa() {
        // Carrega todos os capítulos respeitando as dependências do Grafo
        List<Capitulo> listaCapitulos = List.of(
                new Capitulo01(), new Capitulo02(), new Capitulo03(),
                new Capitulo04(), new Capitulo05(), new Capitulo06(),
                new Capitulo07(), new Capitulo08(), new Capitulo09(),
                new Capitulo10()
        );

        GrafoCapitulos grafo = new GrafoCapitulos(listaCapitulos);
        this.narrativaController = new NarrativaController(grafo);
    }

    private void configurarMundo() {

        // MAPA GLOBAL
        mapaGlobal = new Local("Mapa Global", "Mapa principal do mundo.");

        // CIDADE NATAL
        Local cidadeNatal = new Local("Cidade Natal", "A cidade onde sua história começa.");
        Local academia = new Local("Academia", "Lugar onde você pode treinar e evoluir.");
        Local casa = new Local("Casa", "Seu lugar de descanso.");
        Local academiaBoxe = new Local("Academia de Boxe", "Lugar onde acontecem grandes lutas.");
        Local clubeLuta = new Local("Clube de Luta", "Lutas de rua clandestinas e apostas rápidas.");

        // CIDADE A
        Local cidadeA = new Local("Cidade A", "Uma cidade mais pobre.");
        cidadeA.setCustoAcesso(400);
        Local loja = new Local("Loja", "Uma loja onde você pode comprar itens.");
        Local academiaProfissional = new Local("Academia Profissional", "Uma academia para lutadores profissionais.");

        // CIDADE B
        Local cidadeB = new Local("Cidade B", "Uma cidade rica e movimentada.");
        cidadeB.setCustoAcesso(1000);
        Local campeonatoMundial = new Local("Campeonato Mundial", "O maior campeonato de boxe do mundo.");

        cidadeNatal.adicionarSubLocal(academia);
        cidadeNatal.adicionarSubLocal(casa);
        cidadeNatal.adicionarSubLocal(academiaBoxe);
        cidadeNatal.adicionarSubLocal(clubeLuta);
        cidadeNatal.adicionarSubLocal(cidadeA);
        cidadeNatal.adicionarSubLocal(cidadeB);

        academia.adicionarSaida();
        casa.adicionarSaida();
        academiaBoxe.adicionarSaida();
        clubeLuta.adicionarSaida();

        cidadeA.adicionarSubLocal(loja);
        cidadeA.adicionarSubLocal(academiaProfissional);
        cidadeA.adicionarSaida();

        loja.adicionarSaida();
        academiaProfissional.adicionarSaida();

        cidadeB.adicionarSubLocal(campeonatoMundial);
        cidadeB.adicionarSaida();

        campeonatoMundial.adicionarSaida();

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

    public NarrativaController getNarrativaController() {
        return narrativaController;
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

    // Volta para o local pai.
    public void voltarLocal() {
        if (localAtual.getLocalPai() != null) {
            localAtual = localAtual.getLocalPai();
        }
    }
}
