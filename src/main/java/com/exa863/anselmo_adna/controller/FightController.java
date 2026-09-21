package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.model.combat.AtributosEfetivos;
import com.exa863.anselmo_adna.model.combat.Lutador;
import com.exa863.anselmo_adna.model.combat.ResultadoLuta;
import com.exa863.anselmo_adna.model.combat.TipoVitoria;

import java.util.Random;

public class FightController {

    private final Random random;

    public FightController() {
        this.random = new Random();
    }

    // pesos dos atributos na luta
    public static final double PESO_AGILIDADE = 1.5;
    public static final double PESO_INTELIGENCIA = 1.0;
    public static final double PESO_FORCA = 2.0;
    public static final double PESO_RESISTENCIA = 1.0;
    public static final double PESO_SAUDE = 0.10;      // 100 de saude vira ate 10 pontos
    public static final double PESO_ENERGIA = 0.05;    // 100 de energia vira ate 5 pontos

    // pesos de cada aspecto no resultado final
    public static final double PESO_ACERTO = 2.0;
    public static final double PESO_DANO = 1.5;
    public static final double PESO_FOLEGO = 1.0;

    // diferenca de pontos pra definir o vencedor
    public static final double PONTOS_NOCAUTE = 25.0;
    public static final double PONTOS_DECISAO = 8.0;
    public static final double PONTOS_EMPATE = 1.0;   // zona morta: entre -1.0 e 1.0 e empate

    // calcula os pontos de um lutador contra o outro
    public double calcularPontos(AtributosEfetivos atacante, AtributosEfetivos defensor) {
        // acertos de golpes
        double acerto = (atacante.agilidade * PESO_AGILIDADE)
                + (atacante.inteligencia * PESO_INTELIGENCIA);

        // dano dos golpes (evita dano negativo)
        double dano = (atacante.forca * PESO_FORCA)
                - (defensor.resistencia * PESO_RESISTENCIA);
        if (dano < 0) dano = 0;

        // folego e disposicao
        double folego = (atacante.saude * PESO_SAUDE)
                + (atacante.energia * PESO_ENERGIA);

        return (acerto * PESO_ACERTO) + (dano * PESO_DANO) + (folego * PESO_FOLEGO);
    }

    // resolve a luta entre dois personagens
    public ResultadoLuta resolverCombate(Lutador lutador1, Lutador lutador2) {

        // calcula os atributos de acordo com o estilo de cada um
        AtributosEfetivos eff1 = lutador1.getAtributosEfetivos();
        AtributosEfetivos eff2 = lutador2.getAtributosEfetivos();

        double score1 = calcularPontos(eff1, eff2);
        double score2 = calcularPontos(eff2, eff1);

        double saldoFinal = score1 - score2;

        Lutador vencedor = null;
        Lutador perdedor = null;
        TipoVitoria tipoVitoria;
        String descricao;

        if (saldoFinal == 0) {
            tipoVitoria = TipoVitoria.EMPATE;
            descricao = "A luta terminou em Empate.";
        } else if (saldoFinal > 0) {
            vencedor = lutador1;
            perdedor = lutador2;
            if (saldoFinal < PONTOS_DECISAO) {
                tipoVitoria = TipoVitoria.DECISAO_DIVIDIDA;
                descricao = String.format("%s venceu por Decisão Dividida em luta equilibrada.", lutador1.getNome());
            } else if (saldoFinal < PONTOS_NOCAUTE) {
                tipoVitoria = TipoVitoria.DECISAO_UNANIME;
                descricao = String.format("%s venceu por Decisão Unânime.", lutador1.getNome());
            } else {
                tipoVitoria = TipoVitoria.NOCAUTE;
                descricao = String.format("%s venceu por Nocaute (KO).", lutador1.getNome());
            }
        } else {
            // saldoFinal < 0 -> lutador2 ganhou
            vencedor = lutador2;
            perdedor = lutador1;
            if (saldoFinal > -PONTOS_DECISAO) {
                tipoVitoria = TipoVitoria.DECISAO_DIVIDIDA;
                descricao = String.format("%s venceu por Decisão Dividida em luta equilibrada.", lutador2.getNome());
            } else if (saldoFinal > -PONTOS_NOCAUTE) {
                tipoVitoria = TipoVitoria.DECISAO_UNANIME;
                descricao = String.format("%s venceu por Decisão Unânime.", lutador2.getNome());
            } else {
                tipoVitoria = TipoVitoria.NOCAUTE;
                descricao = String.format("%s venceu por Nocaute (KO).", lutador2.getNome());
            }
        }

        int turnos;
        if (Math.abs(saldoFinal) >= PONTOS_NOCAUTE) {
            turnos = 2 + this.random.nextInt(3); // 2 a 4
        } else if (Math.abs(saldoFinal) >= PONTOS_DECISAO) {
            turnos = 5 + this.random.nextInt(3); // 5 a 7
        } else {
            turnos = 8 + this.random.nextInt(3); // 8 a 10
        }

        return new ResultadoLuta(lutador1, lutador2, vencedor, perdedor, tipoVitoria, score1, score2, saldoFinal, descricao, turnos);
    }
}