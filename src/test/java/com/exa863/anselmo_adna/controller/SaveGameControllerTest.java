package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.model.save.SaveGame;
import com.exa863.anselmo_adna.model.GameState;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.character.Cores;
import com.exa863.anselmo_adna.model.character.Sexo;
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
    private GameState gameState;

    @BeforeEach
    public void setUp() {
        XmlFileController fileController = new XmlFileController(tempDir);
        controller = new SaveGameController(fileController);
        gameState = new GameState();
        gameState.setPlayer(new Player(1, "Rocky", "Boxeador", Cores.CASTANHO, Sexo.MASCULINO));
    }

    @Test
    public void testSalvarManual() throws Exception {
        boolean salvou = controller.salvar(1, gameState);
        assertTrue(salvou);
        assertTrue(controller.existe(1));

        SaveGame save = controller.carregar(1);
        assertNotNull(save);
        assertEquals(SaveGame.VERSAO_ATUAL, save.getVersao());
        assertFalse(save.isAutomatico());
        assertNotNull(save.getDataSalvamento());
    }

    @Test
    public void testAutoSaveNoSlot() throws Exception {
        boolean salvou = controller.autoSave(2, gameState);
        assertTrue(salvou);
        assertTrue(controller.existe(2));

        SaveGame save = controller.carregar(2);
        assertNotNull(save);
        assertEquals(SaveGame.VERSAO_ATUAL, save.getVersao());
        assertTrue(save.isAutomatico());
    }

    @Test
    public void testListarSlotsOcupados() throws Exception {
        controller.salvar(1, gameState);
        controller.autoSave(3, gameState);

        List<Integer> ocupados = controller.listarSlotsOcupados();
        assertEquals(2, ocupados.size());
        assertTrue(ocupados.contains(1));
        assertTrue(ocupados.contains(3));
        assertFalse(ocupados.contains(2));
        assertFalse(ocupados.contains(4));
    }

    @Test
    public void testListarSavesArquivos() throws Exception {
        controller.salvar(1, gameState);
        controller.salvar(2, gameState);

        List<String> arquivos = controller.listarSaves();
        assertEquals(2, arquivos.size());
        assertTrue(arquivos.contains("save_game_1.xml"));
        assertTrue(arquivos.contains("save_game_2.xml"));
    }

    @Test
    public void testExcluirSlot() throws Exception {
        controller.salvar(2, gameState);
        assertTrue(controller.existe(2));

        boolean excluiu = controller.excluir(2);
        assertTrue(excluiu);
        assertFalse(controller.existe(2));

        boolean excluiuNovamente = controller.excluir(2);
        assertFalse(excluiuNovamente);
    }

    @Test
    public void continuarSelecionaManualOuAutomaticoPelaData() throws Exception {
        XmlFileController arquivos = new XmlFileController(tempDir);
        assertNull(controller.carregarMaisRecente());
        SaveGame manual = new SaveGame(gameState);
        manual.setDataSalvamento("2026-10-18 12:00:00");
        arquivos.salvar("save_game_4", manual);
        SaveGame automatico = new SaveGame(gameState, true);
        automatico.setDataSalvamento("2026-10-18 11:00:00");
        arquivos.salvar("autosave", automatico);
        assertEquals(4, controller.carregarMaisRecente().slotId());
        assertEquals("Slot 4", controller.carregarMaisRecente().getNomeSlot());

        automatico.setDataSalvamento("2026-10-19 01:00:00");
        arquivos.salvar("autosave", automatico);
        assertEquals(0, controller.carregarMaisRecente().slotId());
        assertEquals("Autosave", controller.carregarMaisRecente().getNomeSlot());
        assertEquals("2026-10-19 01:00:00", controller.carregarMaisRecente().save().getDataSalvamento());
    }

    @Test
    public void continuarIgnoraSavesInvalidosEDataInvalida() throws Exception {
        XmlFileController arquivos = new XmlFileController(tempDir);
        SaveGame valido = new SaveGame(gameState);
        valido.setDataSalvamento("2026-10-18 10:00:00");
        arquivos.salvar("save_game_2", valido);
        SaveGame dataInvalida = new SaveGame(gameState);
        dataInvalida.setDataSalvamento("sem data");
        arquivos.salvar("autosave", dataInvalida);
        SaveGame incompativel = new SaveGame(gameState);
        incompativel.setVersao(999);
        incompativel.setDataSalvamento("2026-10-19 10:00:00");
        arquivos.salvar("save_game_1", incompativel);
        assertEquals(2, controller.carregarMaisRecente().slotId());
    }

    @Test
    public void testSlotIdInvalidoLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> controller.salvar(0, gameState));
        assertThrows(IllegalArgumentException.class, () -> controller.salvar(5, gameState));
        assertThrows(IllegalArgumentException.class, () -> controller.carregar(-1));
        assertThrows(IllegalArgumentException.class, () -> controller.carregar(10));
        assertThrows(IllegalArgumentException.class, () -> controller.existe(0));
        assertThrows(IllegalArgumentException.class, () -> controller.excluir(99));
    }
}
