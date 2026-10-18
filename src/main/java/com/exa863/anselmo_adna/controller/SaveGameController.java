package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.model.save.SaveGame;
import com.exa863.anselmo_adna.model.GameState;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Gerencia os slots de save do jogo.
 *
 * @author Anselmo e Adna
 */
public class SaveGameController {

    public static final String PASTA_SAVES_PADRAO = "saves";
    public static final int MAX_SLOTS = 4;
    private static final DateTimeFormatter DATA_SAVE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** Slot zero identifica o arquivo de autosave separado. */
    public record SaveRecente(int slotId, SaveGame save) {
        public String getNomeSlot() {
            return slotId == 0 ? "Autosave" : "Slot " + slotId;
        }
    }

    private final FileController fileController;

    public SaveGameController() {
        this(new XmlFileController(PASTA_SAVES_PADRAO));
    }

    public SaveGameController(String pastaSaves) {
        this(new XmlFileController(pastaSaves));
    }

    public SaveGameController(FileController fileController) {
        this.fileController = fileController;
    }

    public boolean salvar(int slotId, GameState gameState) {
        return salvar(slotId, gameState, false);
    }

    public boolean autoSave(int slotId, GameState gameState) {
        return salvar(slotId, gameState, true);
    }

    public boolean autoSave(GameState gameState) {
        return salvarArquivo("autosave", gameState, true);
    }

    public SaveGame carregarAutomatico() {
        return carregarArquivo("autosave");
    }

    public SaveRecente carregarMaisRecente() {
        SaveRecente recente = null;
        LocalDateTime ultimaData = null;
        for (int slot = 0; slot <= MAX_SLOTS; slot++) {
            SaveGame save = slot == 0 ? carregarAutomatico() : carregar(slot);
            if (save == null || save.getDataSalvamento() == null) continue;
            try {
                LocalDateTime data = LocalDateTime.parse(save.getDataSalvamento(), DATA_SAVE);
                if (ultimaData == null || data.isAfter(ultimaData)) {
                    recente = new SaveRecente(slot, save);
                    ultimaData = data;
                }
            } catch (DateTimeParseException e) {
                // Sem uma data válida, o save não participa da seleção automática.
            }
        }
        return recente;
    }

    public boolean existeAutomatico() {
        return fileController.existe("autosave");
    }

    public boolean excluirAutomatico() {
        return fileController.excluir("autosave");
    }

    public boolean salvar(int slotId, GameState gameState, boolean automatico) {
        validarSlotId(slotId);
        return salvarArquivo(nomeSlot(slotId), gameState, automatico);
    }

    private boolean salvarArquivo(String nome, GameState gameState, boolean automatico) {
        if (gameState == null || gameState.getPlayer() == null) {
            throw new IllegalArgumentException("Não é possível salvar uma partida sem protagonista.");
        }
        try {
            fileController.salvar(nome, new SaveGame(gameState, automatico));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public SaveGame carregar(int slotId) {
        validarSlotId(slotId);
        return carregarArquivo(nomeSlot(slotId));
    }

    private SaveGame carregarArquivo(String nome) {
        try {
            SaveGame save = fileController.carregar(nome, SaveGame.class);
            if (save == null || save.getVersao() != SaveGame.VERSAO_ATUAL
                    || save.getGameState() == null || save.getGameState().getPlayer() == null) {
                return null;
            }
            return save;
        } catch (IOException e) {
            return null;
        }
    }

    /** Entrega o estado pronto para GameController.carregarGameState ou seu construtor. */
    public GameState carregarGameState(int slotId) {
        SaveGame save = carregar(slotId);
        return save != null ? save.getGameState() : null;
    }

    public boolean existe(int slotId) {
        validarSlotId(slotId);
        return fileController.existe(nomeSlot(slotId));
    }

    public boolean excluir(int slotId) {
        validarSlotId(slotId);
        return fileController.excluir(nomeSlot(slotId));
    }

    public List<Integer> listarSlotsOcupados() {
        List<Integer> slotsOcupados = new ArrayList<>();
        for (int i = 1; i <= MAX_SLOTS; i++) {
            if (existe(i)) {
                slotsOcupados.add(i);
            }
        }
        return slotsOcupados;
    }

    public List<String> listarSaves() {
        return fileController.listarArquivos();
    }

    public String getPastaSaves() {
        return fileController.getPastaBase().toString();
    }

    public void validarSlotId(int slotId) {
        if (slotId < 1 || slotId > MAX_SLOTS) {
            throw new IllegalArgumentException(
                    "Slot inválido: " + slotId + ". Escolha um slot entre 1 e " + MAX_SLOTS + "."
            );
        }
    }

    private String nomeSlot(int slotId) {
        return "save_game_" + slotId;
    }
}
