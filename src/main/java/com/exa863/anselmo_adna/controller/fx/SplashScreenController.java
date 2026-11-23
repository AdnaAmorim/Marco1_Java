package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.view.fx.Musica;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;

/**
 * Controla a animação de abertura do jogo e a transição da logo até o menu inicial.
 *
 * @author Anselmo e Adna
 */
public class SplashScreenController {

    /** Caminho logo. */
    private static final String CAMINHO_LOGO = "/images/logo.png";

    /** Painel principal da tela. */
    @FXML private StackPane rootPane;
    /** Fundo preto. */
    @FXML private Region fundoPreto;
    /** Logo wrapper. */
    @FXML private StackPane logoWrapper;
    /** Imagem — logo. */
    @FXML private ImageView imgLogo;
    /** Texto exibido — made by. */
    @FXML private Label lblMadeBy;

    /** Logo menu. */
    private Node logoMenu;
    /** Indica se a abertura já foi concluída. */
    private boolean finalizada;

    /** Prepara os componentes da tela após o carregamento do FXML. */
    @FXML
    public void initialize() {
        carregarLogo();
        iniciarAnimacao();
    }

    /** Carrega logo. */
    private void carregarLogo() {
        URL recurso = getClass().getResource(CAMINHO_LOGO);
        if (recurso != null) {
            imgLogo.setImage(new Image(recurso.toExternalForm()));
        }
    }

    /** Inicia animação. */
    private void iniciarAnimacao() {
        imgLogo.setScaleX(0.75);
        imgLogo.setScaleY(0.75);

        PauseTransition esperaAudio = new PauseTransition(Duration.millis(500));
        esperaAudio.setOnFinished(e -> AudioManager.tocarUmaVez(Musica.INTRO));

        PauseTransition esperaLogo = new PauseTransition(Duration.millis(350));

        FadeTransition fadeLogo = new FadeTransition(Duration.millis(850), imgLogo);
        fadeLogo.setFromValue(0.0);
        fadeLogo.setToValue(1.0);

        ScaleTransition crescerLogo = new ScaleTransition(Duration.millis(850), imgLogo);
        crescerLogo.setFromX(0.75);
        crescerLogo.setFromY(0.75);
        crescerLogo.setToX(1.0);
        crescerLogo.setToY(1.0);
        crescerLogo.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition fadeMadeBy = new FadeTransition(Duration.millis(650), lblMadeBy);
        fadeMadeBy.setFromValue(0.0);
        fadeMadeBy.setToValue(1.0);
        fadeMadeBy.setDelay(Duration.millis(450));

        ParallelTransition entradaConteudo = new ParallelTransition(fadeLogo, crescerLogo, fadeMadeBy);
        PauseTransition pausaVisivel = new PauseTransition(Duration.seconds(1.2));

        SequentialTransition abertura = new SequentialTransition(
                esperaAudio,
                esperaLogo,
                entradaConteudo,
                pausaVisivel
        );
        abertura.setOnFinished(e -> iniciarTransicaoParaMenu());
        abertura.play();
    }

    /** Inicia transição da abertura para o menu. */
    private void iniciarTransicaoParaMenu() {
        double deltaX = 0.0;
        double deltaY = -(rootPane.getHeight() * 0.275);

        logoMenu = buscarLogoDoMenu();
        if (logoMenu != null) {
            logoMenu.setOpacity(0.0);
            // Os dois logos pertencem a painéis diferentes. A posição na cena permite alinhar seus centros.
            Bounds origem = imgLogo.localToScene(imgLogo.getBoundsInLocal());
            Bounds destino = logoMenu.localToScene(logoMenu.getBoundsInLocal());
            deltaX = (destino.getMinX() + destino.getWidth() / 2.0) - (origem.getMinX() + origem.getWidth() / 2.0);
            deltaY = (destino.getMinY() + destino.getHeight() / 2.0) - (origem.getMinY() + origem.getHeight() / 2.0);
        }

        FadeTransition fadeMadeBy = new FadeTransition(Duration.millis(450), lblMadeBy);
        fadeMadeBy.setFromValue(lblMadeBy.getOpacity());
        fadeMadeBy.setToValue(0.0);

        FadeTransition fadeFundo = new FadeTransition(Duration.millis(1300), fundoPreto);
        fadeFundo.setFromValue(1.0);
        fadeFundo.setToValue(0.0);
        fadeFundo.setInterpolator(Interpolator.EASE_BOTH);

        TranslateTransition moverLogo = new TranslateTransition(Duration.millis(1150), logoWrapper);
        moverLogo.setByX(deltaX);
        moverLogo.setByY(deltaY);
        moverLogo.setInterpolator(Interpolator.SPLINE(0.25, 0.1, 0.25, 1.0));

        PauseTransition disparoMusica = new PauseTransition(Duration.millis(550));
        disparoMusica.setOnFinished(e -> AudioManager.tocarMusica(Musica.MENU));

        ParallelTransition transicao = new ParallelTransition(fadeMadeBy, fadeFundo, moverLogo, disparoMusica);
        transicao.setOnFinished(e -> concluirAbertura());
        transicao.play();
    }

    /** Busca logo na tela do menu. */
    private Node buscarLogoDoMenu() {
        if (rootPane.getParent() instanceof StackPane container && !container.getChildren().isEmpty()) {
            Node menu = container.getChildren().get(0);
            Node logoContainer = menu.lookup("#logoContainer");
            return logoContainer != null ? logoContainer : menu.lookup("#imgLogo");
        }
        return null;
    }

    /** Conclui abertura. */
    private void concluirAbertura() {
        if (finalizada) {
            return;
        }
        finalizada = true;
        if (logoMenu != null) {
            logoMenu.setOpacity(1.0);
        }
        AudioManager.tocarMusica(Musica.MENU);
        SceneManager.fecharModal(false);
    }
}
