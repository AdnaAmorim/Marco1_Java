package com.exa863.anselmo_adna.model.world;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um local do mapa (cidade, academia, casa etc.).
 * Um local pode ter sublocais dentro dele e opcionalmente um custo para entrar.
 *
 * @author Anselmo e Adna
 */
public class Local {

    private String nome;
    private String descricao;
    private Local localPai;
    private List<Local> subLocais;
    private boolean localDeSaida;
    private int custoAcesso;
    private boolean acessoLiberado;

    public Local(String nome, String descricao) {
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
        saida.localPai = this;

        subLocais.add(saida);
    }
}
