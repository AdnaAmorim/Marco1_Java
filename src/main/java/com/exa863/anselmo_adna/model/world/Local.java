package com.exa863.anselmo_adna.model.world;

public class Local {
    private String nome;
    private String descricao;

    public Local(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }
    public String getNome() {
        return nome;
    }
    public String getDescricao() {
        return descricao;
    }
}
