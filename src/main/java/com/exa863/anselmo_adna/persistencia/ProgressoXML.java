package com.exa863.anselmo_adna.persistencia;

import com.exa863.anselmo_adna.exception.FalhaCarregamentoException;
import com.exa863.anselmo_adna.exception.FalhaSalvamentoException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ProgressoXML {

    private final String caminho = "progresso.xml";

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
