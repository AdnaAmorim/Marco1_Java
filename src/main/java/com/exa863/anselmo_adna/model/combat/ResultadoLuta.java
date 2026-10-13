package com.exa863.anselmo_adna.model.combat;

import com.exa863.anselmo_adna.model.character.Personagem;
import com.exa863.anselmo_adna.model.combat.EstiloLuta;
import com.exa863.anselmo_adna.model.stats.Atributo;

public class ResultadoLuta {
    private final Lutador lutador1;
    private final Lutador lutador2;
    private final Lutador vencedor;   // null em caso de empate
    private final Lutador perdedor;   // null em caso de empate
    private final TipoVitoria tipoVitoria;
    private final double scoreLutador1;
    private final double scoreLutador2;
    private final double saldoFinal;
    private final String descricao;
    private final int turnos;

    public ResultadoLuta(Lutador lutador1, Lutador lutador2,
                         Lutador vencedor, Lutador perdedor,
                         TipoVitoria tipoVitoria, double scoreLutador1,
                         double scoreLutador2, double saldoFinal, String descricao, int turnos) {
        this.lutador1 = lutador1;
        this.lutador2 = lutador2;
        this.vencedor = vencedor;
        this.perdedor = perdedor;
        this.tipoVitoria = tipoVitoria;
        this.scoreLutador1 = scoreLutador1;
        this.scoreLutador2 = scoreLutador2;
        this.saldoFinal = saldoFinal;
        this.descricao = descricao;
        this.turnos = turnos;
        
    }

    public boolean isEmpate() {
        return vencedor == null;
    }

    public boolean isVencedor(Lutador lutador) {
        return vencedor != null && vencedor.equals(lutador);
    }

    public Lutador getLutador1() {
        return lutador1;
    }

    public Lutador getLutador2() {
        return lutador2;
    }

    public Lutador getVencedor() {
        return vencedor;
    }

    public Lutador getPerdedor() {
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

    public int getTurnos() {
        return turnos;
    }

    @Override
    public String toString() {
        return String.format("ResultadoLuta [Vencedor=%s, Tipo=%s, Saldo=%.2f, ScoreLutador1=%.2f, ScoreLutador2=%.2f]",
                vencedor != null ? vencedor.getNome() : "Nenhum (Empate)",
                tipoVitoria != null ? tipoVitoria.getDescricao() : "N/A",
                saldoFinal, scoreLutador1, scoreLutador2);
    }
}
