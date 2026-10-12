package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.model.save.SaveGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários do SaveGameController.
 *
 * @author Anselmo e Adna
 */
public class SaveGameControllerTest {

    @TempDir
    Path tempDir;

    private SaveGameController controller;

    @BeforeEach
    public void setUp() {
        XmlFileController fileController = new XmlFileController(tempDir);
        controller = new SaveGameController(fileController);
    }

    @Test
    public void testSalvarManual() {
        boolean salvou = controller.salvar(1);
        assertTrue(salvou);
        assertTrue(controller.existe(1));

        SaveGame save = controller.carregar(1);
        assertNotNull(save);
        assertEquals(1, save.getVersao());
        assertFalse(save.isAutomatico());
        assertNotNull(save.getDataSalvamento());
    }

    @Test
    public void testAutoSaveNoSlot() {
        boolean salvou = controller.autoSave(2);
        assertTrue(salvou);
        assertTrue(controller.existe(2));

        SaveGame save = controller.carregar(2);
        assertNotNull(save);
        assertEquals(1, save.getVersao());
        assertTrue(save.isAutomatico());
    }

    @Test
    public void testListarSlotsOcupados() {
        controller.salvar(1);
        controller.autoSave(3);

        List<Integer> ocupados = controller.listarSlotsOcupados();
        assertEquals(2, ocupados.size());
        assertTrue(ocupados.contains(1));
        assertTrue(ocupados.contains(3));
        assertFalse(ocupados.contains(2));
        assertFalse(ocupados.contains(4));
    }

    @Test
    public void testListarSavesArquivos() {
        controller.salvar(1);
        controller.salvar(2);

        List<String> arquivos = controller.listarSaves();
        assertEquals(2, arquivos.size());
        assertTrue(arquivos.contains("save_game_1.xml"));
        assertTrue(arquivos.contains("save_game_2.xml"));
    }

    @Test
    public void testExcluirSlot() {
        controller.salvar(2);
        assertTrue(controller.existe(2));

        boolean excluiu = controller.excluir(2);
        assertTrue(excluiu);
        assertFalse(controller.existe(2));

        boolean excluiuNovamente = controller.excluir(2);
        assertFalse(excluiuNovamente);
    }

    @Test
    public void testSlotIdInvalidoLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> controller.salvar(0));
        assertThrows(IllegalArgumentException.class, () -> controller.salvar(5));
        assertThrows(IllegalArgumentException.class, () -> controller.carregar(-1));
        assertThrows(IllegalArgumentException.class, () -> controller.carregar(10));
        assertThrows(IllegalArgumentException.class, () -> controller.existe(0));
        assertThrows(IllegalArgumentException.class, () -> controller.excluir(99));
    }
}
