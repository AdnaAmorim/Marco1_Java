package com.exa863.anselmo_adna.exception;
/**
 * Exceção utilizada quando ocorre uma falha ao salvar dados do jogo.
 * Pode ser usada em operações de salvamento de partidas, configurações e progresso.
 *
 * @author Anselmo e Adna
 */
public class FalhaSalvamentoException extends Exception {

    public FalhaSalvamentoException(String mensagem) {
        super(mensagem);
    }
}
