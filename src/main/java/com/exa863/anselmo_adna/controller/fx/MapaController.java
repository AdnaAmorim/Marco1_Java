package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.NarrativaController;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.model.world.DataDia;
import com.exa863.anselmo_adna.model.world.Local;
import com.exa863.anselmo_adna.view.fx.Icone;
import com.exa863.anselmo_adna.view.fx.MapaConfig;
import com.exa863.anselmo_adna.view.fx.Som;
import com.exa863.anselmo_adna.view.fx.Tela;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Tela genérica de navegação de mapa no JavaFX.
 * Monta o cenário e os marcadores dinamicamente a partir de {@link MapaConfig}.
 *
 * @author Anselmo e Adna
 */
public class MapaController {

    private static final double LARGURA_MAPA = 960.0;
    private static final double ALTURA_MAPA = 540.0;
    private static final Duration DURACAO_FADE_FUNDO = Duration.millis(650);
    private static final Duration INTERVALO_MONITOR_RELOGIO = Duration.millis(400);
    private static final String INFO_PADRAO = "CLIQUE EM UM LOCAL DA CIDADE PARA EXPLORAR.";

    @FXML private StackPane rootPane;
    @FXML private StackPane areaMapa;
    @FXML private ImageView imgMapaBlurAnterior;
    @FXML private ImageView imgMapaBlur;
    @FXML private Region blurEscuro;
    @FXML private Pane mapaCanvas;
    @FXML private ImageView imgMapaAnterior;
    @FXML private ImageView imgMapa;
    @FXML private Pane pinsLayer;

    @FXML private Label lblAtleta;
    @FXML private Label lblSaude;
    @FXML private Label lblEnergia;
    @FXML private Label lblDinheiro;
    @FXML private Label lblDataHora;
    @FXML private Button btnMochila;
    @FXML private Button btnPausa;

    @FXML private VBox painelViagem;
    @FXML private VBox listaDestinos;
    @FXML private Button btnFecharViagem;
    @FXML private Label lblInfoLocal;

    private final GameController jogo = SceneManager.getGameController();
    private final Map<String, Image> cacheImagens = new HashMap<>();
    private MapaConfig configAtual;
    private String caminhoImagemAtual;
    private ParallelTransition transicaoFundo;
    private Timeline monitorRelogio;
    private int ultimoDia = -1;
    private int ultimaHora = -1;
    private int ultimoMinuto = -1;
    private int ultimaSaude = -1;
    private int ultimaEnergia = -1;
    private int ultimoDinheiro = -1;

    @FXML
    public void initialize() {
        configurarEscalaMapa();
        configurarIconesHud();
        configurarTeclado();
        carregarMapa(MapaConfig.paraLocal(resolverIdMapaAtual()));
        iniciarMonitorRelogio();
    }

    public void carregarMapa(MapaConfig config) {
        boolean animar = imgMapa.getImage() != null;
        this.configAtual = config;
        garantirLocalDoMapa(config.getLocalId());
        atualizarFundoHorario(animar);
        atualizarHud();
        construirMarcadores();
        lblInfoLocal.setText(INFO_PADRAO);
    }

    /**
     * Sincroniza HUD e imagem do cenário com o horário atual do relógio do jogo.
     */
    public void atualizarEstado() {
        atualizarHud();
        atualizarFundoHorario(true);
    }

    private String resolverIdMapaAtual() {
        Local atual = jogo.getLocalAtual();
        return atual != null ? atual.getId() : MapaConfig.CIDADE_NATAL.getLocalId();
    }

    private void garantirLocalDoMapa(String localId) {
        Local atual = jogo.getLocalAtual();
        if (atual == null || !localId.equals(atual.getId())) {
            Local alvo = buscarSubLocalRecursivo(jogo.getMapaGlobal(), localId);
            if (alvo != null) {
                jogo.getGameState().setLocalAtual(alvo);
            }
        }
    }

