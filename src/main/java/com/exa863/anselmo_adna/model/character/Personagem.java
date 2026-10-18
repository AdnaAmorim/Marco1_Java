package com.exa863.anselmo_adna.model.character;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe base de todos os personagens do jogo.
 * Guarda id, nome, descrição, cor dos olhos e sexo.
 *
 * @author Anselmo e Adna
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class Personagem {

    private int id;
    private String nome;
    private String descricao;
    private Cores corOlhos;
    private Sexo sexo;

    @JsonCreator
    public Personagem(@JsonProperty("id") int id, @JsonProperty("nome") String nome,
                      @JsonProperty("descricao") String descricao,
                      @JsonProperty("corOlhos") Cores corOlhos, @JsonProperty("sexo") Sexo sexo) {
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
