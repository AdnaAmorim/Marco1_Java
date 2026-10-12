package com.exa863.anselmo_adna.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Leitura e escrita de arquivos em XML usando Jackson.
 *
 * @author Anselmo e Adna
 */
public class XmlFileController extends FileController {

    private final XmlMapper xmlMapper;

    public XmlFileController(Path pastaBase) {
        this(pastaBase, criarMapperPadrao());
    }

    public XmlFileController(String caminhoPasta) {
        this(Path.of(caminhoPasta), criarMapperPadrao());
    }

    public XmlFileController(Path pastaBase, XmlMapper xmlMapper) {
        super(pastaBase);
        this.xmlMapper = (xmlMapper != null) ? xmlMapper : criarMapperPadrao();
    }

    private static XmlMapper criarMapperPadrao() {
        XmlMapper mapper = new XmlMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    @Override
    protected Path obterCaminho(String nomeArquivo) {
        String nomeComExtensao = nomeArquivo.endsWith(".xml") ? nomeArquivo : nomeArquivo + ".xml";
        return super.obterCaminho(nomeComExtensao);
    }

    @Override
    public <T> void salvar(String nomeArquivo, T dados) throws IOException {
        File arquivo = obterCaminho(nomeArquivo).toFile();
        xmlMapper.writeValue(arquivo, dados);
    }

    @Override
    public <T> T carregar(String nomeArquivo, Class<T> tipo) throws IOException {
        File arquivo = obterCaminho(nomeArquivo).toFile();
        if (!arquivo.exists()) {
            throw new FileNotFoundException("Arquivo não encontrado: " + arquivo.getAbsolutePath());
        }
        return xmlMapper.readValue(arquivo, tipo);
    }

    public XmlMapper getXmlMapper() {
        return xmlMapper;
    }
}
