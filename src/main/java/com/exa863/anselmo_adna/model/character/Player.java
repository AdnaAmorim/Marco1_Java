package com.exa863.anselmo_adna.model.character;

import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.model.stats.Inventario;
import com.exa863.anselmo_adna.model.stats.Relacionamentos;

import java.util.HashSet;
import java.util.Set;

public class Player extends Personagem {

    private int dinheiro;
    private Atributo atributos;
    private Relacionamentos[] relacionamentos;
    private Inventario inventario;
    private boolean temLuvas;
    private final Set<String> capitulosConcluidos;

    public Player(int id, String nome, String descricao, Cores corOlhos, Sexo sexo) {
        super(id, nome, descricao, corOlhos, sexo);
        this.dinheiro = 0;
        this.atributos = new Atributo();
        this.relacionamentos = new Relacionamentos[10];
        this.inventario = new Inventario();
        this.temLuvas = false;
        this.capitulosConcluidos = new HashSet<>();
    }

    public int getDinheiro() {
        return dinheiro;
    }

    public void setDinheiro(int dinheiro) {
        this.dinheiro = dinheiro;
    }

    public Atributo getAtributos() {
        return atributos;
    }

    public Relacionamentos[] getRelacionamentos() {
        return relacionamentos;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public boolean isTemLuvas() {
        return temLuvas;
    }

    public void setTemLuvas(boolean temLuvas) {
        this.temLuvas = temLuvas;
    }

    public void concluirCapitulo(String idCapitulo) {
        if (idCapitulo != null) {
            capitulosConcluidos.add(idCapitulo);
        }
    }

    public boolean isCapituloConcluido(String idCapitulo) {
        return capitulosConcluidos.contains(idCapitulo);
    }

    public Set<String> getCapitulosConcluidos() {
        return capitulosConcluidos;
    }
}