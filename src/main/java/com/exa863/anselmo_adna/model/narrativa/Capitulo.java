package com.exa863.anselmo_adna.model.narrativa;

import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.world.NomeLocal;

import java.util.List;

public abstract class Capitulo {

    private final String id;
    private final String titulo;
    private final GatilhoNarrativo gatilho;
    private final List<String> dependencias;

    public Capitulo(String id, String titulo, GatilhoNarrativo gatilho, List<String> dependencias) {
        this.id = id;
        this.titulo = titulo;
        this.gatilho = gatilho;
        this.dependencias = dependencias != null ? dependencias : List.of();
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public GatilhoNarrativo getGatilho() {
        return gatilho;
    }

    public List<String> getDependencias() {
        return dependencias;
    }

    // Retorna a lista linear de diálogos do capítulo
    public abstract List<Dialogo> getDialogos(Player player);

    // Retorna a lista de locais bloqueados enquanto este capítulo estiver ativo/pendente
    public List<NomeLocal> getLocaisBloqueados() {
        return List.of();
    }

    public boolean isLocalBloqueado(String nomeLocal) {
        if (nomeLocal == null) {
            return false;
        }
        return getLocaisBloqueados().stream()
                .anyMatch(bloqueado -> bloqueado.corresponde(nomeLocal));
    }

    public void finalizar(Player player) {
        if (player != null) {
            player.concluirCapitulo(id);
        }
    }
}
