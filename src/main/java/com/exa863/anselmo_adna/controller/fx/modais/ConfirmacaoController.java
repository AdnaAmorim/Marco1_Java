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

    @FXML private Label lblTitulo;
    @FXML private Label lblMensagem;
    @FXML private Label lblAviso;
    @FXML private Button btnConfirmar;
    @FXML private Button btnCancelar;

    private Runnable aoConfirmar;

    @FXML
    public void initialize() {
        IconManager.aplicarBotaoPerigo(btnConfirmar, Icone.EXCLAMATION_MARK, 16);
        IconManager.aplicarBotao(btnCancelar, Icone.LEFT_ARROW, 16);
    }

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

    @FXML
    public void confirmar() {
        AudioManager.tocar(Som.CONFIRMAR);
        SceneManager.fecharModal(false);
        if (aoConfirmar != null) {
            aoConfirmar.run();
        }
    }

    @FXML
    public void cancelar() {
        SceneManager.fecharModal();
    }
}
