package com.exa863.anselmo_adna.exception;
/**
 * Exceção utilizada quando são encontrados dados inválidos no jogo.
 * Pode ser usada na validação de configurações, saves e outros arquivos persistentes.
 *
 * @author Anselmo e Adna
 */
public class DadosInvalidosException extends Exception {

    public DadosInvalidosException(String mensagem) {
        super(mensagem);
    }
}
