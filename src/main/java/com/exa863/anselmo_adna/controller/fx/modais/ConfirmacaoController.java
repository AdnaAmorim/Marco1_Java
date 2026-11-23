package com.exa863.anselmo_adna.controller.fx.modais;

import com.exa863.anselmo_adna.controller.fx.AudioManager;
import com.exa863.anselmo_adna.controller.fx.IconManager;
import com.exa863.anselmo_adna.controller.fx.SceneManager;
import com.exa863.anselmo_adna.view.fx.Icone;
import com.exa863.anselmo_adna.view.fx.Som;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Modal genérico de confirmação reutilizável em qualquer tela.
 *
 * @author Anselmo e Adna
 */
public class ConfirmacaoController {

    /** Texto — título. */
    @FXML private Label lblTitulo;
    /** Texto — mensagem. */
    @FXML private Label lblMensagem;
    /** Texto — aviso. */
    @FXML private Label lblAviso;
    /** Botão — confirmar. */
    @FXML private Button btnConfirmar;
    /** Botão — cancelar. */
    @FXML private Button btnCancelar;

    /** Ação executada quando o jogador confirma. */
    private Runnable aoConfirmar;

    /** Prepara os componentes da tela após o carregamento do FXML. */
    @FXML
    public void initialize() {
        IconManager.aplicarBotaoPerigo(btnConfirmar, Icone.EXCLAMATION_MARK, 16);
        IconManager.aplicarBotao(btnCancelar, Icone.LEFT_ARROW, 16);
    }

    /** Define os textos e a ação da confirmação. */
    public void configurar(String titulo, String mensagem, String aviso,
                           String textoConfirmar, Runnable aoConfirmar) {
        lblTitulo.setText(titulo);
        lblMensagem.setText(mensagem);

        boolean temAviso = aviso != null && !aviso.isBlank();
        lblAviso.setText(temAviso ? aviso : "");
        lblAviso.setVisible(temAviso);
        lblAviso.setManaged(temAviso);

        if (textoConfirmar != null && !textoConfirmar.isBlank()) {
            btnConfirmar.setText(textoConfirmar);
        }
        this.aoConfirmar = aoConfirmar;
    }

    /** Confirma a seleção atual. */
    @FXML
    public void confirmar() {
        AudioManager.tocar(Som.CONFIRMAR);
        // Fecha a confirmação antes da ação, que pode abrir outro modal ou trocar a tela.
        SceneManager.fecharModal(false);
        if (aoConfirmar != null) {
            aoConfirmar.run();
        }
    }

    /** Descarta a ação pendente e fecha o painel. */
    @FXML
    public void cancelar() {
        SceneManager.fecharModal();
    }
}
