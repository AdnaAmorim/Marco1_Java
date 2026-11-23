package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.combat.EstiloLuta;
import com.exa863.anselmo_adna.model.combat.estilos.Contragolpeador;
import com.exa863.anselmo_adna.model.combat.estilos.Nocauteador;
import com.exa863.anselmo_adna.model.combat.estilos.Velocista;
import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.model.world.DataDia;
import com.exa863.anselmo_adna.view.fx.Icone;
import com.exa863.anselmo_adna.view.fx.Som;
import com.exa863.anselmo_adna.view.fx.Tela;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.List;

/**
 * Cena em estilo visual novel do Campeonato Mundial (Cidade B).
 * Valida requisitos, apresenta Anthony "O Trovão" e oferece a seleção estilizada de postura.
 *
 * @author Anselmo e Adna
 */
public class CampeonatoMundialController {

    /** Largura cenário. */
    private static final double LARGURA_CENARIO = 960.0;
    /** Altura cenário. */
    private static final double ALTURA_CENARIO = 540.0;
    /** Imagem mundial. */
    private static final String IMG_MUNDIAL = "/images/locais/Campeonato_Mundial.png";
    /** Imagem anthony. */
    private static final String IMG_ANTHONY = "/images/visual_novel/Anthony.png";

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
    /** Card adversario. */
    @FXML private HBox cardAdversario;
    /** Imagem — adversário. */
    @FXML private ImageView imgAdversario;

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
    /** Botão — pausa. */
    @FXML private Button btnPausa;

    /** Texto — falante. */
    @FXML private Label lblFalante;
    /** Texto — taxa. */
    @FXML private Label lblTaxa;
    /** Texto — texto. */
    @FXML private Label lblTexto;
    /** Painel — detalhes. */
    @FXML private VBox boxDetalhes;
    /** Box opções. */
    @FXML private VBox boxOpcoes;

    /** Controlador da partida compartilhado pela interface. */
    private final GameController jogo = SceneManager.getGameController();

    /** Prepara os componentes da tela após o carregamento do FXML. */
    @FXML
    public void initialize() {
        configurarEscalaCenario();
        configurarIconesHud();
        configurarTeclado();
        carregarImagens();
        atualizarHud();
        verificarAcesso();
    }

