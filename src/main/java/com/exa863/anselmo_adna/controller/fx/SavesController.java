package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SaveGameController;
import com.exa863.anselmo_adna.exception.DadosInvalidosException;
import com.exa863.anselmo_adna.exception.FalhaCarregamentoException;
import com.exa863.anselmo_adna.exception.FalhaSalvamentoException;
import com.exa863.anselmo_adna.model.GameState;
import com.exa863.anselmo_adna.model.save.SaveGame;
import com.exa863.anselmo_adna.view.fx.Icone;
import com.exa863.anselmo_adna.view.fx.Som;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controla o modal de gerenciamento de saves (carregar, salvar e excluir slots).
 *
 * @author Anselmo e Adna
 */
public class SavesController {

    private static final int SLOT_AUTOMATICO = 0;

    private final SaveGameController saveController = new SaveGameController();
    private final SaveGame[] detalhes = new SaveGame[SaveGameController.MAX_SLOTS + 1];

    private Button[] botoesSlot;
    private int slotSelecionado = 1;

    @FXML private Button btnSlot1;
    @FXML private Button btnSlot2;
    @FXML private Button btnSlot3;
    @FXML private Button btnSlot4;
    @FXML private Button btnAutosave;

    @FXML private Label lblSlotTitulo;
    @FXML private Label lblJogador;
    @FXML private Label lblCapitulo;
    @FXML private Label lblLocalData;
    @FXML private Label lblDataSalvamento;

    @FXML private Label lblMensagem;

    @FXML private Button btnCarregar;
    @FXML private Button btnSalvar;
    @FXML private Button btnExcluir;
    @FXML private Button btnVoltar;

    @FXML
    public void initialize() {
        botoesSlot = new Button[] { btnAutosave, btnSlot1, btnSlot2, btnSlot3, btnSlot4 };
        IconManager.aplicarBotao(btnCarregar, Icone.PLAY, 15);
        IconManager.aplicarBotao(btnSalvar, Icone.FLOPPY_DRIVE, 16);
        IconManager.aplicarBotaoPerigo(btnExcluir, Icone.EXCLAMATION_MARK, 16);
        IconManager.aplicarBotao(btnVoltar, Icone.LEFT_ARROW, 15);
        recarregarSlots();
        selecionarSlot(1, false);
    }

    @FXML public void selecionarSlot1() { selecionarSlot(1, true); }
    @FXML public void selecionarSlot2() { selecionarSlot(2, true); }
    @FXML public void selecionarSlot3() { selecionarSlot(3, true); }
    @FXML public void selecionarSlot4() { selecionarSlot(4, true); }
    @FXML public void selecionarAutosave() { selecionarSlot(SLOT_AUTOMATICO, true); }

    @FXML
    public void carregarSlot() {
        SaveGame save = detalhes[slotSelecionado];
        if (save == null || save.getGameState() == null) {
            AudioManager.tocar(Som.ERRO);
            mostrarMensagem(existeSlot(slotSelecionado)
                    ? "SAVE INVÁLIDO OU CORROMPIDO."
                    : "ESTE SLOT ESTÁ VAZIO.");
            return;
        }

        SceneManager.getGameController().carregarGameState(save.getGameState());
        atualizarBotoes();
        AudioManager.tocar(Som.CONFIRMAR);
        mostrarMensagem("PARTIDA CARREGADA: " + save.getGameState().getPlayer().getNome().toUpperCase() + ".");
    }

    @FXML
    public void salvarSlot() {
        if (slotSelecionado == SLOT_AUTOMATICO) {
            AudioManager.tocar(Som.ERRO);
            mostrarMensagem("O SLOT AUTOMÁTICO É GERENCIADO PELO JOGO.");
            return;
        }

        GameController gameController = SceneManager.getGameController();
        if (gameController.getPlayer() == null) {
            AudioManager.tocar(Som.ERRO);
            mostrarMensagem("INICIE UMA CARREIRA ANTES DE SALVAR.");
            return;
        }

        if (saveController.existe(slotSelecionado)) {
            SceneManager.abrirConfirmacao(
                    "SOBRESCREVER SAVE",
                    "SOBRESCREVER O SAVE DO SLOT " + slotSelecionado + "?",
                    "O SAVE ANTERIOR SERÁ SUBSTITUÍDO.",
                    "SOBRESCREVER",
                    this::executarSalvamento
            );
            return;
        }

        executarSalvamento();
    }

    @FXML
    public void excluirSlot() {
        if (!existeSlot(slotSelecionado)) {
            AudioManager.tocar(Som.ERRO);
            mostrarMensagem("ESTE SLOT JÁ ESTÁ VAZIO.");
            return;
        }

        String nome = slotSelecionado == SLOT_AUTOMATICO
                ? "O SALVAMENTO AUTOMÁTICO"
                : "O SAVE DO SLOT " + slotSelecionado;
        SceneManager.abrirConfirmacao(
                "EXCLUIR SAVE",
                "EXCLUIR DEFINITIVAMENTE " + nome + "?",
                "ESSA AÇÃO NÃO PODE SER DESFEITA.",
                "EXCLUIR",
                this::executarExclusao
        );
    }

    @FXML
    public void voltar() {
        SceneManager.fecharModal();
    }