    private void configurarEscalaMapa() {
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(areaMapa.widthProperty());
        clip.heightProperty().bind(areaMapa.heightProperty());
        areaMapa.setClip(clip);

        imgMapaBlurAnterior.setEffect(new GaussianBlur(28));
        imgMapaBlurAnterior.fitWidthProperty().bind(areaMapa.widthProperty());
        imgMapaBlurAnterior.fitHeightProperty().bind(areaMapa.heightProperty());

        imgMapaBlur.setEffect(new GaussianBlur(28));
        imgMapaBlur.fitWidthProperty().bind(areaMapa.widthProperty());
        imgMapaBlur.fitHeightProperty().bind(areaMapa.heightProperty());

        DoubleBinding escala = Bindings.createDoubleBinding(() -> {
            double largura = areaMapa.getWidth();
            double altura = areaMapa.getHeight();
            if (largura <= 0 || altura <= 0) {
                return 1.0;
            }
            return Math.min(largura / LARGURA_MAPA, altura / ALTURA_MAPA);
        }, areaMapa.widthProperty(), areaMapa.heightProperty());

        mapaCanvas.scaleXProperty().bind(escala);
        mapaCanvas.scaleYProperty().bind(escala);

        BooleanBinding temLacuna = Bindings.createBooleanBinding(() -> {
            double largura = areaMapa.getWidth();
            double altura = areaMapa.getHeight();
            if (largura <= 0 || altura <= 0) {
                return false;
            }
            double s = escala.get();
            return (largura - LARGURA_MAPA * s) > 2.0 || (altura - ALTURA_MAPA * s) > 2.0;
        }, areaMapa.widthProperty(), areaMapa.heightProperty(), escala);

        imgMapaBlurAnterior.visibleProperty().bind(temLacuna.and(imgMapaBlurAnterior.imageProperty().isNotNull()));
        imgMapaBlur.visibleProperty().bind(temLacuna);
        blurEscuro.visibleProperty().bind(temLacuna);
    }

    private void configurarIconesHud() {
        IconManager.aplicarBotao(btnMochila, Icone.BAG, 18);
        IconManager.aplicarBotao(btnPausa, Icone.PAUSE, 18);
        IconManager.aplicarBotao(btnFecharViagem, Icone.LEFT_ARROW, 15);
        IconManager.aplicar(lblSaude, Icone.HEART, 17, IconManager.COR_PERIGO);
        IconManager.aplicar(lblEnergia, Icone.SIGNAL_2, 17, IconManager.COR_DESTAQUE);
        IconManager.aplicar(lblDataHora, Icone.CLOCK, 17, IconManager.COR_DESTAQUE);
    }

