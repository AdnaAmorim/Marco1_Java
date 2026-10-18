package com.exa863.anselmo_adna.model;

import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.narrativa.InteracaoNarrativa;
import com.exa863.anselmo_adna.model.world.DataDia;
import com.exa863.anselmo_adna.model.world.EstadoGame;
import com.exa863.anselmo_adna.model.world.Local;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Raiz dos dados de uma partida. O mesmo estado usado pelo jogo é salvo em XML.
 * O protagonista contém atributos, dinheiro, inventário, relacionamentos, romance,
 * capítulos/flags narrativas, contadores de lutas e limites diários de atividades.
 * Controladores, telas e o grafo de regras da história são reconstruídos em memória.
 *
 * @author Anselmo e Adna
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class GameState {

    private Player player;
    private final DataDia dataDia;
    @JsonIgnore
    private final Local mapaGlobal;
    private String localAtualId;
    private EstadoGame estadoGame;
    private final List<InteracaoNarrativa> interacoes;

    public GameState() {
        this(null, new DataDia(), "cidade_natal", List.of(), EstadoGame.EXPLORANDO, List.of());
    }

    @JsonCreator
    public GameState(@JsonProperty("player") Player player,
                     @JsonProperty("dataDia") DataDia dataDia,
                     @JsonProperty("localAtualId") String localAtualId,
                     @JsonProperty("locaisDesbloqueados") List<String> locaisDesbloqueados,
                     @JsonProperty("estadoGame") EstadoGame estadoGame,
                     @JsonProperty("interacoes") List<InteracaoNarrativa> interacoes) {
        this.player = player;
        this.dataDia = Objects.requireNonNull(dataDia, "O relógio da partida é obrigatório.");
        this.mapaGlobal = criarMundo();
        this.localAtualId = Objects.requireNonNull(localAtualId, "A posição atual da partida é obrigatória.");
        for (String id : Objects.requireNonNull(locaisDesbloqueados, "Os locais desbloqueados são obrigatórios.")) {
            buscarLocal(id).setAcessoLiberado(true);
        }
        this.estadoGame = Objects.requireNonNull(estadoGame, "O estado do jogo é obrigatório.");
        this.interacoes = new ArrayList<>(Objects.requireNonNull(interacoes,
                "O histórico de interações é obrigatório."));
        getLocalAtual(); // Rejeita uma posição inválida antes de substituir a partida ativa.
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public DataDia getDataDia() {
        return dataDia;
    }

    public Local getMapaGlobal() {
        return mapaGlobal;
    }

    public Local getLocalAtual() {
        return buscarLocal(localAtualId);
    }

    public void setLocalAtual(Local localAtual) {
        Objects.requireNonNull(localAtual, "O local atual é obrigatório.");
        if (buscarLocal(localAtual.getId()) != localAtual) {
            throw new IllegalArgumentException("O local não pertence ao mapa desta partida.");
        }
        this.localAtualId = localAtual.getId();
    }

    @JsonProperty("locaisDesbloqueados")
    public List<String> getLocaisDesbloqueados() {
        List<String> ids = new ArrayList<>();
        coletarDesbloqueados(mapaGlobal, ids);
        return ids;
    }

    private void coletarDesbloqueados(Local local, List<String> ids) {
        if (local.isAcessoLiberado()) {
            ids.add(local.getId());
        }
        for (Local filho : local.getSubLocais()) {
            coletarDesbloqueados(filho, ids);
        }
    }

    private Local buscarLocal(String id) {
        Local local = buscarLocal(mapaGlobal, Objects.requireNonNull(id, "O ID do local é obrigatório."));
        if (local == null) {
            throw new IllegalArgumentException("Local salvo desconhecido: " + id);
        }
        return local;
    }

    private Local buscarLocal(Local local, String id) {
        if (id.equals(local.getId())) return local;
        for (Local filho : local.getSubLocais()) {
            Local encontrado = buscarLocal(filho, id);
            if (encontrado != null) return encontrado;
        }
        return null;
    }

    public EstadoGame getEstadoGame() {
        return estadoGame;
    }

    public void setEstadoGame(EstadoGame estadoGame) {
        this.estadoGame = Objects.requireNonNull(estadoGame);
    }

    public List<InteracaoNarrativa> getInteracoes() {
        return Collections.unmodifiableList(interacoes);
    }

    public void registrarInteracao(String capituloId, String pergunta, int opcao, String resposta) {
        interacoes.add(new InteracaoNarrativa(capituloId, pergunta, opcao, resposta,
                dataDia.getDias(), dataDia.getHoras(), dataDia.getMinutos()));
    }

    private static Local criarMundo() {
        Local mapaGlobal = new Local("mapa_global", "Mapa Global", "Mapa principal do mundo.");
        Local cidadeNatal = new Local("cidade_natal", "Cidade Natal", "A cidade onde sua história começa.");
        Local academia = new Local("academia", "Academia", "Lugar onde você pode treinar e evoluir.");
        Local casa = new Local("casa", "Casa", "Seu lugar de descanso.");
        Local academiaBoxe = new Local("academia_boxe", "Academia de Boxe", "Lugar onde acontecem grandes lutas.");
        Local clubeLuta = new Local("clube_luta", "Clube de Luta", "Lutas de rua clandestinas e apostas rápidas.");
        Local cidadeA = new Local("cidade_a", "Cidade A", "Uma cidade mais pobre.");
        cidadeA.setCustoAcesso(400);
        Local loja = new Local("loja", "Loja", "Uma loja onde você pode comprar itens.");
        Local academiaProfissional = new Local("academia_profissional", "Academia Profissional", "Uma academia para lutadores profissionais.");
        Local cidadeB = new Local("cidade_b", "Cidade B", "Uma cidade rica e movimentada.");
        cidadeB.setCustoAcesso(1000);
        Local campeonatoMundial = new Local("campeonato_mundial", "Campeonato Mundial", "O maior campeonato de boxe do mundo.");

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
        return mapaGlobal;
    }
}
