package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.narrativa.Dialogos;
import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.model.world.DataDia;
import com.exa863.anselmo_adna.view.fx.Icone;
import com.exa863.anselmo_adna.view.fx.Som;
import com.exa863.anselmo_adna.view.fx.Tela;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Cena em estilo visual novel da casa do atleta (quarto e academia caseira).
 * Permite dormir, treinar no saco caseiro, consultar atributos e voltar ao mapa.
 *
 * @author Anselmo e Adna
 */
public class CasaController {

    /** Largura cenário. */
    private static final double LARGURA_CENARIO = 960.0;
    /** Altura cenário. */
    private static final double ALTURA_CENARIO = 540.0;
    /** Chance fala avos. */
    private static final int CHANCE_FALA_AVOS = 40;
    /** Duração fade fundo. */
    private static final Duration DURACAO_FADE_FUNDO = Duration.millis(550);

    /** Imagem quarto. */
    private static final String IMG_QUARTO = "/images/locais/Quarto.png";
    /** Imagem academia casa. */
    private static final String IMG_ACADEMIA_CASA = "/images/locais/Academia_Casa.png";

    /** Painel principal da tela. */
    @FXML private StackPane rootPane;
    /** Area cenário. */
    @FXML private StackPane areaCenario;
    /** Imagem — fundo desfocado anterior. */
    @FXML private ImageView imgFundoBlurAnterior;
    /** Imagem — fundo desfocado. */
    @FXML private ImageView imgFundoBlur;
    /** Desfocado escuro. */
    @FXML private Region blurEscuro;
    /** Cenário canvas. */
    @FXML private Pane cenarioCanvas;
    /** Imagem — fundo anterior. */
    @FXML private ImageView imgFundoAnterior;
    /** Imagem — fundo. */
    @FXML private ImageView imgFundo;
    /** Cortina sono. */
    @FXML private Region cortinaSono;

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
    /** Texto — treinos. */
    @FXML private Label lblTreinos;
    /** Texto — texto. */
    @FXML private Label lblTexto;
    /** Painel — detalhes. */
    @FXML private VBox boxDetalhes;
    /** Box opções. */
    @FXML private VBox boxOpcoes;

    /** Controlador da partida compartilhado pela interface. */
    private final GameController jogo = SceneManager.getGameController();
    /** Gerador usado nos sorteios da tela. */
    private final Random random = new Random();
    /** Cache imagens. */
    private final Map<String, Image> cacheImagens = new HashMap<>();

    /** Caminho imagem atual. */
    private String caminhoImagemAtual;
    /** Transição fundo. */
    private ParallelTransition transicaoFundo;
    /** Transição sono. */
    private SequentialTransition transicaoSono;

    /** Prepara os componentes da tela após o carregamento do FXML. */
    @FXML
    public void initialize() {
        configurarEscalaCenario();
        configurarIconesHud();
        configurarTeclado();
        atualizarHud();
        trocarFundo(IMG_QUARTO, false);
        exibirMenuPrincipal(true);
    }

    /** Configura escala do cenário para o tamanho da janela. */
    private void configurarEscalaCenario() {
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(areaCenario.widthProperty());
        clip.heightProperty().bind(areaCenario.heightProperty());
        areaCenario.setClip(clip);

        imgFundoBlurAnterior.setEffect(new GaussianBlur(28));
        imgFundoBlurAnterior.fitWidthProperty().bind(areaCenario.widthProperty());
        imgFundoBlurAnterior.fitHeightProperty().bind(areaCenario.heightProperty());

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

        imgFundoBlurAnterior.visibleProperty().bind(temLacuna.and(imgFundoBlurAnterior.imageProperty().isNotNull()));
        imgFundoBlur.visibleProperty().bind(temLacuna);
        blurEscuro.visibleProperty().bind(temLacuna);

        rootPane.sceneProperty().addListener((obs, antiga, nova) -> {
            if (nova == null) {
                pararAnimacoes();
            }
        });
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

    /** Interrompe animações. */
    private void pararAnimacoes() {
        if (transicaoFundo != null) {
            transicaoFundo.stop();
        }
        if (transicaoSono != null) {
            transicaoSono.stop();
        }
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
            lblTreinos.setText("TREINOS HOJE: 0 / " + Player.MAX_TREINOS_POR_DIA);
            return;
        }

        Atributo attr = player.getAtributos();
        lblAtleta.setText(player.getNome().toUpperCase());
        lblSaude.setText(attr.getSaude() + "/100");
        lblEnergia.setText(attr.getEnergia() + "/100");
        lblDinheiro.setText("R$ " + player.getDinheiro());
        lblTreinos.setText("TREINOS HOJE: " + player.getTreinosRestantes(data.getDias())
                + " / " + Player.MAX_TREINOS_POR_DIA);
    }

