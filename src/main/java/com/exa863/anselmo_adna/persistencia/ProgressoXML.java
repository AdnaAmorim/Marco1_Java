package com.exa863.anselmo_adna.persistencia;

import com.exa863.anselmo_adna.exception.FalhaCarregamentoException;
import com.exa863.anselmo_adna.exception.FalhaSalvamentoException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
/**
 * Responsável por salvar e carregar o progresso global do jogo em XML.
 * Armazena os finais alcançados e as conquistas desbloqueadas pelo jogador.
 *
 * @author Anselmo e Adna
 */
public class ProgressoXML {

    private final String caminho = "progresso.xml";
    /**
     * Salva os finais alcançados e as conquistas desbloqueadas
     * no arquivo de progresso global.
     *
     * @param progresso Progresso global atual do jogo.
     * @throws FalhaSalvamentoException Caso ocorra erro ao salvar o arquivo.
     */
    public void salvar(ProgressoGlobal progresso) throws FalhaSalvamentoException {
        Properties dados = new Properties();

        String finais = String.join(",", progresso.finaisAlcancados);
        String conquistas = String.join(",", progresso.conquistasDesbloqueadas);

        dados.setProperty("finaisAlcancados", finais);
        dados.setProperty("conquistasDesbloqueadas", conquistas);

        try (FileOutputStream arquivo = new FileOutputStream(caminho)) {
            dados.storeToXML(arquivo, "Progresso global do jogo");
        } catch (IOException e) {
            throw new FalhaSalvamentoException("Erro ao salvar o progresso global.");
        }
    }
    /**
     * Carrega os finais e as conquistas salvas no arquivo de progresso.
     * Caso o arquivo ainda não exista, mantém o progresso vazio.
     *
     * @param progresso Objeto que receberá os dados carregados.
     * @throws FalhaCarregamentoException Caso ocorra erro ao carregar o arquivo.
     */
    public void carregar(ProgressoGlobal progresso) throws FalhaCarregamentoException {
        File arquivo = new File(caminho);

        if (!arquivo.exists()) {
            return;
        }

        Properties dados = new Properties();

        try (FileInputStream entrada = new FileInputStream(arquivo)) {
            dados.loadFromXML(entrada);
        } catch (IOException e) {
            throw new FalhaCarregamentoException("Erro ao carregar o progresso global.");
        }

        String finais = dados.getProperty("finaisAlcancados", "");
        String conquistas = dados.getProperty("conquistasDesbloqueadas", "");

        progresso.finaisAlcancados.clear();
        progresso.conquistasDesbloqueadas.clear();

        if (!finais.isEmpty()) {
            String[] listaFinais = finais.split(",");

            for (String finalAlcancado : listaFinais) {
                progresso.registrarFinal(finalAlcancado);
            }
        }

        if (!conquistas.isEmpty()) {
            String[] listaConquistas = conquistas.split(",");

            for (String conquista : listaConquistas) {
                progresso.registrarConquista(conquista);
            }
        }
    }
}
