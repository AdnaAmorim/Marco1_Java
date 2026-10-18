package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.exception.FalhaCarregamentoException;
import com.exa863.anselmo_adna.exception.FalhaSalvamentoException;
import com.exa863.anselmo_adna.persistencia.ProgressoGlobal;
import com.exa863.anselmo_adna.persistencia.ProgressoXML;

public class ConquistaController {

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
