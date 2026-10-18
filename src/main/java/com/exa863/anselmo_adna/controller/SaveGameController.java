package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.exception.DadosInvalidosException;
import com.exa863.anselmo_adna.exception.FalhaCarregamentoException;
import com.exa863.anselmo_adna.exception.FalhaSalvamentoException;
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


    public boolean salvar(int slotId)
            throws FalhaSalvamentoException {

        return salvar(slotId, false);
    }


    public boolean autoSave(int slotId)
            throws FalhaSalvamentoException {

        return salvar(slotId, true);
    }


    public boolean salvar(int slotId, boolean automatico)
            throws FalhaSalvamentoException {

        validarSlotId(slotId);

        try {

            fileController.salvar(
                    nomeSlot(slotId),
                    new SaveGame(1, automatico)
            );

            return true;

        } catch (IOException e) {

            throw new FalhaSalvamentoException(
                    "Não foi possível salvar a partida no Slot " + slotId + "."
            );
        }
    }


    public SaveGame carregar(int slotId)
            throws FalhaCarregamentoException,
            DadosInvalidosException {

        validarSlotId(slotId);

        try {

            SaveGame save = fileController.carregar(
                    nomeSlot(slotId),
                    SaveGame.class
            );

            validarSave(save);

            return save;

        } catch (IOException e) {

            throw new FalhaCarregamentoException(
                    "Não foi possível carregar a partida do Slot " + slotId + "."
            );
        }
    }


    private void validarSave(SaveGame save)
            throws DadosInvalidosException {

        if (save == null) {

            throw new DadosInvalidosException(
                    "Os dados do save são inválidos."
            );
        }

        if (save.getVersao() <= 0) {

            throw new DadosInvalidosException(
                    "A versão do save é inválida."
            );
        }

        if (save.getDataSalvamento() == null
                || save.getDataSalvamento().isBlank()) {

            throw new DadosInvalidosException(
                    "A data do save é inválida."
            );
        }
    }


    public boolean existe(int slotId) {

        validarSlotId(slotId);

        return fileController.existe(
                nomeSlot(slotId)
        );
    }


    public boolean excluir(int slotId) {

        validarSlotId(slotId);

        return fileController.excluir(
                nomeSlot(slotId)
        );
    }


    public List<Integer> listarSlotsOcupados() {

        List<Integer> slotsOcupados =
                new ArrayList<>();

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

        return fileController
                .getPastaBase()
                .toString();
    }


    public void validarSlotId(int slotId) {

        if (slotId < 1 || slotId > MAX_SLOTS) {

            throw new IllegalArgumentException(
                    "Slot inválido: "
                            + slotId
                            + ". Escolha um slot entre 1 e "
                            + MAX_SLOTS
                            + "."
            );
        }
    }


    private String nomeSlot(int slotId) {

        return "save_game_" + slotId;
    }
}