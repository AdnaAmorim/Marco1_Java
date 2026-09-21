package com.exa863.anselmo_adna.model.narrativa;

/**
 * Define quando um capítulo deve ser disparado (ex: ao entrar na casa, ao vencer uma luta).
 *
 * @author Anselmo e Adna
 */
public record GatilhoNarrativo(TipoGatilho tipo, String alvo) {
}
