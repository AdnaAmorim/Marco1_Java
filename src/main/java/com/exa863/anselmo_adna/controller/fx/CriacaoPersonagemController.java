package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.model.GameState;
import com.exa863.anselmo_adna.model.character.Cores;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.character.Sexo;
import com.exa863.anselmo_adna.view.fx.Som;
import com.exa863.anselmo_adna.view.fx.Tela;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

import java.net.URL;
import java.util.EnumMap;
import java.util.Map;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/** Versão JavaFX da criação de personagem existente na interface de terminal. */
public class CriacaoPersonagemController {

    @FXML private StackPane rootPane;
    @FXML private StackPane centroConteudo;
    @FXML private ScrollPane scrollConteudo;
    @FXML private ImageView imgFundo;
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

    @FXML
    public void initialize() {
        // Ocupa toda a área visível do ScrollPane e centraliza os dois painéis.
        // Se a altura da janela for pequena, o conteúdo continua rolável.
        scrollConteudo.viewportBoundsProperty().addListener((obs, antigo, atual) ->
                centroConteudo.setMinHeight(atual.getHeight()));
        configurarCenarioDeFundo();
        cmbOlhos.setItems(FXCollections.observableArrayList(Cores.values()));
        cmbOlhos.getSelectionModel().select(Cores.CASTANHO);
        cmbOlhos.valueProperty().addListener((obs, antes, depois) -> atualizarPreview());
        txtNome.textProperty().addListener((obs, antes, depois) -> atualizarPreview());
        selecionarMasculino();
        txtNome.setOnAction(event -> confirmar());
        btnConfirmar.setDefaultButton(true);
    }

    /** Imagem da academia já existente, cobrindo toda a janela sem distorcer. */
    private void configurarCenarioDeFundo() {
        URL recurso = getClass().getResource("/images/cutscenes/02_sonho_do_pai.png");
        if (recurso == null) {
            throw new IllegalStateException("Fundo da seleção de personagem não encontrado.");
        }
        imgFundo.setImage(new Image(recurso.toExternalForm(), false));
        imgFundo.fitWidthProperty().bind(rootPane.widthProperty());
        imgFundo.fitHeightProperty().bind(rootPane.heightProperty());
        rootPane.widthProperty().addListener((obs, anterior, atual) -> ajustarFundo());
        rootPane.heightProperty().addListener((obs, anterior, atual) -> ajustarFundo());
        ajustarFundo();
    }

    private void ajustarFundo() {
        Image imagem = imgFundo.getImage();
        if (imagem == null || rootPane.getWidth() <= 0 || rootPane.getHeight() <= 0) return;
        double largura = imagem.getWidth();
        double altura = imagem.getHeight();
        double proporcaoTela = rootPane.getWidth() / rootPane.getHeight();
        if (largura / altura > proporcaoTela) {
            double larguraVisivel = altura * proporcaoTela;
            imgFundo.setViewport(new Rectangle2D((largura - larguraVisivel) / 2, 0, larguraVisivel, altura));
        } else {
            double alturaVisivel = largura / proporcaoTela;
            imgFundo.setViewport(new Rectangle2D(0, (altura - alturaVisivel) / 2, largura, alturaVisivel));
        }
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
