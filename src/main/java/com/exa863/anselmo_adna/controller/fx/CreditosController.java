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

    /** Botão — voltar. */
    @FXML private Button btnVoltar;

    /** Prepara os componentes da tela após o carregamento do FXML. */
    @FXML
    public void initialize() {
        IconManager.aplicarBotao(btnVoltar, Icone.LEFT_ARROW, 17);
    }

    /** Fecha os créditos para revelar o menu inicial. */
    @FXML
    public void voltarMenuInicial() {
        SceneManager.fecharModal();
    }
}

