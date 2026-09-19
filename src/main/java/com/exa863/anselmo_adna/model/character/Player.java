package com.exa863.anselmo_adna.model.character;

import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.model.stats.Inventario;
import com.exa863.anselmo_adna.model.stats.Relacionamentos;

public class Player extends Personagem {

    private int dinheiro;
    private Atributo atributos;
    private Relacionamentos[] relacionamentos;
    private Inventario inventario;

    public Player(int id, String nome, String descricao, Cores corOlhos, Sexo sexo) {
        super(id, nome, descricao, corOlhos, sexo);
        this.dinheiro = 0;
        this.atributos = new Atributo();
        this.relacionamentos = new Relacionamentos[10];
        this.inventario = new Inventario();
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
}