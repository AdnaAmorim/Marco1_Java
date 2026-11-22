package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.view.fx.Icone;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controla o modal de créditos do jogo.
 *
 * @author Anselmo e Adna
 */
public class CreditosController {

    @FXML private Button btnVoltar;

    @FXML
    public void initialize() {
        IconManager.aplicarBotao(btnVoltar, Icone.LEFT_ARROW, 17);
    }

    @FXML
    public void voltarMenuInicial() {
        SceneManager.fecharModal();
    }
}

