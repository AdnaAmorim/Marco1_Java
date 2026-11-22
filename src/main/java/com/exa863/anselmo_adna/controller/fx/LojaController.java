package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.model.stats.Item;
import com.exa863.anselmo_adna.model.stats.Itens;
import com.exa863.anselmo_adna.model.world.DataDia;
import com.exa863.anselmo_adna.view.fx.Icone;
import com.exa863.anselmo_adna.view.fx.ItemImagem;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Cena em estilo visual novel da Loja de Tudo (Cidade A).
 * Exibe a vitrine de alimentos e suplementos com ícones pixel art e permite comprar itens.
 *
 * @author Anselmo e Adna
 */
public class LojaController {

    private static final double LARGURA_CENARIO = 960.0;
    private static final double ALTURA_CENARIO = 540.0;
    private static final String IMG_LOJA = "/images/locais/Loja_De_Tudo.png";

    @FXML private StackPane rootPane;
    @FXML private StackPane areaCenario;
    @FXML private Pane cenarioCanvas;
    @FXML private ImageView imgFundo;
    @FXML private FlowPane vitrineItens;

    @FXML private Label lblAtleta;
    @FXML private Label lblSaude;
    @FXML private Label lblEnergia;
    @FXML private Label lblDinheiro;
    @FXML private Label lblDataHora;
    @FXML private Button btnMochila;
    @FXML private Button btnPausa;

    @FXML private Label lblFalante;
    @FXML private Label lblPreco;
    @FXML private Label lblTexto;
    @FXML private VBox boxDetalhes;
    @FXML private VBox boxOpcoes;

    private final GameController jogo = SceneManager.getGameController();
    private Item itemSelecionado = Itens.ITENS_LOJA[0];
    private String feedbackCompra;
    private boolean feedbackErro;

    @FXML
    public void initialize() {
        configurarEscalaCenario();
        configurarIconesHud();
        configurarTeclado();
        carregarFundo();
        atualizarTela();
    }

