package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.model.world.DataDia;
import com.exa863.anselmo_adna.view.fx.Icone;
import com.exa863.anselmo_adna.view.fx.Som;
import com.exa863.anselmo_adna.view.fx.Tela;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.net.URL;

/**
 * Cena em estilo visual novel do projeto "Abrir uma Academia" na Cidade B.
 * Exibe os requisitos necessários para realizar o sonho de ter a própria academia.
 *
 * @author Anselmo e Adna
 */
public class AcademiaVendaController {

    private static final double LARGURA_CENARIO = 960.0;
    private static final double ALTURA_CENARIO = 540.0;
    private static final int CAPITAL_NECESSARIO = 5000;
    private static final String IMG_ACADEMIA_VENDA = "/images/locais/Academia_A_Venda.png";

    @FXML private StackPane rootPane;
    @FXML private StackPane areaCenario;
    @FXML private Pane cenarioCanvas;
    @FXML private ImageView imgFundo;

    @FXML private Label lblAtleta;
    @FXML private Label lblSaude;
    @FXML private Label lblEnergia;
    @FXML private Label lblDinheiro;
    @FXML private Label lblDataHora;
    @FXML private Button btnPausa;

    @FXML private Label lblCapital;
    @FXML private VBox boxDetalhes;
    @FXML private VBox boxOpcoes;

    private final GameController jogo = SceneManager.getGameController();

    @FXML
    public void initialize() {
        configurarEscalaCenario();
        configurarIconesHud();
        configurarTeclado();
        carregarFundo();
        atualizarConteudo();
    }

    private void carregarFundo() {
        URL recurso = getClass().getResource(IMG_ACADEMIA_VENDA);
        if (recurso != null) {
            Image imagem = new Image(recurso.toExternalForm());
            imgFundo.setImage(imagem);
        }
    }

    private void configurarEscalaCenario() {
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(areaCenario.widthProperty());
        clip.heightProperty().bind(areaCenario.heightProperty());
        areaCenario.setClip(clip);

        DoubleBinding escala = Bindings.createDoubleBinding(() -> {
            double largura = areaCenario.getWidth();
            double altura = areaCenario.getHeight();
            if (largura <= 0 || altura <= 0) {
                return 1.0;
            }
            return Math.min(largura / LARGURA_CENARIO, altura / ALTURA_CENARIO);
        }, areaCenario.widthProperty(), areaCenario.heightProperty());

        cenarioCanvas.scaleXProperty().bind(escala);
        cenarioCanvas.scaleYProperty().bind(escala);

    }

    private void configurarIconesHud() {
        IconManager.aplicarBotao(btnPausa, Icone.PAUSE, 18);
        IconManager.aplicar(lblSaude, Icone.HEART, 17, IconManager.COR_PERIGO);
        IconManager.aplicar(lblEnergia, Icone.SIGNAL_2, 17, IconManager.COR_DESTAQUE);
        IconManager.aplicar(lblDataHora, Icone.CLOCK, 17, IconManager.COR_DESTAQUE);
    }

    private void configurarTeclado() {
        rootPane.setFocusTraversable(true);
        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                abrirPausa();
                event.consume();
            }
        });
    }

    private void atualizarConteudo() {
        Player player = jogo.getPlayer();
        DataDia data = jogo.getDataDia();
        lblDataHora.setText((data.getDiaFormatado() + " • " + data.getHoraFormatada()).toUpperCase());

        int dinheiro = 0;
        if (player != null) {
            Atributo attr = player.getAtributos();
            dinheiro = player.getDinheiro();
            lblAtleta.setText(player.getNome().toUpperCase());
            lblSaude.setText(attr.getSaude() + "/100");
            lblEnergia.setText(attr.getEnergia() + "/100");
            lblDinheiro.setText("R$ " + dinheiro);
        }

        lblCapital.setText("CAPITAL: R$ " + dinheiro + " / R$ " + CAPITAL_NECESSARIO);

        Label reqMundial = new Label("REQUISITO 1: CONQUISTAR O CAMPEONATO MUNDIAL");
        reqMundial.setWrapText(true);
        reqMundial.getStyleClass().add("vn-linha-info");

        boolean temCapital = dinheiro >= CAPITAL_NECESSARIO;
        Label reqDinheiro = new Label("REQUISITO 2: CAPITAL INICIAL DE R$ 5.000 (SEU DINHEIRO: R$ " + dinheiro + ")");
        reqDinheiro.setWrapText(true);
        reqDinheiro.getStyleClass().add(temCapital ? "vn-linha-positiva" : "vn-linha-alerta");

        boxDetalhes.getChildren().setAll(reqMundial, reqDinheiro);

        Button btnVoltar = new Button("VOLTAR PARA A CIDADE");
        btnVoltar.setMaxWidth(Double.MAX_VALUE);
        btnVoltar.setFocusTraversable(false);
        btnVoltar.getStyleClass().addAll("vn-opcao", "vn-opcao-sair");
        IconManager.aplicarBotao(btnVoltar, Icone.LEFT_ARROW, 15);
        btnVoltar.setOnAction(e -> {
            AudioManager.tocar(Som.CLIQUE);
            SceneManager.irPara(Tela.MAPA);
        });

        boxOpcoes.getChildren().setAll(btnVoltar);
        AudioManager.aplicarHoverNosBotoes(boxOpcoes);
    }

    @FXML
    public void abrirPausa() {
        SceneManager.abrirMenuPausa(null);
    }
}