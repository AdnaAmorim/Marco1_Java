package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.model.save.SaveGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o XmlFileController.
 *
 * @author Anselmo e Adna
 */
public class XmlFileControllerTest {

    @TempDir
    Path tempDir;

    private XmlFileController controller;

    @BeforeEach
    public void setUp() {
        controller = new XmlFileController(tempDir);
    }

    @Test
    public void testSalvarECarregarSaveGame() throws IOException {
        SaveGame saveOriginal = new SaveGame(1, "2026-10-12 17:00:00");

        controller.salvar("slot1.xml", saveOriginal);

        assertTrue(controller.existe("slot1.xml"));

        SaveGame saveCarregado = controller.carregar("slot1.xml", SaveGame.class);

        assertNotNull(saveCarregado);
        assertEquals(saveOriginal.getVersao(), saveCarregado.getVersao());
        assertEquals(saveOriginal.getDataSalvamento(), saveCarregado.getDataSalvamento());
        assertEquals(saveOriginal, saveCarregado);
    }

    @Test
    public void testExisteEListarArquivos() throws IOException {
        controller.salvar("slot1.xml", new SaveGame(1, "2026-10-12 10:00:00"));
        controller.salvar("slot2.xml", new SaveGame(2, "2026-10-12 11:00:00"));

        assertTrue(controller.existe("slot1.xml"));
        assertTrue(controller.existe("slot2.xml"));
        assertFalse(controller.existe("slot3.xml"));

        List<String> arquivos = controller.listarArquivos();
        assertEquals(2, arquivos.size());
        assertTrue(arquivos.contains("slot1.xml"));
        assertTrue(arquivos.contains("slot2.xml"));
    }

    @Test
    public void testExcluirArquivo() throws IOException {
        controller.salvar("temporario.xml", new SaveGame(1));
        assertTrue(controller.existe("temporario.xml"));

        boolean excluido = controller.excluir("temporario.xml");
        assertTrue(excluido);
        assertFalse(controller.existe("temporario.xml"));

        boolean excluidoNovamente = controller.excluir("temporario.xml");
        assertFalse(excluidoNovamente);
    }

    @Test
    public void testCarregarArquivoInexistenteLancaExcecao() {
        assertThrows(FileNotFoundException.class, () -> {
            controller.carregar("nao_existe.xml", SaveGame.class);
        });
    }

    @Test
    public void testArmazenamentoGenericoComOutroObjeto() throws IOException {
        ConfigExemplo config = new ConfigExemplo(80, "pt-BR");

        controller.salvar("config.xml", config);
        assertTrue(controller.existe("config.xml"));

        ConfigExemplo carregado = controller.carregar("config.xml", ConfigExemplo.class);
        assertNotNull(carregado);
        assertEquals(80, carregado.getVolume());
        assertEquals("pt-BR", carregado.getIdioma());
    }

    public static class ConfigExemplo {
        private int volume;
        private String idioma;

        public ConfigExemplo() {}

        public ConfigExemplo(int volume, String idioma) {
            this.volume = volume;
            this.idioma = idioma;
        }

        public int getVolume() { return volume; }
        public void setVolume(int volume) { this.volume = volume; }
        public String getIdioma() { return idioma; }
        public void setIdioma(String idioma) { this.idioma = idioma; }
    }
}
