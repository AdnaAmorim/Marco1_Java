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
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
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

    /** Largura cenário. */
    private static final double LARGURA_CENARIO = 960.0;
    /** Altura cenário. */
    private static final double ALTURA_CENARIO = 540.0;
    /** Imagem loja. */
    private static final String IMG_LOJA = "/images/locais/Loja_De_Tudo.png";

    /** Painel principal da tela. */
    @FXML private StackPane rootPane;
    /** Area cenário. */
    @FXML private StackPane areaCenario;
    /** Imagem — fundo desfocado. */
    @FXML private ImageView imgFundoBlur;
    /** Desfocado escuro. */
    @FXML private Region blurEscuro;
    /** Cenário canvas. */
    @FXML private Pane cenarioCanvas;
    /** Imagem — fundo. */
    @FXML private ImageView imgFundo;
    /** Vitrine itens. */
    @FXML private FlowPane vitrineItens;

    /** Texto — atleta. */
    @FXML private Label lblAtleta;
    /** Texto — saúde. */
    @FXML private Label lblSaude;
    /** Texto — energia. */
    @FXML private Label lblEnergia;
    /** Texto — dinheiro. */
    @FXML private Label lblDinheiro;
    /** Texto — data e hora. */
    @FXML private Label lblDataHora;
    /** Botão — mochila. */
    @FXML private Button btnMochila;
    /** Botão — pausa. */
    @FXML private Button btnPausa;

    /** Texto — falante. */
    @FXML private Label lblFalante;
    /** Texto — preco. */
    @FXML private Label lblPreco;
    /** Texto — texto. */
    @FXML private Label lblTexto;
    /** Painel — detalhes. */
    @FXML private VBox boxDetalhes;
    /** Box opções. */
    @FXML private VBox boxOpcoes;

    /** Controlador da partida compartilhado pela interface. */
    private final GameController jogo = SceneManager.getGameController();
    /** Item selecionado. */
    private Item itemSelecionado = Itens.ITENS_LOJA[0];
    /** Feedback compra. */
    private String feedbackCompra;
    /** Feedback erro. */
    private boolean feedbackErro;

    /** Prepara os componentes da tela após o carregamento do FXML. */
    @FXML
    public void initialize() {
        configurarEscalaCenario();
        configurarIconesHud();
        configurarTeclado();
        carregarFundo();
        atualizarTela();
    }

    /** Carrega imagem de fundo. */
    private void carregarFundo() {
        URL recurso = getClass().getResource(IMG_LOJA);
        if (recurso != null) {
            Image imagem = new Image(recurso.toExternalForm());
            imgFundo.setImage(imagem);
            imgFundoBlur.setImage(imagem);
        }
    }

    /** Configura escala do cenário para o tamanho da janela. */
    private void configurarEscalaCenario() {
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(areaCenario.widthProperty());
        clip.heightProperty().bind(areaCenario.heightProperty());
        areaCenario.setClip(clip);

        imgFundoBlur.setEffect(new GaussianBlur(28));
        imgFundoBlur.fitWidthProperty().bind(areaCenario.widthProperty());
        imgFundoBlur.fitHeightProperty().bind(areaCenario.heightProperty());

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

        BooleanBinding temLacuna = Bindings.createBooleanBinding(() -> {
            double largura = areaCenario.getWidth();
            double altura = areaCenario.getHeight();
            if (largura <= 0 || altura <= 0) {
                return false;
            }
            double s = escala.get();
            return (largura - LARGURA_CENARIO * s) > 2.0 || (altura - ALTURA_CENARIO * s) > 2.0;
        }, areaCenario.widthProperty(), areaCenario.heightProperty(), escala);

        imgFundoBlur.visibleProperty().bind(temLacuna);
        blurEscuro.visibleProperty().bind(temLacuna);
    }

    /** Configura ícones do painel de status. */
    private void configurarIconesHud() {
        IconManager.aplicarBotao(btnMochila, Icone.BAG, 18);
        IconManager.aplicarBotao(btnPausa, Icone.PAUSE, 18);
        IconManager.aplicar(lblSaude, Icone.HEART, 17, IconManager.COR_PERIGO);
        IconManager.aplicar(lblEnergia, Icone.SIGNAL_2, 17, IconManager.COR_DESTAQUE);
        IconManager.aplicar(lblDataHora, Icone.CLOCK, 17, IconManager.COR_DESTAQUE);
    }

    /** Configura atalhos de teclado da tela. */
    private void configurarTeclado() {
        rootPane.setFocusTraversable(true);
        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                abrirPausa();
                event.consume();
            }
        });
    }

    /** Atualiza tela. */
    private void atualizarTela() {
        atualizarHud();
        construirVitrine();
        atualizarCaixaDialogo();
    }

    /** Atualiza painel de status. */
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

    /** Monta vitrine de itens à venda. */
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

    /** Atualiza caixa de diálogo. */
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

    /** Confere o saldo antes de debitar o preço e acrescentar uma unidade à mochila. */
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

    /** Abre mochila do jogador. */
    @FXML
    public void abrirMochila() {
        SceneManager.abrirModal(Tela.INVENTARIO);
    }

    /** Retorna da loja ao mapa. */
    private void sairDaLoja() {
        AudioManager.tocar(Som.CLIQUE);
        jogo.voltarLocal();
        SceneManager.irPara(Tela.MAPA);
    }

    /** Abre menu de pausa. */
    @FXML
    public void abrirPausa() {
        SceneManager.abrirMenuPausa(null);
    }

    /** Cria botão para uma opção do menu. */
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

    /** Cria linha de detalhes do painel. */
    private Label criarLinhaDetalhe(String texto, String classeCss) {
        Label label = new Label(texto);
        label.setWrapText(true);
        label.getStyleClass().add(classeCss);
        return label;
    }
}