    private void carregarFundo() {
        URL recurso = getClass().getResource(IMG_LOJA);
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
        IconManager.aplicarBotao(btnMochila, Icone.BAG, 18);
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

    private void atualizarTela() {
        atualizarHud();
        construirVitrine();
        atualizarCaixaDialogo();
    }

    private void atualizarHud() {
        Player player = jogo.getPlayer();
        DataDia data = jogo.getDataDia();
        lblDataHora.setText((data.getDiaFormatado() + " • " + data.getHoraFormatada()).toUpperCase());

        if (player == null) {
            lblAtleta.setText("ATLETA");
            lblSaude.setText("100/100");
            lblEnergia.setText("100/100");
            lblDinheiro.setText("R$ 0");
            return;
        }

        Atributo attr = player.getAtributos();
        lblAtleta.setText(player.getNome().toUpperCase());
        lblSaude.setText(attr.getSaude() + "/100");
        lblEnergia.setText(attr.getEnergia() + "/100");
        lblDinheiro.setText("R$ " + player.getDinheiro());
    }

    private void construirVitrine() {
        Player player = jogo.getPlayer();
        vitrineItens.getChildren().clear();

        for (Item item : Itens.ITENS_LOJA) {
            int naMochila = player != null ? player.getInventario().getQuantidade(item) : 0;

            ImageView icone = new ImageView(ItemImagem.obter(item));
            icone.setFitWidth(44);
            icone.setFitHeight(44);
            icone.setPreserveRatio(true);
            icone.setSmooth(true);

            Label lblNome = new Label(item.getNome().toUpperCase());
            lblNome.getStyleClass().add("inv-item-nome");
            lblNome.setWrapText(true);

            Label lblValor = new Label("R$ " + item.getPreco() + (naMochila > 0 ? "  •  X" + naMochila : ""));
            lblValor.getStyleClass().add("inv-item-preco");

            VBox tile = new VBox(icone, lblNome, lblValor);
            tile.getStyleClass().add("inv-item-tile");
            if (item.equals(itemSelecionado)) {
                tile.getStyleClass().add("inv-item-selecionado");
            }
            tile.setOnMouseClicked(e -> {
                AudioManager.tocar(Som.SELECIONAR_SLOT);
                itemSelecionado = item;
                feedbackCompra = null;
                atualizarTela();
            });
            vitrineItens.getChildren().add(tile);
        }
    }

    private void atualizarCaixaDialogo() {
        lblFalante.setText("LOJA DE TUDO • " + itemSelecionado.getNome().toUpperCase());
        lblPreco.setText("PREÇO: R$ " + itemSelecionado.getPreco());
        lblTexto.setText(itemSelecionado.getDescricao().toUpperCase());

        List<Label> detalhes = new ArrayList<>();
        detalhes.add(criarLinhaDetalhe("EFEITO: " + ItemImagem.formatarEfeitos(itemSelecionado), "vn-linha-positiva"));
        if (feedbackCompra != null) {
            detalhes.add(criarLinhaDetalhe(feedbackCompra, feedbackErro ? "vn-linha-alerta" : "vn-linha-info"));
        }
        boxDetalhes.getChildren().setAll(detalhes);
        boxDetalhes.setVisible(true);
        boxDetalhes.setManaged(true);

        boxOpcoes.getChildren().setAll(
                criarBotaoOpcao("COMPRAR (R$ " + itemSelecionado.getPreco() + ")", Icone.CART, false, this::comprarSelecionado),
                criarBotaoOpcao("ABRIR MOCHILA", Icone.BAG, false, this::abrirMochila),
                criarBotaoOpcao("SAIR DA LOJA", Icone.LEFT_ARROW, true, this::sairDaLoja)
        );
        AudioManager.aplicarHoverNosBotoes(boxOpcoes);
    }

    private void comprarSelecionado() {
        Player player = jogo.getPlayer();
        if (player == null || itemSelecionado == null) {
            return;
        }

        if (player.getDinheiro() < itemSelecionado.getPreco()) {
            AudioManager.tocar(Som.ERRO);
            feedbackErro = true;
            feedbackCompra = "DINHEIRO INSUFICIENTE! PREÇO: R$ " + itemSelecionado.getPreco()
                    + " • SEU DINHEIRO: R$ " + player.getDinheiro();
            atualizarTela();
            return;
        }

        player.setDinheiro(player.getDinheiro() - itemSelecionado.getPreco());
        player.getInventario().adicionarItem(itemSelecionado);
        AudioManager.tocar(Som.CONFIRMAR);
        feedbackErro = false;
        feedbackCompra = "COMPRA REALIZADA: " + itemSelecionado.getNome().toUpperCase() + " ADICIONADO À MOCHILA!";
        atualizarTela();
    }

    @FXML
    public void abrirMochila() {
        SceneManager.abrirModal(Tela.INVENTARIO);
    }

    private void sairDaLoja() {
        AudioManager.tocar(Som.CLIQUE);
        jogo.voltarLocal();
        SceneManager.irPara(Tela.MAPA);
    }

    @FXML
    public void abrirPausa() {
        SceneManager.abrirMenuPausa(null);
    }

    private Button criarBotaoOpcao(String texto, Icone icone, boolean estiloSair, Runnable acao) {
        Button botao = new Button(texto);
        botao.setMaxWidth(Double.MAX_VALUE);
        botao.setFocusTraversable(false);
        botao.getStyleClass().add("vn-opcao");
        if (estiloSair) {
            botao.getStyleClass().add("vn-opcao-sair");
        }
        IconManager.aplicarBotao(botao, icone, 15);
        botao.setOnAction(e -> acao.run());
        return botao;
    }

    private Label criarLinhaDetalhe(String texto, String classeCss) {
        Label label = new Label(texto);
        label.setWrapText(true);
        label.getStyleClass().add(classeCss);
        return label;
    }
}