    /** Exibe opções principais da tela. */
    private void exibirMenuPrincipal(boolean sortearFalaAvos) {
        trocarFundo(IMG_QUARTO, true);
        limparDetalhes();
        atualizarHud();

        if (sortearFalaAvos && random.nextInt(100) < CHANCE_FALA_AVOS) {
            lblFalante.setText("AVÓS");
            lblTexto.setText(Dialogos.obterDialogoAleatorioAvos().toUpperCase());
        } else {
            lblFalante.setText("QUARTO • CASA DO ATLETA");
            lblTexto.setText("SEU REFÚGIO PARA DESCANSAR, REVISAR SEUS ATRIBUTOS E TREINAR NO SACO CASEIRO.");
        }

        boxOpcoes.getChildren().setAll(
                criarBotaoOpcao("DORMIR E DESCANSAR (+8H)", Icone.CLOCK, false, this::dormir),
                criarBotaoOpcao("TREINAR NO SACO (-25 EN)", Icone.STAR, false, this::treinarEmCasa),
                criarBotaoOpcao("VER ATRIBUTOS", Icone.PERSON, false, this::verAtributos),
                criarBotaoOpcao("SAIR DE CASA", Icone.LEFT_ARROW, true, this::sairDeCasa)
        );
        AudioManager.aplicarHoverNosBotoes(boxOpcoes);
    }

    /** Inicia o descanso do jogador em casa. */
    private void dormir() {
        Player player = jogo.getPlayer();
        if (player == null) {
            return;
        }
        Atributo attr = player.getAtributos();

        if (attr.getEnergia() >= Atributo.MAX_ENERGIA) {
            AudioManager.tocar(Som.ERRO);
            lblFalante.setText("SEM SONO");
            lblTexto.setText("VOCÊ ROLOU NA CAMA SEM CONSEGUIR DORMIR. SUA ENERGIA JÁ ESTÁ CHEIA (100/100)!");
            mostrarDetalhes(List.of(
                    criarLinhaDetalhe("GASTE ENERGIA TREINANDO ANTES DE DESCANSAR NOVAMENTE.", "vn-linha-info")
            ));
            definirBotaoVoltar("VOLTAR", () -> exibirMenuPrincipal(false));
            return;
        }

        AudioManager.tocar(Som.CONFIRMAR);
        animarPassagemSono(() -> concluirSono(attr));
    }

