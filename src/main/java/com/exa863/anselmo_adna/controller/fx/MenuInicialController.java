package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.view.fx.Icone;
import com.exa863.anselmo_adna.view.fx.Tela;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;

/**
 * Controla as ações dos botões do menu inicial, o vídeo de fundo e o brilho da logo.
 *
 * @author Anselmo e Adna
 */
public class MenuInicialController {

    private static final String VIDEO_FUNDO = "/videos/background.mp4";

    @FXML private StackPane rootPane;
    @FXML private MediaView bgMediaView;
    @FXML private StackPane logoPulse;
    @FXML private ImageView imgLogo;
    @FXML private StackPane shineLayer;
    @FXML private Button btnContinuar;
    @FXML private Button btnNovoJogo;
    @FXML private Button btnCarregar;
    @FXML private Button btnTutorial;
    @FXML private Button btnOpcoes;
    @FXML private Button btnCreditos;
    @FXML private Button btnSair;

    private MediaPlayer mediaPlayer;
    private SequentialTransition animacaoBrilho;
    private ScaleTransition animacaoPulso;

    @FXML
    public void initialize() {
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(rootPane.widthProperty());
        clip.heightProperty().bind(rootPane.heightProperty());
        rootPane.setClip(clip);

        configurarIcones();
        iniciarVideoFundo();
        iniciarBrilhoLogo();
        iniciarPulsoLogo();
    }

    private void configurarIcones() {
        IconManager.aplicarBotao(btnContinuar, Icone.PLAY, 16);
        IconManager.aplicarBotao(btnNovoJogo, Icone.GAME_CONTROLLER, 18);
        IconManager.aplicarBotao(btnCarregar, Icone.FLOPPY_DRIVE, 17);
        IconManager.aplicarBotao(btnTutorial, Icone.QUESTION_MARK, 17);
        IconManager.aplicarBotao(btnOpcoes, Icone.GEAR, 17);
        IconManager.aplicarBotao(btnCreditos, Icone.STAR, 17);
        IconManager.aplicarBotao(btnSair, Icone.EXIT, 17);
    }

    private void iniciarPulsoLogo() {
        if (logoPulse == null) {
            return;
        }
        animacaoPulso = new ScaleTransition(Duration.seconds(2.2), logoPulse);
        animacaoPulso.setFromX(1.0);
        animacaoPulso.setFromY(1.0);
        animacaoPulso.setToX(1.035);
        animacaoPulso.setToY(1.035);
        animacaoPulso.setAutoReverse(true);
        animacaoPulso.setCycleCount(Animation.INDEFINITE);
        animacaoPulso.setInterpolator(Interpolator.EASE_BOTH);
        animacaoPulso.play();
    }

    private void iniciarBrilhoLogo() {
        if (imgLogo == null || imgLogo.getImage() == null || shineLayer == null) {
            return;
        }

        double largura = imgLogo.getBoundsInLocal().getWidth();
        double altura = imgLogo.getBoundsInLocal().getHeight();
        if (largura <= 0 || altura <= 0) {
            return;
        }

        shineLayer.setMinSize(0, 0);
        shineLayer.setPrefSize(largura, altura);
        shineLayer.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        shineLayer.setBlendMode(BlendMode.ADD);

        ImageView mascaraLogo = new ImageView(imgLogo.getImage());
        mascaraLogo.setFitWidth(imgLogo.getFitWidth());
        mascaraLogo.setFitHeight(imgLogo.getFitHeight());
        mascaraLogo.setPreserveRatio(true);
        mascaraLogo.setSmooth(false);
        shineLayer.setClip(mascaraLogo);

        double faixaLargura = largura * 0.28;
        double faixaAltura = altura * 1.7;
        Rectangle faixaBrilho = new Rectangle(faixaLargura, faixaAltura);
        faixaBrilho.setManaged(false);
        faixaBrilho.setLayoutX((largura - faixaLargura) / 2.0);
        faixaBrilho.setLayoutY((altura - faixaAltura) / 2.0);
        faixaBrilho.setRotate(-31.0);
        faixaBrilho.setFill(new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0.00, Color.rgb(255, 235, 179, 0.0)),
                new Stop(0.28, Color.rgb(255, 235, 179, 0.35)),
                new Stop(0.45, Color.rgb(255, 240, 195, 0.85)),
                new Stop(0.50, Color.rgb(255, 248, 220, 1.0)),
                new Stop(0.55, Color.rgb(255, 240, 195, 0.85)),
                new Stop(0.72, Color.rgb(255, 235, 179, 0.35)),
                new Stop(1.00, Color.rgb(255, 235, 179, 0.0))
        ));

        double alcance = largura * 0.85;
        faixaBrilho.setTranslateX(-alcance);
        shineLayer.getChildren().setAll(faixaBrilho);

        TranslateTransition varredura = new TranslateTransition(Duration.millis(1100), faixaBrilho);
        varredura.setFromX(-alcance);
        varredura.setToX(alcance);
        varredura.setInterpolator(Interpolator.LINEAR);

        PauseTransition intervalo = new PauseTransition(Duration.millis(1500));

        animacaoBrilho = new SequentialTransition(varredura, intervalo);
        animacaoBrilho.setCycleCount(Animation.INDEFINITE);
        animacaoBrilho.play();
    }

    private void iniciarVideoFundo() {
        URL recurso = getClass().getResource(VIDEO_FUNDO);
        if (recurso == null || bgMediaView == null) {
            return;
        }

        Media media = new Media(recurso.toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setMute(true);
        mediaPlayer.setVolume(0.0);
        mediaPlayer.setAutoPlay(true);
        bgMediaView.setMediaPlayer(mediaPlayer);

        media.widthProperty().addListener((obs, oldVal, newVal) -> ajustarVideo());
        media.heightProperty().addListener((obs, oldVal, newVal) -> ajustarVideo());
        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> ajustarVideo());
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> ajustarVideo());
        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene == null) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                }
                if (animacaoBrilho != null) {
                    animacaoBrilho.stop();
                }
                if (animacaoPulso != null) {
                    animacaoPulso.stop();
                }
            }
        });

        mediaPlayer.setOnReady(() -> Platform.runLater(this::ajustarVideo));
        mediaPlayer.play();
    }

    private void ajustarVideo() {
        if (mediaPlayer == null || mediaPlayer.getMedia() == null) {
            return;
        }
        double width = rootPane.getWidth();
        double height = rootPane.getHeight();
        double videoW = mediaPlayer.getMedia().getWidth();
        double videoH = mediaPlayer.getMedia().getHeight();
        if (width <= 0 || height <= 0 || videoW <= 0 || videoH <= 0) {
            return;
        }

        double scale = Math.max(width / videoW, height / videoH);
        double drawW = videoW * scale;
        double drawH = videoH * scale;
        bgMediaView.setFitWidth(drawW);
        bgMediaView.setFitHeight(drawH);
        bgMediaView.setLayoutX((width - drawW) / 2.0);
        bgMediaView.setLayoutY(0.0);
    }

    @FXML
    public void abrirSaves() {
        SceneManager.abrirModal(Tela.SAVES);
    }

    @FXML
    public void abrirTutorial() {
        SceneManager.abrirModal(Tela.TUTORIAL);
    }

    @FXML
    public void abrirConfiguracoes() {
        SceneManager.abrirModal(Tela.CONFIGURACOES);
    }

    @FXML
    public void abrirCreditos() {
        SceneManager.abrirModal(Tela.CREDITOS);
    }

    @FXML
    public void sairJogo() {
        SceneManager.solicitarSaida();
    }
}

