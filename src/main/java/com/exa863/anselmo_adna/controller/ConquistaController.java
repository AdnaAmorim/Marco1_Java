package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.exception.FalhaCarregamentoException;
import com.exa863.anselmo_adna.exception.FalhaSalvamentoException;
import com.exa863.anselmo_adna.persistencia.ProgressoGlobal;
import com.exa863.anselmo_adna.persistencia.ProgressoXML;
/**
 * Controla o desbloqueio das conquistas do jogo.
 * Verifica se a conquista já foi obtida e salva o novo progresso quando necessário.
 *
 * @author Anselmo e Adna
 */
public class ConquistaController {
    /**
     * Tenta desbloquear uma conquista para o jogador.
     *
     * @param conquista Identificador da conquista que será desbloqueada.
     * @return true se a conquista foi desbloqueada agora ou false se já havia sido obtida.
     */
    public boolean desbloquear(String conquista)
            throws FalhaCarregamentoException, FalhaSalvamentoException {

        ProgressoGlobal progresso = new ProgressoGlobal();
        ProgressoXML xml = new ProgressoXML();

        xml.carregar(progresso);

        if (!progresso.conquistaJaDesbloqueada(conquista)) {
            progresso.registrarConquista(conquista);
            xml.salvar(progresso);
            return true;
        }

        return false;
    }
}