    /** Aplica as oito horas de descanso, recupera saúde e energia e sorteia uma possível perda de habilidade. */
    private void concluirSono(Atributo attr) {
        attr.setEnergia(Atributo.MAX_ENERGIA);
        attr.setSaude(Atributo.MAX_SAUDE);
        jogo.getDataDia().avancarMinutos(480);

        String atributoPerdido = null;
        int novoValor = 0;
        if (random.nextInt(100) < 60) {
            List<String> elegiveis = new ArrayList<>();
            if (attr.getForca() > 1) elegiveis.add("FORÇA");
            if (attr.getAgilidade() > 1) elegiveis.add("AGILIDADE");
            if (attr.getResistencia() > 1) elegiveis.add("RESISTÊNCIA");
            if (attr.getInteligencia() > 1) elegiveis.add("INTELIGÊNCIA");

            if (!elegiveis.isEmpty()) {
                atributoPerdido = elegiveis.get(random.nextInt(elegiveis.size()));
                switch (atributoPerdido) {
                    case "FORÇA" -> {
                        attr.setForca(attr.getForca() - 1);
                        novoValor = attr.getForca();
                    }
                    case "AGILIDADE" -> {
                        attr.setAgilidade(attr.getAgilidade() - 1);
                        novoValor = attr.getAgilidade();
                    }
                    case "RESISTÊNCIA" -> {
                        attr.setResistencia(attr.getResistencia() - 1);
                        novoValor = attr.getResistencia();
                    }
                    case "INTELIGÊNCIA" -> {
                        attr.setInteligencia(attr.getInteligencia() - 1);
                        novoValor = attr.getInteligencia();
                    }
                }
            }
        }

        atualizarHud();
        lblFalante.setText("DESCANSO REVIGORANTE");
        lblTexto.setText("VOCÊ DORMIU 8 HORAS SEGUIDAS. ENERGIA E SAÚDE FORAM TOTALMENTE RESTAURADAS!");

        List<Label> detalhes = new ArrayList<>();
        detalhes.add(criarLinhaDetalhe("ENERGIA 100/100  •  SAÚDE 100/100  •  "
                + jogo.getDataDia().getDiaFormatado().toUpperCase() + " " + jogo.getDataDia().getHoraFormatada(),
                "vn-linha-positiva"));

        if (atributoPerdido != null) {
            detalhes.add(criarLinhaDetalhe("DESTREINAMENTO: -1 EM " + atributoPerdido
                    + " (ATUAL: " + novoValor + "/10)", "vn-linha-alerta"));
        } else {
            detalhes.add(criarLinhaDetalhe("CONDICIONAMENTO PRESERVADO! NENHUM ATRIBUTO CAIU.", "vn-linha-positiva"));
        }

        mostrarDetalhes(detalhes);
        definirBotaoVoltar("LEVANTAR DA CAMA", () -> exibirMenuPrincipal(false));
    }

