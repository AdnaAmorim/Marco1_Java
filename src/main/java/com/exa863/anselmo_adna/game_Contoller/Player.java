package com.exa863.anselmo_adna.game_Contoller;

import com.exa863.anselmo_adna.personagem.Personagem;
import com.exa863.anselmo_adna.atributos.Atributo;
//import com.exa863.anselmo_adna.inventario.Inventario;
import com.exa863.anselmo_adna.relacionamentos.Relacionamentos;

public class Player extends Personagem {

    private int dinheiro;
    private Atributo atributos;
    private Relacionamentos[] relacionamentos;
    //private Inventario inventario;

    public Player() {

        super(id, nome, descricao, corOlhos, sexo);

        this.dinheiro = dinheiro;
        this.atributos = atributos;


        this.relacionamentos = new Relacionamentos[];
       // this.inventario = new Inventario();
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

    //public Inventario getInventario() {
       // return inventario;
    }
//}