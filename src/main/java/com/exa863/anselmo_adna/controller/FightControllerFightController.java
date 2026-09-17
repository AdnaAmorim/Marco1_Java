package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.model.character.Personagem;
import com.exa863.anselmo_adna.model.combat.AtributosEfetivos;
import com.exa863.anselmo_adna.model.combat.EstiloLuta;
import com.exa863.anselmo_adna.model.combat.ResultadoLuta;
import com.exa863.anselmo_adna.model.combat.TipoVitoria;
import com.exa863.anselmo_adna.model.stats.Atributo;

public class FightControllerFightController {

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

        // dano dos golpes
        double dano = (atacante.forca * PESO_FORCA)
                - (defensor.resistencia * PESO_RESISTENCIA);

        // folego e disposicao
        double folego = (atacante.saude * PESO_SAUDE)
                + (atacante.energia * PESO_ENERGIA);

        return (acerto * PESO_ACERTO) + (dano * PESO_DANO) + (folego * PESO_FOLEGO);
    }

    // resolve a luta entre dois personagens
    public ResultadoLuta resolverCombate(Personagem lutador1, Atributo attrLutador1, EstiloLuta estiloLutador1,
                                        Personagem lutador2, Atributo attrLutador2, EstiloLuta estiloLutador2) {

        // calcula os atributos de acordo com o estilo de cada um
        AtributosEfetivos eff1 = estiloLutador1.aplicarModificadores(attrLutador1);
        AtributosEfetivos eff2 = estiloLutador2.aplicarModificadores(attrLutador2);

        double score1 = calcularPontos(eff1, eff2);
        double score2 = calcularPontos(eff2, eff1);

        double saldoFinal = score1 - score2;

        Personagem vencedor = null;
        Personagem perdedor = null;
        TipoVitoria tipoVitoria;
        String descricao;

        if (saldoFinal >= PONTOS_NOCAUTE) {
            vencedor = lutador1;
            perdedor = lutador2;
            tipoVitoria = TipoVitoria.NOCAUTE;
            descricao = String.format("%s venceu por Nocaute (KO).", lutador1.getNome());
        } else if (saldoFinal >= PONTOS_DECISAO) {
            vencedor = lutador1;
            perdedor = lutador2;
            tipoVitoria = TipoVitoria.DECISAO_UNANIME;
            descricao = String.format("%s venceu por Decisão Unânime.", lutador1.getNome());
        } else if (saldoFinal > PONTOS_EMPATE) {
            vencedor = lutador1;
            perdedor = lutador2;
            tipoVitoria = TipoVitoria.DECISAO_DIVIDIDA;
            descricao = String.format("%s venceu por Decisão Dividida em luta equilibrada.", lutador1.getNome());
        } else if (saldoFinal >= -PONTOS_EMPATE) {
            tipoVitoria = TipoVitoria.EMPATE;
            descricao = "Luta empatada na pontuação dos juízes.";
        } else if (saldoFinal > -PONTOS_DECISAO) {
            vencedor = lutador2;
            perdedor = lutador1;
            tipoVitoria = TipoVitoria.DECISAO_DIVIDIDA;
            descricao = String.format("%s venceu por Decisão Dividida em luta equilibrada.", lutador2.getNome());
        } else if (saldoFinal > -PONTOS_NOCAUTE) {
            vencedor = lutador2;
            perdedor = lutador1;
            tipoVitoria = TipoVitoria.DECISAO_UNANIME;
            descricao = String.format("%s venceu por Decisão Unânime.", lutador2.getNome());
        } else {
            vencedor = lutador2;
            perdedor = lutador1;
            tipoVitoria = TipoVitoria.NOCAUTE;
            descricao = String.format("%s venceu por Nocaute (KO).", lutador2.getNome());
        }

        return new ResultadoLuta(lutador1, lutador2, vencedor, perdedor, tipoVitoria, score1, score2, saldoFinal, descricao);
    }
}
