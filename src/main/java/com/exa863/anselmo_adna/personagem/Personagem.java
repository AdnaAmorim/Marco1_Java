package com.exa863.anselmo_adna.personagem;

public class Personagem {
    private int id;
    private String nome;
    private String descricao;
    private Cores corOlhos;
    private Sexo sexo;

    public Personagem(int id, String nome, String descricao, Cores corOlhos, Sexo sexo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.corOlhos = corOlhos;
        this.sexo = sexo;
    }

    public String getNome() {
        return nome;
    }

}
