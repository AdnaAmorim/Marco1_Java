package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.model.GameState;
import com.exa863.anselmo_adna.model.character.Cores;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.character.Sexo;
import com.exa863.anselmo_adna.view.fx.Som;
import com.exa863.anselmo_adna.view.fx.Tela;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

/** Versão JavaFX da criação de personagem existente na interface de terminal. */
public class CriacaoPersonagemController {

    private static final double LARGURA_BASE = 1280.0;
    private static final double ALTURA_BASE = 760.0;
    private static final double ZOOM_HOVER = 3.1;
    private static final double DESLOCAMENTO_ROSTO_Y = 345.0;

    @FXML private StackPane rootPane;
    @FXML private VBox conteudoContainer;
    @FXML private StackPane palcoPreview;
    @FXML private TextField txtNome;
    @FXML private ComboBox<Cores> cmbOlhos;
    @FXML private VBox cardMasculino;
    @FXML private VBox cardFeminino;
    @FXML private Label lblNomePreview;
    @FXML private Label lblSexoPreview;
    @FXML private Label lblOlhosPreview;
    @FXML private Label lblMensagem;
    @FXML private Button btnConfirmar;
    @FXML private ImageView imgMasculino;
    @FXML private ImageView imgFeminino;
    @FXML private ImageView imgPersonagemPrincipal;

    private final Map<Sexo, Map<Cores, Image>> imagens = new EnumMap<>(Sexo.class);

    private Sexo sexoSelecionado = Sexo.MASCULINO;
    private ParallelTransition animacaoZoom;

    @FXML
    public void initialize() {
        configurarEscalaResponsiva();
        configurarZoomPreview();
        cmbOlhos.setItems(FXCollections.observableArrayList(Cores.values()));
        cmbOlhos.getSelectionModel().select(Cores.CASTANHO);
        cmbOlhos.valueProperty().addListener((obs, antes, depois) -> atualizarPreview());
        txtNome.textProperty().addListener((obs, antes, depois) -> atualizarPreview());
        selecionarMasculino();
        txtNome.setOnAction(event -> confirmar());
        btnConfirmar.setDefaultButton(true);
    }

    private void configurarZoomPreview() {
        if (palcoPreview == null || imgPersonagemPrincipal == null) {
            return;
        }
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(palcoPreview.widthProperty());
        clip.heightProperty().bind(palcoPreview.heightProperty());
        palcoPreview.setClip(clip);

        palcoPreview.hoverProperty().addListener((obs, antigo, hover) -> animarZoomPreview(hover));
    }

    private void animarZoomPreview(boolean aproximar) {
        if (animacaoZoom != null) {
            animacaoZoom.stop();
        }
        Duration duracao = Duration.millis(220);

        ScaleTransition escala = new ScaleTransition(duracao, imgPersonagemPrincipal);
        escala.setToX(aproximar ? ZOOM_HOVER : 1.0);
        escala.setToY(aproximar ? ZOOM_HOVER : 1.0);
        escala.setInterpolator(Interpolator.EASE_OUT);

        TranslateTransition mover = new TranslateTransition(duracao, imgPersonagemPrincipal);
        mover.setToY(aproximar ? DESLOCAMENTO_ROSTO_Y : 0.0);
        mover.setInterpolator(Interpolator.EASE_OUT);

        animacaoZoom = new ParallelTransition(escala, mover);
        animacaoZoom.play();
    }

    private void configurarEscalaResponsiva() {
        DoubleBinding escala = Bindings.createDoubleBinding(() -> {
            double largura = rootPane.getWidth();
            double altura = rootPane.getHeight();
            if (largura <= 0 || altura <= 0) {
                return 1.0;
            }
            return Math.min(largura / LARGURA_BASE, altura / ALTURA_BASE);
        }, rootPane.widthProperty(), rootPane.heightProperty());

        conteudoContainer.scaleXProperty().bind(escala);
        conteudoContainer.scaleYProperty().bind(escala);
    }

    @FXML public void selecionarMasculino() { selecionarSexo(Sexo.MASCULINO); }
    @FXML public void selecionarFeminino() { selecionarSexo(Sexo.FEMININO); }

    private void selecionarSexo(Sexo sexo) {
        boolean mudou = sexoSelecionado != sexo;
        sexoSelecionado = sexo;
        cardMasculino.getStyleClass().remove("personagem-selecionado");
        cardFeminino.getStyleClass().remove("personagem-selecionado");
        (sexo == Sexo.MASCULINO ? cardMasculino : cardFeminino)
                .getStyleClass().add("personagem-selecionado");
        if (mudou) AudioManager.tocar(Som.SELECIONAR_SLOT);
        atualizarPreview();
    }

    private void atualizarPreview() {
        String nome = txtNome.getText() == null ? "" : txtNome.getText().trim();
        lblNomePreview.setText(nome.isEmpty() ? "SEU BOXEADOR" : nome.toUpperCase());
        lblSexoPreview.setText(sexoSelecionado == Sexo.MASCULINO ? "MASCULINO" : "FEMININO");
        Cores olhos = cmbOlhos.getValue();
        lblOlhosPreview.setText(olhos == null ? "CASTANHO" : olhos.name());
        lblMensagem.setText("");
        atualizarImagens();
    }

    /** Troca os sprites de acordo com o sexo e a cor dos olhos selecionados. */
    private void atualizarImagens() {
        Cores cor = cmbOlhos.getValue() == null ? Cores.CASTANHO : cmbOlhos.getValue();
        imgMasculino.setImage(obterSprite(Sexo.MASCULINO, cor));
        imgFeminino.setImage(obterSprite(Sexo.FEMININO, cor));
        imgPersonagemPrincipal.setImage(obterSprite(sexoSelecionado, cor));
    }

    private Image obterSprite(Sexo sexo, Cores olhos) {
        Map<Cores, Image> variantes = imagens.computeIfAbsent(sexo, chave -> new EnumMap<>(Cores.class));
        return variantes.computeIfAbsent(olhos, cor -> {
            String genero = sexo == Sexo.FEMININO ? "feminino" : "masculino";
            String caminho = "/images/personagens/" + genero + "_" + cor.name().toLowerCase(java.util.Locale.ROOT) + ".png";
            URL recurso = getClass().getResource(caminho);
            if (recurso == null) throw new IllegalStateException("Imagem não encontrada: " + caminho);
            return new Image(recurso.toExternalForm(), false);
        });
    }

    @FXML
    public void confirmar() {
        String nome = txtNome.getText() == null ? "" : txtNome.getText().trim();
        if (nome.isBlank() || nome.length() > 30) {
            AudioManager.tocar(Som.ERRO);
            lblMensagem.setText("INFORME UM NOME DE 1 A 30 CARACTERES.");
            txtNome.requestFocus();
            return;
        }

        Cores corOlhos = cmbOlhos.getValue();
        if (corOlhos == null) {
            lblMensagem.setText("SELECIONE A COR DOS OLHOS.");
            return;
        }

        // Só descarta a partida anterior depois da confirmação do novo personagem.
        Player player = new Player(1, nome,
                "Jovem determinado a seguir os passos do pai nos ringues de boxe.",
                corOlhos, sexoSelecionado);
        SceneManager.getGameController().carregarGameState(new GameState());
        SceneManager.getGameController().setPlayer(player);
        AudioManager.tocar(Som.CONFIRMAR);
        SceneManager.irPara(Tela.CUTSCENE);
    }

    @FXML
    public void voltar() {
        AudioManager.tocar(Som.CANCELAR);
        SceneManager.irPara(Tela.MENU_INICIAL);
    }
}
