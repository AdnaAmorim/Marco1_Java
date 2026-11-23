package com.exa863.anselmo_adna;

import com.exa863.anselmo_adna.controller.fx.SceneManager;
import com.exa863.anselmo_adna.view.fx.Tela;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Configura a janela principal do JavaFX e abre o menu inicial.
 *
 * @author Anselmo e Adna
 */
public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) {
        Font.loadFont(getClass().getResourceAsStream("/fonts/upheavtt.ttf"), 28);

        URL icone = getClass().getResource("/images/icon.png");
        if (icone != null) {
            stage.getIcons().add(new Image(icone.toExternalForm()));
        }

        stage.setTitle("Boxing Game");
        stage.setResizable(true);
        stage.setMinWidth(640);
        stage.setMinHeight(360);

        // O botão de fechar da janela usa a mesma confirmação de saída do menu.
        stage.setOnCloseRequest(event -> {
            event.consume();
            SceneManager.solicitarSaida();
        });

        SceneManager.inicializarComAbertura(stage, Tela.SPLASH_SCREEN, Tela.MENU_INICIAL);
        stage.show();
    }

    public static void iniciar(String[] args) {
        launch(Application.class, args);
    }
}
