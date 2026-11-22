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

    @FXML private StackPane rootPane;
    @FXML private Label lblStatus;
    @FXML private FlowPane gridItens;
    @FXML private Label lblVazio;
    @FXML private HBox boxDetalhe;
    @FXML private ImageView imgSelecionado;
    @FXML private Label lblNomeItem;
    @FXML private Label lblDescItem;
    @FXML private Label lblEfeitoItem;
    @FXML private Button btnUsar;
    @FXML private Label lblMensagem;
    @FXML private Button btnFechar;

    private final GameController jogo = SceneManager.getGameController();
    private Item itemSelecionado;

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

    @FXML
    public void fechar() {
        SceneManager.fecharModal();
    }
}
