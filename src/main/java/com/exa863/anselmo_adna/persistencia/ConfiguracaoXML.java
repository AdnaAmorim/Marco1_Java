
package com.exa863.anselmo_adna.persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfiguracaoXML {

    public void salvar(Configuracao config) throws IOException {

        Properties dados = new Properties();

        dados.setProperty("dificuldade",
                config.dificuldade);

        dados.setProperty("pularAnimacaoCombate",
                String.valueOf(config.pularAnimacaoCombate));

        dados.setProperty("pularCutscene",
                String.valueOf(config.pularCutscene));

        try (FileOutputStream arquivo =
                     new FileOutputStream("configuracao.xml")) {

            dados.storeToXML(arquivo, "Configuracoes do jogo");
        }
    }

    public void carregar(Configuracao config) throws IOException {

        File arquivo = new File("configuracao.xml");

        if (!arquivo.exists()) {
            return;
        }

        Properties dados = new Properties();

        try (FileInputStream entrada =
                     new FileInputStream(arquivo)) {

            dados.loadFromXML(entrada);
        }

        config.dificuldade =
                dados.getProperty("dificuldade", "Normal");

        config.pularAnimacaoCombate =
                Boolean.parseBoolean(
                        dados.getProperty("pularAnimacaoCombate", "false"));

        config.pularCutscene =
                Boolean.parseBoolean(
                        dados.getProperty("pularCutscene", "false"));
    }
}
