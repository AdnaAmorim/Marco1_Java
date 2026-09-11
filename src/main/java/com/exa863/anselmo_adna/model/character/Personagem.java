package com.exa863.anselmo_adna.model.character;

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

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Cores getCorOlhos() {
        return corOlhos;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setCorOlhos(Cores corOlhos) {
        this.corOlhos = corOlhos;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }
}