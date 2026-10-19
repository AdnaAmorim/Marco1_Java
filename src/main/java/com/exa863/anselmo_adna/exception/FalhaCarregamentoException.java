package com.exa863.anselmo_adna.exception;
/**
 * Exceção utilizada quando ocorre uma falha ao carregar dados do jogo.
 * Pode ser usada em operações de carregamento de partidas, configurações e progresso.
 *
 * @author Anselmo e Adna
 */
public class FalhaCarregamentoException extends Exception {

    public FalhaCarregamentoException(String mensagem) {
        super(mensagem);
    }
}
