package com.exa863.anselmo_adna.persistencia;

import com.exa863.anselmo_adna.exception.DadosInvalidosException;
import com.exa863.anselmo_adna.exception.FalhaCarregamentoException;
import com.exa863.anselmo_adna.exception.FalhaSalvamentoException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
/**
 * Responsável por salvar e carregar as configurações do jogo em XML.
 * Mantém as preferências do jogador entre as execuções do programa.
 *
 * @author Anselmo e Adna
 */
public class ConfiguracaoXML {
    /**
     * Salva as configurações atuais do jogo no arquivo configuracao.xml.
     *
     * @param config Configurações atuais do jogo.
     * @throws FalhaSalvamentoException Caso ocorra erro ao salvar o arquivo.
     */
    public void salvar(Configuracao config) throws FalhaSalvamentoException {
        Properties dados = new Properties();

        dados.setProperty("autoSave", String.valueOf(config.autoSave));
        dados.setProperty("pularAnimacaoCombate", String.valueOf(config.pularAnimacaoCombate));
        dados.setProperty("pularCutscene", String.valueOf(config.pularCutscene));
        dados.setProperty("volumeMusica", String.valueOf(config.volumeMusica));
        dados.setProperty("volumeEfeitos", String.valueOf(config.volumeEfeitos));

        try (FileOutputStream arquivo = new FileOutputStream("configuracao.xml")) {
            dados.storeToXML(arquivo, "Configuracoes do jogo");
        } catch (IOException e) {
            throw new FalhaSalvamentoException("Erro ao salvar as configurações.");
        }
    }
    /**
     * Carrega as configurações salvas no arquivo configuracao.xml.
     * Caso o arquivo ainda não exista, mantém os valores padrão.
     *
     * @param config Objeto que receberá as configurações carregadas.
     * @throws FalhaCarregamentoException Caso ocorra erro ao carregar o arquivo.
     * @throws DadosInvalidosException Caso algum valor salvo seja inválido.
     */
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

        String autoSave = dados.getProperty("autoSave", "true");
        String pularAnimacao = dados.getProperty("pularAnimacaoCombate", "false");
        String pularCutscene = dados.getProperty("pularCutscene", "false");
        // Arquivos de configuração anteriores à inclusão do áudio usam os volumes padrão do jogo.
        String volumeMusica = dados.getProperty("volumeMusica", "80");
        String volumeEfeitos = dados.getProperty("volumeEfeitos", "60");

        config.autoSave = lerBoolean(autoSave, "autoSave");
        config.pularAnimacaoCombate = lerBoolean(pularAnimacao, "pularAnimacaoCombate");
        config.pularCutscene = lerBoolean(pularCutscene, "pularCutscene");
        config.volumeMusica = lerVolume(volumeMusica, "volumeMusica");
        config.volumeEfeitos = lerVolume(volumeEfeitos, "volumeEfeitos");
    }

    private int lerVolume(String valor, String nome) throws DadosInvalidosException {
        try {
            int numero = Integer.parseInt(valor);
            if (numero < 0 || numero > 100) {
                throw new DadosInvalidosException("Valor inválido na configuração: " + nome);
            }
            return numero;
        } catch (NumberFormatException e) {
            throw new DadosInvalidosException("Valor inválido na configuração: " + nome);
        }
    }

    private boolean lerBoolean(String valor, String nome)
            throws DadosInvalidosException {

        if (valor.equals("true")) {
            return true;
        }

        if (valor.equals("false")) {
            return false;
        }

        throw new DadosInvalidosException(
                "Valor inválido na configuração: " + nome
        );
    }
}