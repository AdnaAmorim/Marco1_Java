package com.exa863.anselmo_adna.model.combat;

import com.exa863.anselmo_adna.model.stats.Atributo;

/**
 * Classe base para os estilos de luta (Velocista, Nocauteador, Contragolpeador).
 * Cada estilo aplica bônus e penalidades nos atributos do lutador antes da luta começar.
 * O bônus escala de acordo com o nível do atributo principal do estilo.
 *
 * @author Anselmo e Adna
 */
public abstract class EstiloLuta {

    // nivel do atributo do lutador em relacao ao maximo (ex: 50% significa 5 de 10, ou 50 de 100)
    public static final double NIVEL_INICIANTE = 0.25;      // ate 25% (ex: ate 2.5 de 10)
    public static final double NIVEL_INTERMEDIARIO = 0.50;  // ate 50% (ex: 5 de 10 ou 50 de 100)
    public static final double NIVEL_AVANCADO = 0.80;       // ate 80% (ex: 8 de 10 ou 80 de 100)
    public static final double NIVEL_ELITE = 0.90;          // a partir de 90% (ex: 9 de 10 ou 90 de 100)

    // bonus que cada nivel ganha
    public static final double BONUS_INICIANTE = 0.15;     // +15%
    public static final double BONUS_INTERMEDIARIO = 0.30; // +30%
    public static final double BONUS_AVANCADO = 0.45;      // +45%
    public static final double BONUS_MESTRE = 0.60;        // +60% (entre 80% e 90%)
    public static final double BONUS_ELITE = 0.75;         // +75% (90% pra cima, os mais experientes)

    private final String nome;
    private final String descricao;

    public EstiloLuta(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    // da mais bonus pra quem tem o atributo mais alto
    public double calcularPercentualBonus(double valorAtual, double valorMaximo) {
        if (valorMaximo <= 0) {
            return BONUS_INICIANTE;
        }

        double aproveitamento = valorAtual / valorMaximo;

        if (aproveitamento <= NIVEL_INICIANTE) {
            return BONUS_INICIANTE;
        } else if (aproveitamento <= NIVEL_INTERMEDIARIO) {
            return BONUS_INTERMEDIARIO;
        } else if (aproveitamento <= NIVEL_AVANCADO) {
            return BONUS_AVANCADO;
        } else if (aproveitamento < NIVEL_ELITE) {
            return BONUS_MESTRE;
        } else {
            return BONUS_ELITE;
        }
    }

    public abstract AtributosEfetivos aplicarModificadores(Atributo base);
}