    private void selecionarSlot(int slot, boolean tocarSom) {
        slotSelecionado = slot;
        if (tocarSom) {
            AudioManager.tocar(Som.SELECIONAR_SLOT);
        }
        atualizarSelecaoVisual();
        atualizarDetalhes();
        atualizarBotoes();
    }

    private void recarregarSlots() {
        detalhes[SLOT_AUTOMATICO] = lerAutomatico();
        for (int i = 1; i <= SaveGameController.MAX_SLOTS; i++) {
            detalhes[i] = lerSlot(i);
        }

        for (int i = 1; i <= SaveGameController.MAX_SLOTS; i++) {
            botoesSlot[i].setText(rotuloSlot("SLOT " + i, detalhes[i], saveController.existe(i)));
        }
        btnAutosave.setText(rotuloSlot("AUTO", detalhes[SLOT_AUTOMATICO], saveController.existeAutomatico()));
    }

    private void atualizarSelecaoVisual() {
        for (int i = 0; i < botoesSlot.length; i++) {
            botoesSlot[i].getStyleClass().remove("slot-selecionado");
            if (i == slotSelecionado) {
                botoesSlot[i].getStyleClass().add("slot-selecionado");
            }
        }
    }

    private void atualizarDetalhes() {
        boolean automatico = slotSelecionado == SLOT_AUTOMATICO;
        String titulo = automatico ? "SALVAMENTO AUTOMÁTICO" : "SLOT " + slotSelecionado;
        SaveGame save = detalhes[slotSelecionado];
        boolean existe = existeSlot(slotSelecionado);

        if (save == null) {
            lblSlotTitulo.setText(titulo + "  |  " + (existe ? "INDISPONÍVEL" : "VAZIO"));
            lblJogador.setText(existe
                    ? "SAVE INCOMPATÍVEL OU CORROMPIDO."
                    : "NENHUMA PARTIDA SALVA NESTE ESPAÇO.");
            lblCapitulo.setText("");
            lblLocalData.setText(existe
                    ? "USE EXCLUIR PARA LIBERAR ESTE SLOT."
                    : "SELECIONE OUTRO SLOT OU SALVE AQUI.");
            lblDataSalvamento.setText("");
            return;
        }

        GameState estado = save.getGameState();
        GameController gameController = SceneManager.getGameController();
        lblSlotTitulo.setText(titulo + "  |  SALVO");
        lblJogador.setText("JOGADOR: " + estado.getPlayer().getNome().toUpperCase());
        lblCapitulo.setText(gameController.getNarrativaController()
                .getCapituloAtual(estado.getPlayer()).toUpperCase());
        lblLocalData.setText((estado.getDataDia().getDiaFormatado()
                + " ÀS " + estado.getDataDia().getHoraFormatada()
                + " | " + estado.getLocalAtual().getNome()).toUpperCase());
        lblDataSalvamento.setText("SALVO EM: " + save.getDataSalvamento());
    }

    private void atualizarBotoes() {
        boolean existe = existeSlot(slotSelecionado);
        boolean valido = detalhes[slotSelecionado] != null;
        boolean temPartida = SceneManager.getGameController().getPlayer() != null;

        btnCarregar.setDisable(!valido);
        btnSalvar.setDisable(slotSelecionado == SLOT_AUTOMATICO || !temPartida);
        btnExcluir.setDisable(!existe);
    }

    private void executarSalvamento() {
        try {
            saveController.salvar(slotSelecionado, SceneManager.getGameController().getGameState());
            recarregarSlots();
            atualizarDetalhes();
            atualizarBotoes();
            AudioManager.tocar(Som.CONFIRMAR);
            mostrarMensagem("PARTIDA SALVA NO SLOT " + slotSelecionado + ".");
        } catch (FalhaSalvamentoException e) {
            AudioManager.tocar(Som.ERRO);
            mostrarMensagem(e.getMessage().toUpperCase());
        }
    }

    private void executarExclusao() {
        boolean excluido = slotSelecionado == SLOT_AUTOMATICO
                ? saveController.excluirAutomatico()
                : saveController.excluir(slotSelecionado);

        recarregarSlots();
        atualizarDetalhes();
        atualizarBotoes();
        AudioManager.tocar(excluido ? Som.CANCELAR : Som.ERRO);
        mostrarMensagem(excluido ? "SAVE EXCLUÍDO." : "NÃO FOI POSSÍVEL EXCLUIR O SAVE.");
    }

    private void mostrarMensagem(String texto) {
        lblMensagem.setText(texto);
    }

    private boolean existeSlot(int slot) {
        return slot == SLOT_AUTOMATICO ? saveController.existeAutomatico() : saveController.existe(slot);
    }

    private SaveGame lerSlot(int slot) {
        try {
            return saveController.carregar(slot);
        } catch (FalhaCarregamentoException | DadosInvalidosException e) {
            return null;
        }
    }

    private SaveGame lerAutomatico() {
        try {
            return saveController.carregarAutomatico();
        } catch (FalhaCarregamentoException | DadosInvalidosException e) {
            return null;
        }
    }

    private String rotuloSlot(String prefixo, SaveGame save, boolean existe) {
        if (save != null) {
            return prefixo + " - " + save.getGameState().getPlayer().getNome().toUpperCase();
        }
        return prefixo + " - " + (existe ? "INDISPONÍVEL" : "VAZIO");
    }
}
