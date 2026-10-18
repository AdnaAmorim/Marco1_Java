package com.exa863.anselmo_adna.model.world;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um local do mapa (cidade, academia, casa etc.).
 * Um local pode ter sublocais dentro dele e opcionalmente um custo para entrar.
 *
 * @author Anselmo e Adna
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class Local {

    private String id;
    private String nome;
    private String descricao;
    @JsonIgnore
    private Local localPai;
    private List<Local> subLocais;
    private boolean localDeSaida;
    private int custoAcesso;
    private boolean acessoLiberado;

    @JsonCreator
    public Local(@JsonProperty("nome") String nome, @JsonProperty("descricao") String descricao) {
        this.nome = nome;
        this.descricao = descricao;
        this.subLocais = new ArrayList<>();
        this.localDeSaida = false;
        this.custoAcesso = 0;
        this.acessoLiberado = false;
    }

    public String getNome() {
        return nome;
    }

    public Local(String id, String nome, String descricao) {
        this(nome, descricao);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public Local getLocalPai() {
        return localPai;
    }

    public List<Local> getSubLocais() {
        return subLocais;
    }

    public int getCustoAcesso() {
        return custoAcesso;
    }
    public void setCustoAcesso(int custoAcesso) {
        this.custoAcesso = custoAcesso;
    }

    public boolean isLocalDeSaida() {
        return localDeSaida;
    }
    public boolean isAcessoLiberado() {
        return acessoLiberado;
    }

    public void adicionarSubLocal(Local local) {

        local.localPai = this;

        subLocais.add(local);
    }
    public void setAcessoLiberado(boolean acessoLiberado) {
        this.acessoLiberado = acessoLiberado;
    }

    public void adicionarSaida() {

        Local saida = new Local(
                "Sair daqui",
                "Voltar para o mapa anterior."
        );

        saida.localDeSaida = true;
        saida.id = id + "_saida";
        saida.localPai = this;

        subLocais.add(saida);
    }

    /** Reconstitui as referências de retorno sem duplicar os locais no XML. */
    public void restaurarVinculos() {
        for (Local local : subLocais) {
            local.localPai = this;
            local.restaurarVinculos();
        }
    }
}
