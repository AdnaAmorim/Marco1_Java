package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.exception.DadosInvalidosException;
import com.exa863.anselmo_adna.exception.FalhaCarregamentoException;
import com.exa863.anselmo_adna.exception.FalhaSalvamentoException;
import com.exa863.anselmo_adna.model.save.SaveGame;
import com.exa863.anselmo_adna.model.GameState;
import com.exa863.anselmo_adna.persistencia.Configuracao;
import com.exa863.anselmo_adna.persistencia.ConfiguracaoXML;

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
    private static final String NOME_AUTOSAVE = "autosave";
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

    public boolean salvar(int slotId, GameState gameState)
            throws FalhaSalvamentoException {

        return salvar(slotId, gameState, false);
    }

    public boolean autoSave(int slotId, GameState gameState)
            throws FalhaSalvamentoException {

        return salvar(slotId, gameState, true);
    }

    /** Lê a opção "Save automático" das configurações; se não for possível ler, mantém o padrão (ligado). */
    public boolean autoSaveHabilitado() {
        Configuracao config = new Configuracao();
        try {
            new ConfiguracaoXML().carregar(config);
        } catch (FalhaCarregamentoException | DadosInvalidosException e) {
            return true;
        }
        return config.autoSave;
    }

    public boolean autoSave(GameState gameState)
            throws FalhaSalvamentoException {

        return salvarArquivo(NOME_AUTOSAVE, "o salvamento automático", gameState, true);
    }

    public boolean salvar(int slotId, GameState gameState, boolean automatico)
            throws FalhaSalvamentoException {

        validarSlotId(slotId);
        return salvarArquivo(nomeSlot(slotId), "o Slot " + slotId, gameState, automatico);
    }

    private boolean salvarArquivo(String nome, String descricao, GameState gameState, boolean automatico)
            throws FalhaSalvamentoException {

        if (gameState == null || gameState.getPlayer() == null) {
            throw new IllegalArgumentException("Não é possível salvar uma partida sem protagonista.");
        }

        try {
            fileController.salvar(nome, new SaveGame(gameState, automatico));
            return true;

        } catch (IOException e) {
            throw new FalhaSalvamentoException(
                    "Não foi possível salvar a partida em " + descricao + "."
            );
        }
    }

    public SaveGame carregarAutomatico()
            throws FalhaCarregamentoException,
            DadosInvalidosException {

        return carregarArquivo(NOME_AUTOSAVE, "do salvamento automático");
    }

    public SaveGame carregar(int slotId)
            throws FalhaCarregamentoException,
            DadosInvalidosException {

        validarSlotId(slotId);
        return carregarArquivo(nomeSlot(slotId), "do Slot " + slotId);
    }

    private SaveGame carregarArquivo(String nome, String descricao)
            throws FalhaCarregamentoException,
            DadosInvalidosException {

        try {
            SaveGame save = fileController.carregar(nome, SaveGame.class);

            validarSave(save);

            return save;

        } catch (IOException e) {
            throw new FalhaCarregamentoException(
                    "Não foi possível carregar a partida " + descricao + "."
            );
        }
    }

    private void validarSave(SaveGame save)
            throws DadosInvalidosException {

        if (save == null) {
            throw new DadosInvalidosException("Os dados do save são inválidos.");
        }

        if (save.getVersao() <= 0 || save.getVersao() != SaveGame.VERSAO_ATUAL) {
            throw new DadosInvalidosException("A versão do save é inválida ou incompatível.");
        }

        if (save.getDataSalvamento() == null
                || save.getDataSalvamento().isBlank()) {
            throw new DadosInvalidosException("A data do save é inválida.");
        }

        if (save.getGameState() == null || save.getGameState().getPlayer() == null) {
            throw new DadosInvalidosException("O save não contém uma partida válida.");
        }
    }

    /** Entrega o estado pronto para GameController.carregarGameState ou seu construtor. */
    public GameState carregarGameState(int slotId)
            throws FalhaCarregamentoException,
            DadosInvalidosException {

        return carregar(slotId).getGameState();
    }

    /** Procura entre autosave e slots o save válido mais recente; saves ilegíveis são ignorados. */
    public SaveRecente carregarMaisRecente() {
        SaveRecente recente = null;
        LocalDateTime ultimaData = null;
        for (int slot = 0; slot <= MAX_SLOTS; slot++) {
            SaveGame save;
            try {
                save = slot == 0 ? carregarAutomatico() : carregar(slot);
            } catch (FalhaCarregamentoException | DadosInvalidosException e) {
                continue;
            }
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
        return fileController.existe(NOME_AUTOSAVE);
    }

    public boolean excluirAutomatico() {
        return fileController.excluir(NOME_AUTOSAVE);
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