    /** Treina em casa. */
    private void treinarEmCasa() {
        Player player = jogo.getPlayer();
        if (player == null) {
            return;
        }
        Atributo attr = player.getAtributos();
        int diaAtual = jogo.getDataDia().getDias();

        trocarFundo(IMG_ACADEMIA_CASA, true);

        // Os treinos em casa e na academia contam para o mesmo limite diário.
        if (!player.podeTreinarHoje(diaAtual)) {
            AudioManager.tocar(Som.ERRO);
            lblFalante.setText("ACADEMIA DA CASA • LIMITE DIÁRIO");
            lblTexto.setText("VOCÊ JÁ COMPLETOU OS " + Player.MAX_TREINOS_POR_DIA + " TREINOS PERMITIDOS HOJE.");
            mostrarDetalhes(List.of(
                    criarLinhaDetalhe("VOLTE AO QUARTO E DURMA PARA AVANÇAR O DIA E RECUPERAR AS ENERGIAS.", "vn-linha-info")
            ));
            definirBotaoVoltar("VOLTAR AO QUARTO", () -> exibirMenuPrincipal(false));
            return;
        }

        if (attr.getEnergia() < 25) {
            AudioManager.tocar(Som.ERRO);
            lblFalante.setText("ACADEMIA DA CASA • SEM ENERGIA");
            lblTexto.setText("VOCÊ NÃO TEM ENERGIA SUFICIENTE PARA RENDER NO SACO CASEIRO (EXIGE 25).");
            mostrarDetalhes(List.of(
                    criarLinhaDetalhe("ENERGIA ATUAL: " + attr.getEnergia() + "/100. DESCANSE NA CAMA PRIMEIRO.", "vn-linha-alerta")
            ));
            definirBotaoVoltar("VOLTAR AO QUARTO", () -> exibirMenuPrincipal(false));
            return;
        }

        AudioManager.tocar(Som.CONFIRMAR);
        attr.setEnergia(attr.getEnergia() - 25);
        jogo.getDataDia().avancarMinutos(120);
        player.registrarTreino(diaAtual);

        List<Label> detalhes = new ArrayList<>();
        if (random.nextInt(100) < 20 && attr.getForca() < Atributo.MAX_HABILIDADE) {
            attr.setForca(attr.getForca() + 1);
            detalhes.add(criarLinhaDetalhe("+1 FORÇA (ATUAL: " + attr.getForca() + "/10)", "vn-linha-positiva"));
        }
        if (random.nextInt(100) < 20 && attr.getAgilidade() < Atributo.MAX_HABILIDADE) {
            attr.setAgilidade(attr.getAgilidade() + 1);
            detalhes.add(criarLinhaDetalhe("+1 AGILIDADE (ATUAL: " + attr.getAgilidade() + "/10)", "vn-linha-positiva"));
        }
        if (random.nextInt(100) < 20 && attr.getResistencia() < Atributo.MAX_HABILIDADE) {
            attr.setResistencia(attr.getResistencia() + 1);
            detalhes.add(criarLinhaDetalhe("+1 RESISTÊNCIA (ATUAL: " + attr.getResistencia() + "/10)", "vn-linha-positiva"));
        }
        if (random.nextInt(100) < 20 && attr.getInteligencia() < Atributo.MAX_HABILIDADE) {
            attr.setInteligencia(attr.getInteligencia() + 1);
            detalhes.add(criarLinhaDetalhe("+1 INTELIGÊNCIA (ATUAL: " + attr.getInteligencia() + "/10)", "vn-linha-positiva"));
        }

        atualizarHud();
        lblFalante.setText("ACADEMIA DA CASA");
        if (detalhes.isEmpty()) {
            lblTexto.setText("VOCÊ BATEU DURO NO SACO DE AREIA, MAS NÃO SENTIU GANHOS TÉCNICOS DESTA VEZ.");
        } else {
            lblTexto.setText("TREINO NO SACO CASEIRO CONCLUÍDO! SEU CORPO RESPONDEU BEM AO ESFORÇO.");
        }
        detalhes.add(criarLinhaDetalhe("-25 ENERGIA  •  +2 HORAS (" + jogo.getDataDia().getHoraFormatada() + ")", "vn-linha-info"));
        mostrarDetalhes(detalhes);

        boxOpcoes.getChildren().setAll(
                criarBotaoOpcao("TREINAR NOVAMENTE (-25 EN)", Icone.STAR, false, this::treinarEmCasa),
                criarBotaoOpcao("VOLTAR AO QUARTO", Icone.LEFT_ARROW, false, () -> exibirMenuPrincipal(false))
        );
        AudioManager.aplicarHoverNosBotoes(boxOpcoes);
    }

    /** Mostra os atributos atuais do jogador. */
    private void verAtributos() {
        Player player = jogo.getPlayer();
        if (player == null) {
            return;
        }
        AudioManager.tocar(Som.CLIQUE);
        Atributo attr = player.getAtributos();

        lblFalante.setText("ATRIBUTOS DO BOXEADOR");
        lblTexto.setText("SITUAÇÃO ATUAL DE " + player.getNome().toUpperCase() + ":");

        VBox grid = new VBox();
        grid.getStyleClass().add("vn-stat-grid");
        grid.getChildren().addAll(
                criarLinhaStat("FORÇA", attr.getForca() + " / 10", "AGILIDADE", attr.getAgilidade() + " / 10"),
                criarLinhaStat("RESISTÊNCIA", attr.getResistencia() + " / 10", "INTELIGÊNCIA", attr.getInteligencia() + " / 10"),
                criarLinhaStat("SAÚDE", attr.getSaude() + " / 100", "ENERGIA", attr.getEnergia() + " / 100")
        );

        boxDetalhes.getChildren().setAll(grid);
        boxDetalhes.setVisible(true);
        boxDetalhes.setManaged(true);

        definirBotaoVoltar("VOLTAR AO QUARTO", () -> exibirMenuPrincipal(false));
    }