    private void configurarTeclado() {
        rootPane.setFocusTraversable(true);
        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if (painelViagem.isVisible()) {
                    fecharViagem();
                } else {
                    abrirPausa();
                }
                event.consume();
            } else if (event.getCode() == KeyCode.I && !painelViagem.isVisible()) {
                abrirMochila();
                event.consume();
            }
        });
    }

    private void iniciarMonitorRelogio() {
        registrarRelogioAtual();
        monitorRelogio = new Timeline(new KeyFrame(INTERVALO_MONITOR_RELOGIO, e -> verificarMudancaRelogio()));
        monitorRelogio.setCycleCount(Animation.INDEFINITE);
        monitorRelogio.play();

        rootPane.sceneProperty().addListener((obs, cenaAntiga, cenaNova) -> {
            if (cenaNova == null) {
                pararAnimacoes();
            }
        });
    }

    private void verificarMudancaRelogio() {
        DataDia data = jogo.getDataDia();
        Player player = jogo.getPlayer();
        boolean mudouRelogio = data.getDias() != ultimoDia
                || data.getHoras() != ultimaHora
                || data.getMinutos() != ultimoMinuto;
        boolean mudouStatus = player != null && (player.getAtributos().getSaude() != ultimaSaude
                || player.getAtributos().getEnergia() != ultimaEnergia
                || player.getDinheiro() != ultimoDinheiro);
        if (mudouRelogio || mudouStatus) {
            atualizarEstado();
        }
    }

    private void registrarRelogioAtual() {
        DataDia data = jogo.getDataDia();
        ultimoDia = data.getDias();
        ultimaHora = data.getHoras();
        ultimoMinuto = data.getMinutos();
        Player player = jogo.getPlayer();
        if (player != null) {
            ultimaSaude = player.getAtributos().getSaude();
            ultimaEnergia = player.getAtributos().getEnergia();
            ultimoDinheiro = player.getDinheiro();
        }
    }

    private void pararAnimacoes() {
        if (monitorRelogio != null) {
            monitorRelogio.stop();
        }
        if (transicaoFundo != null) {
            transicaoFundo.stop();
        }
    }

    private void atualizarFundoHorario(boolean animar) {
        if (configAtual == null) {
            return;
        }
        String novoCaminho = configAtual.getImagemPorHora(jogo.getDataDia().getHoras());
        if (novoCaminho.equals(caminhoImagemAtual)) {
            return;
        }

        Image novaImagem = carregarImagem(novoCaminho);
        if (novaImagem == null) {
            return;
        }

        caminhoImagemAtual = novoCaminho;

        if (!animar || imgMapa.getImage() == null) {
            if (transicaoFundo != null) {
                transicaoFundo.stop();
            }
            imgMapaAnterior.setImage(null);
            imgMapaBlurAnterior.setImage(null);
            imgMapa.setImage(novaImagem);
            imgMapa.setOpacity(1.0);
            imgMapaBlur.setImage(novaImagem);
            imgMapaBlur.setOpacity(1.0);
            return;
        }

        if (transicaoFundo != null) {
            transicaoFundo.stop();
        }

        imgMapaAnterior.setImage(imgMapa.getImage());
        imgMapaBlurAnterior.setImage(imgMapaBlur.getImage());

        imgMapa.setImage(novaImagem);
        imgMapa.setOpacity(0.0);
        imgMapaBlur.setImage(novaImagem);
        imgMapaBlur.setOpacity(0.0);

        FadeTransition fadeMapa = new FadeTransition(DURACAO_FADE_FUNDO, imgMapa);
        fadeMapa.setFromValue(0.0);
        fadeMapa.setToValue(1.0);
        fadeMapa.setInterpolator(Interpolator.EASE_BOTH);

        FadeTransition fadeBlur = new FadeTransition(DURACAO_FADE_FUNDO, imgMapaBlur);
        fadeBlur.setFromValue(0.0);
        fadeBlur.setToValue(1.0);
        fadeBlur.setInterpolator(Interpolator.EASE_BOTH);

        transicaoFundo = new ParallelTransition(fadeMapa, fadeBlur);
        transicaoFundo.setOnFinished(e -> {
            imgMapaAnterior.setImage(null);
            imgMapaBlurAnterior.setImage(null);
        });
        transicaoFundo.play();
    }

    private Image carregarImagem(String caminho) {
        return cacheImagens.computeIfAbsent(caminho, key -> {
            URL recurso = getClass().getResource(key);
            return recurso != null ? new Image(recurso.toExternalForm()) : null;
        });
    }

    private void atualizarHud() {
        Player player = jogo.getPlayer();
        DataDia data = jogo.getDataDia();
        registrarRelogioAtual();
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

    private void construirMarcadores() {
        pinsLayer.getChildren().clear();

        for (MapaConfig.Marcador marcador : configAtual.getMarcadores()) {
            Button botao = new Button(marcador.rotulo());
            botao.setFocusTraversable(false);
            botao.setLayoutX(marcador.x());
            botao.setLayoutY(marcador.y());
            botao.getStyleClass().add("mapa-pin");
            if (marcador.viagem()) {
                botao.getStyleClass().add("mapa-pin-viagem");
            }
            IconManager.aplicarBotao(botao, marcador.icone(), 14);

            if (marcador.viagem()) {
                botao.hoverProperty().addListener((obs, antes, hover) ->
                        lblInfoLocal.setText(hover
                                ? marcador.rotulo() + " — VIAJE PARA OUTRAS CIDADES QUANDO ESTIVEREM LIBERADAS."
                                : INFO_PADRAO));
                botao.setOnAction(e -> abrirViagem());
            } else {
                Local local = buscarSubLocal(jogo.getLocalAtual(), marcador.localId());
                boolean bloqueado = isBloqueado(local);
                if (bloqueado) {
                    botao.getStyleClass().add("mapa-pin-bloqueado");
                }
                String descricao = local != null
                        ? local.getNome().toUpperCase() + " — " + local.getDescricao().toUpperCase()
                        : marcador.rotulo();
                String textoHover = bloqueado ? descricao + " (BLOQUEADO NA HISTÓRIA)" : descricao;
                botao.hoverProperty().addListener((obs, antes, hover) ->
                        lblInfoLocal.setText(hover ? textoHover : INFO_PADRAO));
                botao.setOnAction(e -> selecionarLocal(marcador.localId()));
            }

            pinsLayer.getChildren().add(botao);
        }

        AudioManager.aplicarHoverNosBotoes(pinsLayer);
    }

    private void selecionarLocal(String localId) {
        fecharViagemSilencioso();
        if ("abrir_academia".equals(localId)) {
            AudioManager.tocar(Som.CLIQUE);
            SceneManager.irPara(Tela.ACADEMIA_VENDA);
            return;
        }
        Local local = buscarSubLocal(jogo.getLocalAtual(), localId);
        if (local == null) {
            AudioManager.tocar(Som.ERRO);
            return;
        }

        if (isBloqueado(local)) {
            AudioManager.tocar(Som.ERRO);
            lblInfoLocal.setText(local.getNome().toUpperCase() + " AINDA ESTÁ BLOQUEADO PELA HISTÓRIA.");
            return;
        }

        AudioManager.tocar(Som.CLIQUE);
        if ("casa".equals(localId)) {
            jogo.entrarLocal(local);
            SceneManager.irPara(Tela.CASA);
            return;
        }
        if ("academia".equals(localId) || "academia_profissional".equals(localId)) {
            jogo.entrarLocal(local);
            SceneManager.irPara(Tela.ACADEMIA);
            return;
        }
        if ("academia_boxe".equals(localId)) {
            jogo.entrarLocal(local);
            SceneManager.irPara(Tela.ACADEMIA_BOXE);
            return;
        }
        if ("clube_luta".equals(localId)) {
            jogo.entrarLocal(local);
            SceneManager.irPara(Tela.CLUBE_DE_LUTA);
            return;
        }
        if ("loja".equals(localId)) {
            jogo.entrarLocal(local);
            SceneManager.irPara(Tela.LOJA);
            return;
        }
        lblInfoLocal.setText("LOCAL SELECIONADO: " + local.getNome().toUpperCase() + " — " + local.getDescricao().toUpperCase());
    }

    @FXML
    public void abrirViagem() {
        listaDestinos.getChildren().clear();

        for (String id : configAtual.getDestinosViagem()) {
            Local destino = buscarSubLocalRecursivo(jogo.getMapaGlobal(), id);
            if (destino == null) {
                continue;
            }
            String texto = destino.getNome().toUpperCase();
            if (destino.getCustoAcesso() > 0 && !destino.isAcessoLiberado()) {
                texto += "  •  R$ " + destino.getCustoAcesso();
            }

            Button btnDestino = new Button(texto);
            btnDestino.setMaxWidth(Double.MAX_VALUE);
            btnDestino.setFocusTraversable(false);
            btnDestino.getStyleClass().add("viagem-destino");
            IconManager.aplicarBotao(btnDestino, Icone.LOCATION, 14);
            btnDestino.setOnAction(e -> viajarPara(destino));
            listaDestinos.getChildren().add(btnDestino);
        }

        AudioManager.aplicarHoverNosBotoes(listaDestinos);
        AudioManager.tocar(Som.ABRIR_MODAL);
        painelViagem.setVisible(true);
        painelViagem.setManaged(true);
    }

    private void viajarPara(Local destino) {
        int custo = destino.getCustoAcesso();
        if (custo > 0 && !destino.isAcessoLiberado()) {
            Player player = jogo.getPlayer();
            if (player == null || player.getDinheiro() < custo) {
                AudioManager.tocar(Som.ERRO);
                fecharViagemSilencioso();
                lblInfoLocal.setText("DINHEIRO INSUFICIENTE PARA VIAJAR ATÉ "
                        + destino.getNome().toUpperCase() + " (PASSAGEM: R$ " + custo + ").");
                return;
            }
            fecharViagemSilencioso();
            SceneManager.abrirConfirmacao(
                    "COMPRAR PASSAGEM",
                    "PAGAR R$ " + custo + " PARA VIAJAR ATÉ " + destino.getNome().toUpperCase() + "?",
                    "SEU DINHEIRO ATUAL: R$ " + player.getDinheiro() + ". O ACESSO SERÁ LIBERADO PERMANENTEMENTE.",
                    "VIAJAR",
                    () -> {
                        player.setDinheiro(player.getDinheiro() - custo);
                        destino.setAcessoLiberado(true);
                        AudioManager.tocar(Som.CONFIRMAR);
                        carregarMapa(MapaConfig.paraLocal(destino.getId()));
                    }
            );
            return;
        }

        AudioManager.tocar(Som.CONFIRMAR);
        fecharViagemSilencioso();
        carregarMapa(MapaConfig.paraLocal(destino.getId()));
    }

    @FXML
    public void fecharViagem() {
        if (painelViagem.isVisible()) {
            AudioManager.tocar(Som.FECHAR_MODAL);
            fecharViagemSilencioso();
        }
    }

    private void fecharViagemSilencioso() {
        painelViagem.setVisible(false);
        painelViagem.setManaged(false);
    }

    @FXML
    public void abrirMochila() {
        SceneManager.abrirModal(Tela.INVENTARIO);
    }

    @FXML
    public void abrirPausa() {
        SceneManager.abrirMenuPausa(null);
    }

    private boolean isBloqueado(Local local) {
        if (local == null) {
            return false;
        }
        Player player = jogo.getPlayer();
        NarrativaController narrativa = jogo.getNarrativaController();
        return player != null && narrativa != null
                && narrativa.isLocalBloqueadoPelaNarrativa(local.getNome(), player);
    }

    private Local buscarSubLocal(Local origem, String id) {
        if (origem == null || id == null) {
            return null;
        }
        for (Local filho : origem.getSubLocais()) {
            if (id.equals(filho.getId())) {
                return filho;
            }
        }
        return null;
    }

    private Local buscarSubLocalRecursivo(Local origem, String id) {
        if (origem == null || id == null) {
            return null;
        }
        if (id.equals(origem.getId())) {
            return origem;
        }
        for (Local filho : origem.getSubLocais()) {
            Local encontrado = buscarSubLocalRecursivo(filho, id);
            if (encontrado != null) {
                return encontrado;
            }
        }
        return null;
    }
}
