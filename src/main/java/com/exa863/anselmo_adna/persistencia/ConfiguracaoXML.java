package com.exa863.anselmo_adna.persistencia;

import com.exa863.anselmo_adna.exception.DadosInvalidosException;
import com.exa863.anselmo_adna.exception.FalhaCarregamentoException;
import com.exa863.anselmo_adna.exception.FalhaSalvamentoException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfiguracaoXML {

    public void salvar(Configuracao config) throws FalhaSalvamentoException {
        Properties dados = new Properties();

        dados.setProperty("autoSave", String.valueOf(config.autoSave));
        dados.setProperty("pularAnimacaoCombate", String.valueOf(config.pularAnimacaoCombate));
        dados.setProperty("pularCutscene", String.valueOf(config.pularCutscene));

        try (FileOutputStream arquivo = new FileOutputStream("configuracao.xml")) {
            dados.storeToXML(arquivo, "Configuracoes do jogo");
        } catch (IOException e) {
            throw new FalhaSalvamentoException("Erro ao salvar as configurações.");
        }
    }

    public void carregar(Configuracao config)
            throws FalhaCarregamentoException, DadosInvalidosException {

        File arquivo = new File("configuracao.xml");

        if (!arquivo.exists()) {
            return;
        }

        Properties dados = new Properties();

        try (FileInputStream entrada = new FileInputStream(arquivo)) {
            dados.loadFromXML(entrada);
        } catch (IOException e) {
            throw new FalhaCarregamentoException("Erro ao carregar as configurações.");
        }

        String autoSave = dados.getProperty("autoSave", "false");
        String pularAnimacao = dados.getProperty("pularAnimacaoCombate", "false");
        String pularCutscene = dados.getProperty("pularCutscene", "false");

        config.autoSave = lerBoolean(autoSave, "autoSave");
        config.pularAnimacaoCombate = lerBoolean(pularAnimacao, "pularAnimacaoCombate");
        config.pularCutscene = lerBoolean(pularCutscene, "pularCutscene");
    }

    private boolean lerBoolean(String valor, String nome)
            throws DadosInvalidosException {

        if (valor.equals("true")) {
            return true;
        }

        if (valor.equals("false")) {
            return false;
        }

        throw new DadosInvalidosException("Valor inválido na configuração: " + nome);
    }
}
