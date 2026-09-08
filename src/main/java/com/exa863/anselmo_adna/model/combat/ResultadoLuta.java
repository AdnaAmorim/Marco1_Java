package com.exa863.anselmo_adna.model.combat;

import com.exa863.anselmo_adna.model.character.Personagem;

public class ResultadoLuta {
    private final Personagem lutador1;
    private final Personagem lutador2;
    private final Personagem vencedor;   // null em caso de empate
    private final Personagem perdedor;   // null em caso de empate
    private final TipoVitoria tipoVitoria;
    private final double scoreLutador1;
    private final double scoreLutador2;
    private final double saldoFinal;
    private final String descricao;

    public ResultadoLuta(Personagem lutador1, Personagem lutador2,
                         Personagem vencedor, Personagem perdedor,
                         TipoVitoria tipoVitoria, double scoreLutador1,
                         double scoreLutador2, double saldoFinal, String descricao) {
        this.lutador1 = lutador1;
        this.lutador2 = lutador2;
        this.vencedor = vencedor;
        this.perdedor = perdedor;
        this.tipoVitoria = tipoVitoria;
        this.scoreLutador1 = scoreLutador1;
        this.scoreLutador2 = scoreLutador2;
        this.saldoFinal = saldoFinal;
        this.descricao = descricao;
    }

    public boolean isEmpate() {
        return vencedor == null;
    }

    public boolean isVencedor(Personagem personagem) {
        return vencedor != null && vencedor.equals(personagem);
    }

    public Personagem getLutador1() {
        return lutador1;
    }

    public Personagem getLutador2() {
        return lutador2;
    }

    public Personagem getVencedor() {
        return vencedor;
    }

    public Personagem getPerdedor() {
        return perdedor;
    }

    public TipoVitoria getTipoVitoria() {
        return tipoVitoria;
    }

    public double getScoreLutador1() {
        return scoreLutador1;
    }

    public double getScoreLutador2() {
        return scoreLutador2;
    }

    public double getSaldoFinal() {
        return saldoFinal;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return String.format("ResultadoLuta [Vencedor=%s, Tipo=%s, Saldo=%.2f, ScoreLutador1=%.2f, ScoreLutador2=%.2f]",
                vencedor != null ? vencedor.getNome() : "Nenhum (Empate)",
                tipoVitoria != null ? tipoVitoria.getDescricao() : "N/A",
                saldoFinal, scoreLutador1, scoreLutador2);
    }
}
