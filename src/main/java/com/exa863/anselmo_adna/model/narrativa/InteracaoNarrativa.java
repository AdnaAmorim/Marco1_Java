package com.exa863.anselmo_adna.model.narrativa;

/** Uma decisão efetivamente tomada pelo jogador, com o horário da partida. */
public record InteracaoNarrativa(String capituloId, String pergunta, int opcao,
                                String resposta, int dia, int hora, int minuto) {
}
