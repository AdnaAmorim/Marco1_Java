package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.model.save.SaveGame;

import java.io.IOException;
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

    public boolean salvar(int slotId) {
        return salvar(slotId, false);
    }

    public boolean autoSave(int slotId) {
        return salvar(slotId, true);
    }

    public boolean salvar(int slotId, boolean automatico) {
        validarSlotId(slotId);
        try {
            fileController.salvar(nomeSlot(slotId), new SaveGame(1, automatico));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public SaveGame carregar(int slotId) {
        validarSlotId(slotId);
        try {
            return fileController.carregar(nomeSlot(slotId), SaveGame.class);
        } catch (IOException e) {
            return null;
        }
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
