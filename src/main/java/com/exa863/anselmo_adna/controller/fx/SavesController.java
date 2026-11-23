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
import com.exa863.anselmo_adna.view.fx.Tela;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controla o modal de gerenciamento de saves (carregar, salvar e excluir slots).
 *
 * @author Anselmo e Adna
 */
public class SavesController {

    /** Slot automático. */
    private static final int SLOT_AUTOMATICO = 0;

    /** Controlador responsável pelos arquivos de partida. */
    private final SaveGameController saveController = new SaveGameController();
    /** Detalhes. */
    private final SaveGame[] detalhes = new SaveGame[SaveGameController.MAX_SLOTS + 1];

    /** Botoes slot. */
    private Button[] botoesSlot;
    /** Slot selecionado. */
    private int slotSelecionado = 1;

    /** Botão — slot 1. */
    @FXML private Button btnSlot1;
    /** Botão — slot 2. */
    @FXML private Button btnSlot2;
    /** Botão — slot 3. */
    @FXML private Button btnSlot3;
    /** Botão — slot 4. */
    @FXML private Button btnSlot4;
    /** Botão de seleção do slot automático. */
    @FXML private Button btnAutosave;

    /** Texto — título do slot. */
    @FXML private Label lblSlotTitulo;
    /** Texto — jogador. */
    @FXML private Label lblJogador;
    /** Texto — capítulo. */
    @FXML private Label lblCapitulo;
    /** Texto — local e data. */
    @FXML private Label lblLocalData;
    /** Texto exibido — data salvamento. */
    @FXML private Label lblDataSalvamento;

    /** Texto exibido — mensagem. */
    @FXML private Label lblMensagem;

    /** Botão — carregar. */
    @FXML private Button btnCarregar;
    /** Botão — salvar. */
    @FXML private Button btnSalvar;
    /** Botão — excluir. */
    @FXML private Button btnExcluir;
    /** Botão — voltar. */
    @FXML private Button btnVoltar;

    /** Prepara os componentes da tela após o carregamento do FXML. */
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

    /** Seleciona — slot 1. */
    @FXML public void selecionarSlot1() { selecionarSlot(1, true); }
    /** Seleciona — slot 2. */
    @FXML public void selecionarSlot2() { selecionarSlot(2, true); }
    /** Seleciona — slot 3. */
    @FXML public void selecionarSlot3() { selecionarSlot(3, true); }
    /** Seleciona — slot 4. */
    @FXML public void selecionarSlot4() { selecionarSlot(4, true); }
    /** Seleciona autosave. */
    @FXML public void selecionarAutosave() { selecionarSlot(SLOT_AUTOMATICO, true); }

    /** Carrega slot selecionado. */
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
        AudioManager.tocar(Som.CONFIRMAR);
        SceneManager.irPara(Tela.MAPA);
    }

    /** Salva slot selecionado. */
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

    /** Exclui slot selecionado. */
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

    /** Retorna à tela anterior. */
    @FXML
    public void voltar() {
        SceneManager.fecharModal();
    }

    /** Seleciona slot selecionado. */
    private void selecionarSlot(int slot, boolean tocarSom) {
        slotSelecionado = slot;
        if (tocarSom) {
            AudioManager.tocar(Som.SELECIONAR_SLOT);
        }
        atualizarSelecaoVisual();
        atualizarDetalhes();
        atualizarBotoes();
    }

    /** Recarrega slots. */
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

    /** Atualiza destaque do slot selecionado. */
    private void atualizarSelecaoVisual() {
        for (int i = 0; i < botoesSlot.length; i++) {
            botoesSlot[i].getStyleClass().remove("slot-selecionado");
            if (i == slotSelecionado) {
                botoesSlot[i].getStyleClass().add("slot-selecionado");
            }
        }
    }

    /** Atualiza detalhes. */
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

    /** Permite excluir um arquivo ilegível, mas só habilita o carregamento de uma partida válida. */
    private void atualizarBotoes() {
        boolean existe = existeSlot(slotSelecionado);
        boolean valido = detalhes[slotSelecionado] != null;
        boolean temPartida = SceneManager.getGameController().getPlayer() != null;

        btnCarregar.setDisable(!valido);
        btnSalvar.setDisable(slotSelecionado == SLOT_AUTOMATICO || !temPartida);
        btnExcluir.setDisable(!existe);
    }

    /** Executa salvamento. */
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

    /** Executa exclusao. */
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

    /** Mostra mensagem. */
    private void mostrarMensagem(String texto) {
        lblMensagem.setText(texto);
    }

    /** Verifica se há um arquivo de partida no slot. */
    private boolean existeSlot(int slot) {
        return slot == SLOT_AUTOMATICO ? saveController.existeAutomatico() : saveController.existe(slot);
    }

    /** Lê slot selecionado. */
    private SaveGame lerSlot(int slot) {
        try {
            return saveController.carregar(slot);
        } catch (FalhaCarregamentoException | DadosInvalidosException e) {
            return null;
        }
    }

    /** Lê partida do slot automático. */
    private SaveGame lerAutomatico() {
        try {
            return saveController.carregarAutomatico();
        } catch (FalhaCarregamentoException | DadosInvalidosException e) {
            return null;
        }
    }

    /** Monta o rótulo do slot conforme a situação do arquivo. */
    private String rotuloSlot(String prefixo, SaveGame save, boolean existe) {
        if (save != null) {
            return prefixo + " - " + save.getGameState().getPlayer().getNome().toUpperCase();
        }
        return prefixo + " - " + (existe ? "INDISPONÍVEL" : "VAZIO");
    }
}