    /** Carrega imagens. */
    private void carregarImagens() {
        URL recFundo = getClass().getResource(IMG_MUNDIAL);
        if (recFundo != null) {
            Image imagem = new Image(recFundo.toExternalForm());
            imgFundo.setImage(imagem);
            imgFundoBlur.setImage(imagem);
        }

        URL recAnthony = getClass().getResource(IMG_ANTHONY);
        if (recAnthony != null) {
            Image imgAdv = new Image(recAnthony.toExternalForm());
            imgAdversario.setImage(imgAdv);
            double w = imgAdv.getWidth();
            double h = imgAdv.getHeight();
            if (w > 0 && h > 0) {
                imgAdversario.setViewport(new Rectangle2D(w * 0.08, h * 0.02, w * 0.84, h * 0.48));
            }
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

    /** Atualiza painel de status. */
    private void atualizarHud() {
        Player player = jogo.getPlayer();
        DataDia data = jogo.getDataDia();
        lblDataHora.setText((data.getDiaFormatado() + " • " + data.getHoraFormatada()).toUpperCase());

        if (player == null) {
            return;
        }
        Atributo attr = player.getAtributos();
        lblAtleta.setText(player.getNome().toUpperCase());
        lblSaude.setText(attr.getSaude() + "/100");
        lblEnergia.setText(attr.getEnergia() + "/100");
        lblDinheiro.setText("R$ " + player.getDinheiro());
    }

    /** Libera a disputa do cinturão após a conclusão do capítulo 10. */
    private void verificarAcesso() {
        Player player = jogo.getPlayer();
        if (player == null) {
            voltarAoMapa();
            return;
        }

        if (!player.isCapituloConcluido("CAPITULO_10")) {
            cardAdversario.setVisible(false);
            cardAdversario.setManaged(false);
            lblTaxa.setText("ACESSO BLOQUEADO");
            lblFalante.setText("CAMPEONATO MUNDIAL • BLOQUEADO");
            lblTexto.setText("O CAMPEONATO MUNDIAL AINDA NÃO ESTÁ LIBERADO PARA VOCÊ.");
            mostrarDetalhes(List.of(
                    criarLinhaDetalhe("COMPLETE OS CAPÍTULOS ANTERIORES E AS LUTAS CLASSIFICATÓRIAS PRIMEIRO.", "vn-linha-alerta")
            ));
            definirBotaoSaida("VOLTAR AO MAPA");
            return;
        }

        exibirMenuMundial();
    }

    /** Exibe opções do Campeonato Mundial. */
    private void exibirMenuMundial() {
        cardAdversario.setVisible(true);
        cardAdversario.setManaged(true);
        lblTaxa.setText("INSCRIÇÃO: R$ " + GameController.TAXA_MUNDIAL);
        lblFalante.setText("CAMPEONATO MUNDIAL • O GRANDE PALCO");
        lblTexto.setText("A ARENA LOTADA GRITA O SEU NOME. É A DISPUTA DEFINITIVA CONTRA O MELHOR LUTADOR DO PLANETA!");
        mostrarDetalhes(List.of(
                criarLinhaDetalhe("EXIGE R$ " + GameController.TAXA_MUNDIAL + " DE INSCRIÇÃO  •  SAÚDE >= "
                        + GameController.MIN_STATUS_MUNDIAL + " E ENERGIA >= " + GameController.MIN_STATUS_MUNDIAL, "vn-linha-info")
        ));

        boxOpcoes.getChildren().setAll(
                criarBotaoOpcao("PAGAR R$ " + GameController.TAXA_MUNDIAL + " E SUBIR NO RINGUE", Icone.EXCLAMATION_MARK, false, this::validarEAvancarPostura),
                criarBotaoOpcao("VOLTAR PARA A CIDADE", Icone.LEFT_ARROW, true, this::voltarAoMapa)
        );
        AudioManager.aplicarHoverNosBotoes(boxOpcoes);
    }

    /** Confere a taxa de inscrição, a saúde e a energia antes de oferecer os estilos de luta. */
    private void validarEAvancarPostura() {
        Player player = jogo.getPlayer();
        if (player == null) {
            return;
        }
        Atributo attr = player.getAtributos();

        if (player.getDinheiro() < GameController.TAXA_MUNDIAL) {
            AudioManager.tocar(Som.ERRO);
            lblFalante.setText("INSCRIÇÃO RECUSADA • SEM DINHEIRO");
            lblTexto.setText("A INSCRIÇÃO NO CAMPEONATO MUNDIAL CUSTA R$ " + GameController.TAXA_MUNDIAL + ".");
            mostrarDetalhes(List.of(
                    criarLinhaDetalhe("SEU DINHEIRO ATUAL: R$ " + player.getDinheiro(), "vn-linha-alerta")
            ));
            definirBotaoVoltar("VOLTAR", this::exibirMenuMundial);
            return;
        }

        if (attr.getSaude() < GameController.MIN_STATUS_MUNDIAL || attr.getEnergia() < GameController.MIN_STATUS_MUNDIAL) {
            AudioManager.tocar(Som.ERRO);
            lblFalante.setText("MÉDICO DA COMISSÃO");
            lblTexto.setText("VOCÊ PRECISA DE PELO MENOS " + GameController.MIN_STATUS_MUNDIAL
                    + " DE SAÚDE E ENERGIA PARA ENCARAR O CAMPEONATO MUNDIAL!");
            mostrarDetalhes(List.of(
                    criarLinhaDetalhe("STATUS ATUAL: " + attr.getSaude() + " HP  •  " + attr.getEnergia() + " EN", "vn-linha-alerta")
            ));
            definirBotaoVoltar("VOLTAR", this::exibirMenuMundial);
            return;
        }

        exibirEscolhaPostura();
    }

    /** Exibe estilos de luta disponíveis. */
    private void exibirEscolhaPostura() {
        AudioManager.tocar(Som.CLIQUE);
        lblFalante.setText("POSTURA PARA A FINAL MUNDIAL");
        lblTexto.setText("ESCOLHA SUA POSTURA DE COMBATE CONTRA ANTHONY \"O TROVÃO\":");

        HBox linhaVelocista = criarLinhaPostura("VELOCISTA",
                List.of("+AGI (15-75%)", "+20% EN"), List.of("-20% FOR"));
        HBox linhaNocauteador = criarLinhaPostura("NOCAUTEADOR",
                List.of("+FOR (15-75%)"), List.of("-20% AGI", "-20% EN"));
        HBox linhaContragolpeador = criarLinhaPostura("CONTRAGOLPEADOR",
                List.of("+RES (15-75%)", "+INT (15-75%)"), List.of("-15% AGI"));

        boxDetalhes.getChildren().setAll(linhaVelocista, linhaNocauteador, linhaContragolpeador);
        boxDetalhes.setVisible(true);
        boxDetalhes.setManaged(true);

        Button btnVel = criarBotaoOpcao("POSTURA VELOCISTA", Icone.STAR, false, () -> iniciarCombate(new Velocista()));
        Button btnNoc = criarBotaoOpcao("POSTURA NOCAUTEADOR", Icone.STAR, false, () -> iniciarCombate(new Nocauteador()));
        Button btnCon = criarBotaoOpcao("POSTURA CONTRAGOLPEADOR", Icone.STAR, false, () -> iniciarCombate(new Contragolpeador()));
        Button btnVoltar = criarBotaoOpcao("VOLTAR", Icone.LEFT_ARROW, true, this::exibirMenuMundial);

        vincularDestaquePostura(btnVel, linhaVelocista);
        vincularDestaquePostura(btnNoc, linhaNocauteador);
        vincularDestaquePostura(btnCon, linhaContragolpeador);

        boxOpcoes.getChildren().setAll(btnVel, btnNoc, btnCon, btnVoltar);
        AudioManager.aplicarHoverNosBotoes(boxOpcoes);
    }

    /** Inicia combate. */
    private void iniciarCombate(EstiloLuta postura) {
        Player player = jogo.getPlayer();
        if (player == null) {
            return;
        }
        AudioManager.tocar(Som.CONFIRMAR);
        MundialFx.iniciarCombate(player, postura);
    }

    /** Retorna à navegação pelo mapa. */
    private void voltarAoMapa() {
        AudioManager.tocar(Som.CLIQUE);
        SceneManager.irPara(Tela.MAPA);
    }

    /** Abre menu de pausa. */
    @FXML
    public void abrirPausa() {
        SceneManager.abrirMenuPausa(null);
    }

    /** Cria opção de estilo de luta. */
    private HBox criarLinhaPostura(String nome, List<String> bonus, List<String> penalidades) {
        HBox linha = new HBox();
        linha.getStyleClass().add("postura-linha");

        Label lblNome = new Label(nome);
        lblNome.getStyleClass().add("postura-nome");
        linha.getChildren().add(lblNome);

        for (String b : bonus) {
            Label tag = new Label(b);
            tag.getStyleClass().add("postura-tag-pos");
            linha.getChildren().add(tag);
        }
        for (String p : penalidades) {
            Label tag = new Label(p);
            tag.getStyleClass().add("postura-tag-neg");
            linha.getChildren().add(tag);
        }
        return linha;
    }

    /** Vincula destaque visual à seleção da postura. */
    private void vincularDestaquePostura(Button botao, HBox linha) {
        botao.hoverProperty().addListener((obs, antes, hover) -> {
            if (hover) {
                if (!linha.getStyleClass().contains("postura-linha-ativa")) {
                    linha.getStyleClass().add("postura-linha-ativa");
                }
            } else {
                linha.getStyleClass().remove("postura-linha-ativa");
            }
        });
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

    /** Define ação do botão de saída. */
    private void definirBotaoSaida(String texto) {
        boxOpcoes.getChildren().setAll(criarBotaoOpcao(texto, Icone.LEFT_ARROW, true, this::voltarAoMapa));
        AudioManager.aplicarHoverNosBotoes(boxOpcoes);
    }

    /** Define ação do botão de retorno. */
    private void definirBotaoVoltar(String texto, Runnable acao) {
        boxOpcoes.getChildren().setAll(criarBotaoOpcao(texto, Icone.LEFT_ARROW, false, acao));
        AudioManager.aplicarHoverNosBotoes(boxOpcoes);
    }

    /** Cria linha de detalhes do painel. */
    private Label criarLinhaDetalhe(String texto, String classeCss) {
        Label label = new Label(texto);
        label.setWrapText(true);
        label.getStyleClass().add(classeCss);
        return label;
    }

    /** Mostra detalhes. */
    private void mostrarDetalhes(List<Label> linhas) {
        boxDetalhes.getChildren().setAll(linhas);
        boxDetalhes.setVisible(true);
        boxDetalhes.setManaged(true);
    }
}
