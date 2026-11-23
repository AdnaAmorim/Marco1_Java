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

    /** Largura base. */
    private static final double LARGURA_BASE = 1280.0;
    /** Altura base. */
    private static final double ALTURA_BASE = 760.0;
    /** Zoom hover. */
    private static final double ZOOM_HOVER = 3.1;
    /** Deslocamento rosto y. */
    private static final double DESLOCAMENTO_ROSTO_Y = 345.0;

    /** Painel principal da tela. */
    @FXML private StackPane rootPane;
    /** Conteudo container. */
    @FXML private VBox conteudoContainer;
    /** Palco na prévia. */
    @FXML private StackPane palcoPreview;
    /** Campo — nome. */
    @FXML private TextField txtNome;
    /** Seletor — cor dos olhos. */
    @FXML private ComboBox<Cores> cmbOlhos;
    /** Card masculino. */
    @FXML private VBox cardMasculino;
    /** Card feminino. */
    @FXML private VBox cardFeminino;
    /** Texto — nome na prévia. */
    @FXML private Label lblNomePreview;
    /** Texto — sexo na prévia. */
    @FXML private Label lblSexoPreview;
    /** Texto — cor dos olhos na prévia. */
    @FXML private Label lblOlhosPreview;
    /** Texto — mensagem. */
    @FXML private Label lblMensagem;
    /** Botão — confirmar. */
    @FXML private Button btnConfirmar;
    /** Imagem — masculino. */
    @FXML private ImageView imgMasculino;
    /** Imagem — feminino. */
    @FXML private ImageView imgFeminino;
    /** Imagem — personagem principal. */
    @FXML private ImageView imgPersonagemPrincipal;

    /** Imagens. */
    private final Map<Sexo, Map<Cores, Image>> imagens = new EnumMap<>(Sexo.class);

    /** Sexo selecionado. */
    private Sexo sexoSelecionado = Sexo.MASCULINO;
    /** Animação zoom. */
    private ParallelTransition animacaoZoom;

    /** Prepara os componentes da tela após o carregamento do FXML. */
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

    /** Configura ampliação da prévia do personagem. */
    private void configurarZoomPreview() {
        if (palcoPreview == null || imgPersonagemPrincipal == null) {
            return;
        }
        // Limita o zoom ao espaço da prévia quando a janela muda de tamanho.
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(palcoPreview.widthProperty());
        clip.heightProperty().bind(palcoPreview.heightProperty());
        palcoPreview.setClip(clip);

        palcoPreview.hoverProperty().addListener((obs, antigo, hover) -> animarZoomPreview(hover));
    }

    /** Anima ampliação da prévia do personagem. */
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

    /** Configura escala dos elementos conforme o tamanho da janela. */
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

    /** Seleciona — masculino. */
    @FXML public void selecionarMasculino() { selecionarSexo(Sexo.MASCULINO); }
    /** Seleciona feminino. */
    @FXML public void selecionarFeminino() { selecionarSexo(Sexo.FEMININO); }

    /** Seleciona sexo. */
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

    /** Atualiza prévia do personagem. */
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

    /** Obtém sprite. */
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

    /** Confirma a seleção atual. */
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

    /** Retorna à tela anterior. */
    @FXML
    public void voltar() {
        AudioManager.tocar(Som.CANCELAR);
        SceneManager.irPara(Tela.MENU_INICIAL);
    }
}
