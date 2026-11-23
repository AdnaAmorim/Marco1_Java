package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.model.stats.Item;
import com.exa863.anselmo_adna.view.fx.Icone;
import com.exa863.anselmo_adna.view.fx.ItemImagem;
import com.exa863.anselmo_adna.view.fx.Som;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Modal da mochila do atleta.
 * Lista os itens carregados com ícones, exibe detalhes e permite consumir suprimentos.
 *
 * @author Anselmo e Adna
 */
public class InventarioController {

    /** Painel principal da tela. */
    @FXML private StackPane rootPane;
    /** Texto — status. */
    @FXML private Label lblStatus;
    /** Grade — itens. */
    @FXML private FlowPane gridItens;
    /** Texto — vazio. */
    @FXML private Label lblVazio;
    /** Painel — detalhe. */
    @FXML private HBox boxDetalhe;
    /** Imagem — selecionado. */
    @FXML private ImageView imgSelecionado;
    /** Texto — nome item. */
    @FXML private Label lblNomeItem;
    /** Texto — descrição do item. */
    @FXML private Label lblDescItem;
    /** Texto — efeito item. */
    @FXML private Label lblEfeitoItem;
    /** Botão — usar. */
    @FXML private Button btnUsar;
    /** Texto — mensagem. */
    @FXML private Label lblMensagem;
    /** Botão — fechar. */
    @FXML private Button btnFechar;

    /** Controlador da partida compartilhado pela interface. */
    private final GameController jogo = SceneManager.getGameController();
    /** Item selecionado. */
    private Item itemSelecionado;

    /** Prepara os componentes da tela após o carregamento do FXML. */
    @FXML
    public void initialize() {
        IconManager.aplicarBotao(btnUsar, Icone.PLAY, 15);
        IconManager.aplicarBotao(btnFechar, Icone.LEFT_ARROW, 15);

        rootPane.setFocusTraversable(true);
        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                fechar();
                event.consume();
            }
        });

        atualizarTela();
    }

    /** Atualiza tela. */
    private void atualizarTela() {
        Player player = jogo.getPlayer();
        if (player == null) {
            return;
        }

        Atributo attr = player.getAtributos();
        lblStatus.setText("SAÚDE " + attr.getSaude() + "/100  •  ENERGIA " + attr.getEnergia() + "/100");

        Map<Item, Integer> itens = player.getInventario().getItens();
        gridItens.getChildren().clear();

        if (itens.isEmpty()) {
            itemSelecionado = null;
            lblVazio.setVisible(true);
            lblVazio.setManaged(true);
            atualizarDetalhe();
            return;
        }

        lblVazio.setVisible(false);
        lblVazio.setManaged(false);

        List<Item> lista = new ArrayList<>(itens.keySet());
        // Ao consumir a última unidade do item selecionado, o painel passa a mostrar outro item da mochila.
        if (itemSelecionado == null || !itens.containsKey(itemSelecionado)) {
            itemSelecionado = lista.get(0);
        }

        for (Item item : lista) {
            int qtd = itens.get(item);
            VBox tile = criarTileItem(item, qtd);
            gridItens.getChildren().add(tile);
        }

        atualizarDetalhe();
    }

    /** Cria cartão do item na grade do inventário. */
    private VBox criarTileItem(Item item, int quantidade) {
        ImageView icone = new ImageView(ItemImagem.obter(item));
        icone.setFitWidth(48);
        icone.setFitHeight(48);
        icone.setPreserveRatio(true);
        icone.setSmooth(true);

        Label lblNome = new Label(item.getNome().toUpperCase());
        lblNome.getStyleClass().add("inv-item-nome");
        lblNome.setWrapText(true);

        Label lblQtd = new Label("X" + quantidade + " • " + (item.isConsumivel() ? "CONSUMÍVEL" : "ESPECIAL"));
        lblQtd.getStyleClass().add("inv-item-sub");

        VBox tile = new VBox(icone, lblNome, lblQtd);
        tile.getStyleClass().add("inv-item-tile");
        if (item.equals(itemSelecionado)) {
            tile.getStyleClass().add("inv-item-selecionado");
        }
        tile.setOnMouseClicked(e -> {
            AudioManager.tocar(Som.SELECIONAR_SLOT);
            itemSelecionado = item;
            lblMensagem.setText("");
            atualizarTela();
        });
        return tile;
    }

    /** Atualiza detalhe. */
    private void atualizarDetalhe() {
        if (itemSelecionado == null) {
            imgSelecionado.setImage(null);
            lblNomeItem.setText("MOCHILA VAZIA");
            lblDescItem.setText("COMPRE ALIMENTOS E SUPLEMENTOS NA LOJA DE TUDO.");
            lblEfeitoItem.setText("");
            btnUsar.setDisable(true);
            return;
        }

        Image img = ItemImagem.obter(itemSelecionado);
        imgSelecionado.setImage(img);
        lblNomeItem.setText(itemSelecionado.getNome().toUpperCase());
        lblDescItem.setText(itemSelecionado.getDescricao().toUpperCase());
        lblEfeitoItem.setText(ItemImagem.formatarEfeitos(itemSelecionado));
        btnUsar.setDisable(!itemSelecionado.isConsumivel());
    }

    /** Usa item selecionado. */
    @FXML
    public void usarSelecionado() {
        Player player = jogo.getPlayer();
        if (player == null || itemSelecionado == null || !itemSelecionado.isConsumivel()) {
            AudioManager.tocar(Som.ERRO);
            return;
        }

        String nome = itemSelecionado.getNome().toUpperCase();
        if (player.getInventario().usarItem(itemSelecionado, player)) {
            AudioManager.tocar(Som.CONFIRMAR);
            lblMensagem.setText("VOCÊ UTILIZOU " + nome + "!");
            atualizarTela();
        } else {
            AudioManager.tocar(Som.ERRO);
        }
    }

    /** Fecha o painel atual. */
    @FXML
    public void fechar() {
        SceneManager.fecharModal();
    }
}
