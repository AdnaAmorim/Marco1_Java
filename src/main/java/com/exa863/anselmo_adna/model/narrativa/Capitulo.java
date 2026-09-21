package com.exa863.anselmo_adna.model.narrativa;

import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.world.NomeLocal;

import java.util.List;

/**
 * Classe base para os capítulos da história.
 * Cada capítulo tem suas dependências, onde acontece, seus diálogos e o que precisa para liberar.
 *
 * @author Anselmo e Adna
 */
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

    /**
     * Retorna os diálogos e opções deste capítulo.
     *
     * @param player O jogador atual.
     * @return Lista com as falas e escolhas da cena.
     */
    public abstract List<Dialogo> getDialogos(Player player);

    /**
     * Confere se o jogador cumpre os requisitos extras para começar este capítulo
     * (como ter dinheiro suficiente, lutas feitas ou amizade com alguém).
     *
     * @param player O jogador atual.
     * @return true se o capítulo puder começar.
     */
    public boolean podeIniciar(Player player) {
        return true;
    }

    /**
     * Lista de locais que ficam bloqueados enquanto este capítulo não for feito.
     */
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

    /**
     * Marca o capítulo como concluído para o jogador.
     */
    public void finalizar(Player player) {
        if (player != null) {
            player.concluirCapitulo(id);
        }
    }
}