package com.exa863.anselmo_adna.controller.fx.modais;

import com.exa863.anselmo_adna.controller.fx.SceneManager;
import com.exa863.anselmo_adna.controller.fx.IconManager;
import com.exa863.anselmo_adna.view.fx.Icone;
import com.exa863.anselmo_adna.view.fx.Tela;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

/** Menu sobreposto ao jogo. ESC e CONTINUAR retomam exatamente do ponto pausado. */
public class MenuPausaController {

    @FXML private StackPane rootPane;
    @FXML private Button btnContinuar;
    @FXML private Button btnSalvar;
    @FXML private Button btnConfiguracoes;
    @FXML private Button btnVoltarMenu;

    @FXML
    public void initialize() {
        IconManager.aplicarBotao(btnContinuar, Icone.PLAY, 20);
        IconManager.aplicarBotao(btnSalvar, Icone.FLOPPY_DRIVE, 20);
        IconManager.aplicarBotao(btnConfiguracoes, Icone.GEAR, 20);
        IconManager.aplicarBotaoPerigo(btnVoltarMenu, Icone.HOME, 20);
        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                continuar();
                event.consume();
            }
        });
        Platform.runLater(btnContinuar::requestFocus);
    }

    @FXML
    public void continuar() {
        SceneManager.fecharMenuPausa();
    }

    @FXML
    public void abrirSaves() {
        SceneManager.abrirModal(Tela.SAVES);
    }

    @FXML
    public void abrirConfiguracoes() {
        SceneManager.abrirModal(Tela.CONFIGURACOES);
    }

    @FXML
    public void voltarAoMenu() {
        SceneManager.abrirConfirmacao(
                "VOLTAR AO MENU",
                "DESEJA INTERROMPER A HISTÓRIA E VOLTAR AO MENU?",
                "O PROGRESSO NÃO SALVO PODE SER PERDIDO.",
                "VOLTAR",
                () -> SceneManager.irPara(Tela.MENU_INICIAL)
        );
    }
}
