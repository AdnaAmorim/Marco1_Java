package com.exa863.anselmo_adna.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador base para gerenciamento de arquivos
 *
 * @author Anselmo e Adna
 */
public abstract class FileController {

    protected final Path pastaBase;

    public FileController(Path pastaBase) {
        this.pastaBase = pastaBase;
        criarPastaSeNaoExistir();
    }

    public FileController(String caminhoPasta) {
        this(Path.of(caminhoPasta));
    }

    private void criarPastaSeNaoExistir() {
        File pasta = pastaBase.toFile();
        if (!pasta.exists()) {
            pasta.mkdirs();
        }
    }

    protected Path obterCaminho(String nomeArquivo) {
        return pastaBase.resolve(nomeArquivo);
    }

    public abstract <T> void salvar(String nomeArquivo, T dados) throws IOException;

    public abstract <T> T carregar(String nomeArquivo, Class<T> tipo) throws IOException;

    public boolean excluir(String nomeArquivo) {
        File arquivo = obterCaminho(nomeArquivo).toFile();
        return arquivo.delete();
    }

    public boolean existe(String nomeArquivo) {
        File arquivo = obterCaminho(nomeArquivo).toFile();
        return arquivo.exists();
    }

    public List<String> listarArquivos() {
        File pasta = pastaBase.toFile();
        File[] arquivos = pasta.listFiles();

        if (arquivos == null) {
            return new ArrayList<>();
        }

        List<String> nomes = new ArrayList<>();
        for (File arquivo : arquivos) {
            if (arquivo.isFile()) {
                nomes.add(arquivo.getName());
            }
        }
        return nomes;
    }

    public Path getPastaBase() {
        return pastaBase;
    }
}