    /** Cria linha de atributo do jogador. */
    private HBox criarLinhaStat(String nome1, String valor1, String nome2, String valor2) {
        Label lNome1 = new Label(nome1 + ":");
        lNome1.getStyleClass().add("vn-stat-nome");
        Label lValor1 = new Label(valor1);
        lValor1.getStyleClass().add("vn-stat-valor");

        Region espaco = new Region();
        espaco.setMinWidth(24);

        Label lNome2 = new Label(nome2 + ":");
        lNome2.getStyleClass().add("vn-stat-nome");
        Label lValor2 = new Label(valor2);
        lValor2.getStyleClass().add("vn-stat-valor");

        HBox linha = new HBox(lNome1, lValor1, espaco, lNome2, lValor2);
        linha.getStyleClass().add("vn-stat-linha");
        return linha;
    }

    /** Retorna de casa ao mapa. */
    private void sairDeCasa() {
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

    /** Limpa detalhes. */
    private void limparDetalhes() {
        boxDetalhes.getChildren().clear();
        boxDetalhes.setVisible(false);
        boxDetalhes.setManaged(false);
    }

    /** Anima passagem do tempo durante o sono. */
    private void animarPassagemSono(Runnable aoMeio) {
        if (transicaoSono != null) {
            transicaoSono.stop();
        }
        boxOpcoes.setDisable(true);
        cortinaSono.setVisible(true);
        cortinaSono.setOpacity(0.0);

        FadeTransition escurecer = new FadeTransition(Duration.millis(320), cortinaSono);
        escurecer.setFromValue(0.0);
        escurecer.setToValue(0.92);
        escurecer.setOnFinished(e -> aoMeio.run());

        FadeTransition clarear = new FadeTransition(Duration.millis(360), cortinaSono);
        clarear.setFromValue(0.92);
        clarear.setToValue(0.0);
        clarear.setOnFinished(e -> {
            cortinaSono.setVisible(false);
            boxOpcoes.setDisable(false);
        });

        transicaoSono = new SequentialTransition(escurecer, clarear);
        transicaoSono.play();
    }

    /** Troca imagem de fundo. */
    private void trocarFundo(String novoCaminho, boolean animar) {
        if (novoCaminho.equals(caminhoImagemAtual)) {
            return;
        }
        Image novaImagem = carregarImagem(novoCaminho);
        if (novaImagem == null) {
            return;
        }
        caminhoImagemAtual = novoCaminho;

        if (!animar || imgFundo.getImage() == null) {
            if (transicaoFundo != null) {
                transicaoFundo.stop();
            }
            imgFundoAnterior.setImage(null);
            imgFundoBlurAnterior.setImage(null);
            imgFundo.setImage(novaImagem);
            imgFundo.setOpacity(1.0);
            imgFundoBlur.setImage(novaImagem);
            imgFundoBlur.setOpacity(1.0);
            return;
        }

        if (transicaoFundo != null) {
            transicaoFundo.stop();
        }
        imgFundoAnterior.setImage(imgFundo.getImage());
        imgFundoBlurAnterior.setImage(imgFundoBlur.getImage());

        imgFundo.setImage(novaImagem);
        imgFundo.setOpacity(0.0);
        imgFundoBlur.setImage(novaImagem);
        imgFundoBlur.setOpacity(0.0);

        FadeTransition fadePrincipal = new FadeTransition(DURACAO_FADE_FUNDO, imgFundo);
        fadePrincipal.setFromValue(0.0);
        fadePrincipal.setToValue(1.0);
        fadePrincipal.setInterpolator(Interpolator.EASE_BOTH);

        FadeTransition fadeBlur = new FadeTransition(DURACAO_FADE_FUNDO, imgFundoBlur);
        fadeBlur.setFromValue(0.0);
        fadeBlur.setToValue(1.0);
        fadeBlur.setInterpolator(Interpolator.EASE_BOTH);

        transicaoFundo = new ParallelTransition(fadePrincipal, fadeBlur);
        transicaoFundo.setOnFinished(e -> {
            imgFundoAnterior.setImage(null);
            imgFundoBlurAnterior.setImage(null);
        });
        transicaoFundo.play();
    }

    /** Carrega imagem. */
    private Image carregarImagem(String caminho) {
        return cacheImagens.computeIfAbsent(caminho, key -> {
            URL recurso = getClass().getResource(key);
            return recurso != null ? new Image(recurso.toExternalForm()) : null;
        });
    }
}
