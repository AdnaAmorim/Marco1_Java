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
    private final Set<String> capitulosConcluidos;
    private int ultimoDiaTreinado;
    private int ultimoDiaLutaBoxe;

    public Player(int id, String nome, String descricao, Cores corOlhos, Sexo sexo) {
        super(id, nome, descricao, corOlhos, sexo);
        this.dinheiro = 0;
        this.atributos = new Atributo();
        this.relacionamentos = new Relacionamentos[10];
        this.inventario = new Inventario();
        this.capitulosConcluidos = new HashSet<>();
        this.ultimoDiaTreinado = 0;
        this.ultimoDiaLutaBoxe = -1;
    }

    public int getUltimoDiaTreinado() {
        return ultimoDiaTreinado;
    }

    public void setUltimoDiaTreinado(int ultimoDiaTreinado) {
        this.ultimoDiaTreinado = ultimoDiaTreinado;
    }

    public boolean jaTreinouHoje(int diaAtual) {
        return this.ultimoDiaTreinado == diaAtual;
    }

    public int getUltimoDiaLutaBoxe() {
        return ultimoDiaLutaBoxe;
    }

    public void setUltimoDiaLutaBoxe(int ultimoDiaLutaBoxe) {
        this.ultimoDiaLutaBoxe = ultimoDiaLutaBoxe;
    }

    public boolean podeLutarBoxe(int diaAtual) {
        if (this.ultimoDiaLutaBoxe == -1) {
            return true;
        }
        return (diaAtual - this.ultimoDiaLutaBoxe) >= 2;
    }

    public int getProximoDiaLutaBoxe() {
        if (this.ultimoDiaLutaBoxe == -1) {
            return 1;
        }
        return this.ultimoDiaLutaBoxe + 2;
    }

    public void registrarLutaBoxe(int diaAtual) {
        this.ultimoDiaLutaBoxe = diaAtual;
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