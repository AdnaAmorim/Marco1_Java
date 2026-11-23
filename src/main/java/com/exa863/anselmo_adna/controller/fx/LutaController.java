package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.controller.ConquistaController;
import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SaveGameController;
import com.exa863.anselmo_adna.exception.DadosInvalidosException;
import com.exa863.anselmo_adna.exception.FalhaCarregamentoException;
import com.exa863.anselmo_adna.exception.FalhaSalvamentoException;
import com.exa863.anselmo_adna.model.character.Cores;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.character.Sexo;
import com.exa863.anselmo_adna.model.combat.AtributosEfetivos;
import com.exa863.anselmo_adna.model.combat.Lutador;
import com.exa863.anselmo_adna.model.combat.ResultadoLuta;
import com.exa863.anselmo_adna.model.combat.Round;
import com.exa863.anselmo_adna.persistencia.Configuracao;
import com.exa863.anselmo_adna.persistencia.ConfiguracaoXML;
import com.exa863.anselmo_adna.persistencia.Conquistas;
import com.exa863.anselmo_adna.view.fx.Icone;
import com.exa863.anselmo_adna.view.fx.LutaContexto;
import com.exa863.anselmo_adna.view.fx.Som;
import com.exa863.anselmo_adna.view.fx.Tela;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Tela genérica de luta em estilo cutscene/visual novel.
 * Reproduz a apresentação, os rounds narrados e o resultado oficial de qualquer {@link LutaContexto}.
 *
 * @author Anselmo e Adna
 */
public class LutaController {

    /** Fala e indicações visuais de uma etapa do combate. */
    private record PassoLuta(String roundLabel, String emissor, String texto, String banner,
                             int alvoImpacto, boolean mostrarNarrador, int focoLutador) {
    }

    /** Painel principal da tela. */
    @FXML private StackPane rootPane;
    /** Imagem — cenário. */
    @FXML private ImageView imgCenario;
    /** Botão — menu de pausa. */
    @FXML private Button btnMenuPausa;
    /** Texto — evento. */
    @FXML private Label lblEvento;
    /** Texto exibido — round. */
    @FXML private Label lblRound;

    /** Texto — nome 1. */
    @FXML private Label lblNome1;
    /** Texto — estilo 1. */
    @FXML private Label lblEstilo1;
    /** Texto — saúde 1. */
    @FXML private Label lblHp1;
    /** Texto — energia 1. */
    @FXML private Label lblSt1;
    /** Indicador — saúde 1. */
    @FXML private ProgressBar barHp1;
    /** Indicador — energia 1. */
    @FXML private ProgressBar barSt1;
    /** Texto exibido — atributos 1. */
    @FXML private Label lblAttr1;

    /** Texto — nome 2. */
    @FXML private Label lblNome2;
    /** Texto — estilo 2. */
    @FXML private Label lblEstilo2;
    /** Texto — saúde 2. */
    @FXML private Label lblHp2;
    /** Texto — energia 2. */
    @FXML private Label lblSt2;
    /** Indicador — saúde 2. */
    @FXML private ProgressBar barHp2;
    /** Indicador — energia 2. */
    @FXML private ProgressBar barSt2;
    /** Texto exibido — atributos 2. */
    @FXML private Label lblAttr2;

    /** Palco luta. */
    @FXML private StackPane palcoLuta;
    /** Imagem — lutador 1. */
    @FXML private ImageView imgLutador1;
    /** Imagem — lutador 2. */
    @FXML private ImageView imgLutador2;
    /** Imagem — narrador. */
    @FXML private ImageView imgNarrador;
    /** Texto exibido — banner central. */
    @FXML private Label lblBannerCentral;

    /** Texto — emissor. */
    @FXML private Label lblEmissor;
    /** Texto — texto. */
    @FXML private Label lblTexto;
    /** Painel — resumo resultado. */
    @FXML private VBox boxResumoResultado;
    /** Texto — aviso. */
    @FXML private Label lblAviso;
    /** Botão — pular. */
    @FXML private Button btnPular;
    /** Botão — avancar. */
    @FXML private Button btnAvancar;

    /** Controlador da partida compartilhado pela interface. */
    private final GameController jogo = SceneManager.getGameController();
    /** Contexto. */
    private LutaContexto contexto;
    /** Passos. */
    private List<PassoLuta> passos;
    /** Índice passo. */
    private int indicePasso;
    /** Resultado exibido. */
    private boolean resultadoExibido;

    /** Digitação. */
    private Timeline digitacao;
    /** Texto completo. */
    private String textoCompleto = "";
    /** Caracteres exibidos. */
    private int caracteresExibidos;
    /** Digitação pausada pelo menu. */
    private boolean digitacaoPausadaPeloMenu;

    /** Prepara os componentes da tela após o carregamento do FXML. */
    @FXML
    public void initialize() {
        rootPane.setFocusTraversable(true);
        IconManager.aplicarBotao(btnMenuPausa, Icone.PAUSE, 16);

        imgCenario.fitWidthProperty().bind(rootPane.widthProperty());
        imgCenario.fitHeightProperty().bind(rootPane.heightProperty());
        rootPane.widthProperty().addListener((obs, a, d) -> ajustarRecorteCenario());
        rootPane.heightProperty().addListener((obs, a, d) -> ajustarRecorteCenario());

        var alturaSprite = javafx.beans.binding.Bindings.max(160.0, palcoLuta.heightProperty().multiply(0.96));
        imgLutador1.fitHeightProperty().bind(alturaSprite);
        imgLutador2.fitHeightProperty().bind(alturaSprite);
        imgNarrador.fitHeightProperty().bind(alturaSprite);

        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                abrirMenuPausa();
                event.consume();
            } else if (!(event.getTarget() instanceof Button)
                    && (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.SPACE)) {
                avancar();
                event.consume();
            }
        });

        rootPane.sceneProperty().addListener((obs, antes, depois) -> {
            if (depois == null) {
                pararDigitacao();
            }
        });
    }

    /** Prepara a apresentação da luta a partir do contexto recebido. */
    public void iniciar(LutaContexto contexto) {
        this.contexto = contexto;
        this.resultadoExibido = false;

        lblEvento.setText(contexto.tituloEvento().toUpperCase());
        carregarImagens();
        preencherHudLutadores();
        construirRoteiro();
        atualizarVisibilidadePular();

        indicePasso = 0;
        mostrarPassoAtual();
        Platform.runLater(rootPane::requestFocus);
    }

    /** Atualiza visibilidade do botão de pular. */
    private void atualizarVisibilidadePular() {
        boolean visivel = !resultadoExibido && pularAnimacaoHabilitado();
        btnPular.setVisible(visivel);
        btnPular.setManaged(visivel);
    }

    /** Carrega imagens. */
    private void carregarImagens() {
        URL recCenario = getClass().getResource(contexto.imagemCenario());
        if (recCenario != null) {
            imgCenario.setImage(new Image(recCenario.toExternalForm()));
            ajustarRecorteCenario();
        }

        String caminhoPlayer = resolverImagemPlayer();
        carregarSprite(imgLutador1, caminhoPlayer, 0.04, 0.02, 0.92, 0.68);
        carregarSprite(imgLutador2, contexto.imagemOponente(), 0.08, 0.08, 0.84, 0.66);
        carregarSprite(imgNarrador, contexto.imagemNarrador(), 0.04, 0.0, 0.92, 0.72);
    }

    /** Determina imagem do protagonista. */
    private String resolverImagemPlayer() {
        Player player = jogo.getPlayer();
        if (player == null) {
            return "/images/personagens/masculino_castanho.png";
        }
        String sexo = player.getSexo() == Sexo.FEMININO ? "feminino" : "masculino";
        Cores cor = player.getCorOlhos() != null ? player.getCorOlhos() : Cores.CASTANHO;
        return "/images/personagens/" + sexo + "_" + cor.name().toLowerCase() + ".png";
    }

    /** Carrega sprite. */
    private void carregarSprite(ImageView alvo, String caminho, double xPct, double yPct, double wPct, double hPct) {
        if (caminho == null) {
            return;
        }
        URL recurso = getClass().getResource(caminho);
        if (recurso == null) {
            return;
        }
        Image imagem = new Image(recurso.toExternalForm());
        alvo.setImage(imagem);
        double w = imagem.getWidth();
        double h = imagem.getHeight();
        if (w > 0 && h > 0) {
            alvo.setViewport(new Rectangle2D(w * xPct, h * yPct, w * wPct, h * hPct));
        }
    }

    /** Ajusta recorte do cenário. */
    private void ajustarRecorteCenario() {
        Image imagem = imgCenario.getImage();
        if (imagem == null || rootPane.getWidth() <= 0 || rootPane.getHeight() <= 0) {
            return;
        }
        double largura = imagem.getWidth();
        double altura = imagem.getHeight();
        double proporcaoJanela = rootPane.getWidth() / rootPane.getHeight();
        double proporcaoImagem = largura / altura;
        if (proporcaoImagem > proporcaoJanela) {
            double visivel = altura * proporcaoJanela;
            imgCenario.setViewport(new Rectangle2D((largura - visivel) / 2, 0, visivel, altura));
        } else {
            double visivel = largura / proporcaoJanela;
            imgCenario.setViewport(new Rectangle2D(0, (altura - visivel) / 2, largura, visivel));
        }
    }

    /** Preenche indicadores dos dois lutadores. */
    private void preencherHudLutadores() {
        Lutador l1 = contexto.lutador1();
        Lutador l2 = contexto.lutador2();
        AtributosEfetivos e1 = l1.getAtributosEfetivos();
        AtributosEfetivos e2 = l2.getAtributosEfetivos();

        lblNome1.setText(l1.getNome().toUpperCase());
        lblEstilo1.setText("[" + l1.getEstiloLuta().getNome().toUpperCase() + "]");
        lblHp1.setText(Math.round(e1.saude) + " HP");
        lblSt1.setText(Math.round(e1.energia) + " ST");
        barHp1.setProgress(Math.clamp(e1.saude / 100.0, 0.0, 1.0));
        barSt1.setProgress(Math.clamp(e1.energia / 100.0, 0.0, 1.0));
        lblAttr1.setText(String.format("FOR %.0f | AGI %.0f | RES %.0f", e1.forca, e1.agilidade, e1.resistencia));

        lblNome2.setText(l2.getNome().toUpperCase());
        lblEstilo2.setText("[" + l2.getEstiloLuta().getNome().toUpperCase() + "]");
        lblHp2.setText(Math.round(e2.saude) + " HP");
        lblSt2.setText(Math.round(e2.energia) + " ST");
        barHp2.setProgress(Math.clamp(e2.saude / 100.0, 0.0, 1.0));
        barSt2.setProgress(Math.clamp(e2.energia / 100.0, 0.0, 1.0));
        lblAttr2.setText(String.format("FOR %.0f | AGI %.0f | RES %.0f", e2.forca, e2.agilidade, e2.resistencia));
    }

    /** Transforma os rounds da partida já calculada em falas e indicações de impacto para a animação. */
    private void construirRoteiro() {
        passos = new ArrayList<>();
        Lutador l1 = contexto.lutador1();
        Lutador l2 = contexto.lutador2();
        String dataHoje = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        passos.add(new PassoLuta(
                "APRESENTAÇÃO", "LOCUTOR",
                "SENHORAS E SENHORES! BOA NOITE! NA NOITE DE HOJE, " + dataHoje
                        + ", PRESENCIAREMOS UM COMBATE HISTÓRICO!",
                "GRANDE COMBATE", 0, true, 0));

        passos.add(new PassoLuta(
                "APRESENTAÇÃO", "LOCUTOR",
                "NO CÓRNER ESQUERDO, UM MESTRE DO " + l1.getEstiloLuta().getNome().toUpperCase()
                        + "... O TEMÍVEL " + l1.getNome().toUpperCase() + "!",
                null, 0, true, 1));

        passos.add(new PassoLuta(
                "APRESENTAÇÃO", "LOCUTOR",
                "E NO CÓRNER DIREITO, COM DETERMINAÇÃO NO " + l2.getEstiloLuta().getNome().toUpperCase()
                        + "... O FORMIDÁVEL " + l2.getNome().toUpperCase() + "!",
                null, 0, true, 2));

        String nome1Upper = l1.getNome().toUpperCase();
        String nome2Upper = l2.getNome().toUpperCase();

        for (Round round : contexto.match()) {
            String tituloRound = round.isUltimo() ? "DESFECHO" : "ROUND " + round.getNumero();

            if (!round.isUltimo()) {
                String textoGongo = round.getNumero() == 1
                        ? "OS LUTADORES TOCAM AS LUVAS NO CENTRO DO RINGUE... 3... 2... 1... FIGHT!"
                        : "[GONGO SOA] PREPAREM-SE PARA O ROUND " + round.getNumero() + "! FIGHT!";
                passos.add(new PassoLuta(
                        tituloRound, "ÁRBITRO", textoGongo,
                        "ROUND " + round.getNumero() + " • FIGHT!", 0, false, 0));
            }

            for (String evento : round.getEventos()) {
                String texto = evento.toUpperCase();
                int impacto = detectarAlvoImpacto(texto, nome1Upper, nome2Upper);
                String banner = round.isUltimo() ? "FIM DA LUTA!" : null;
                passos.add(new PassoLuta(tituloRound, "NARRADOR", texto, banner, impacto, false, 0));
            }

            if (!round.isUltimo()) {
                passos.add(new PassoLuta(
                        tituloRound, "ÁRBITRO",
                        "DING! FIM DO ROUND " + round.getNumero()
                                + "! OS LUTADORES VÃO PARA SEUS CÓRNERS E RESPIRAM FUNDO.",
                        "FIM DO ROUND " + round.getNumero(), 0, true, 0));
            }
        }
    }

    /** Identifica lutador que recebe o efeito de impacto. */
    private int detectarAlvoImpacto(String texto, String nome1, String nome2) {
        int pos1 = texto.indexOf(nome1);
        int pos2 = texto.indexOf(nome2);
        if (pos1 >= 0 && pos2 >= 0) {
            return pos1 < pos2 ? 2 : 1;
        }
        return 0;
    }

    /** Mostra etapa atual da narração da luta. */
    private void mostrarPassoAtual() {
        if (indicePasso >= passos.size()) {
            exibirResultadoOficial();
            return;
        }

        PassoLuta passo = passos.get(indicePasso);
        lblRound.setText(passo.roundLabel());
        lblEmissor.setText(passo.emissor());
        btnAvancar.setText(indicePasso == passos.size() - 1 ? "VER RESULTADO  →" : "CONTINUAR  →");

        atualizarPresencaPalco(passo.mostrarNarrador(), passo.focoLutador());

        if (passo.banner() != null) {
            mostrarBanner(passo.banner());
        } else {
            ocultarBanner();
        }

        if (passo.alvoImpacto() == 1) {
            animarImpacto(imgLutador1);
        } else if (passo.alvoImpacto() == 2) {
            animarImpacto(imgLutador2);
        }

        mostrarDigitando(passo.texto());
    }

    /** Atualiza visibilidade dos personagens no palco. */
    private void atualizarPresencaPalco(boolean mostrarNarrador, int focoLutador) {
        imgNarrador.setVisible(mostrarNarrador);
        imgNarrador.setManaged(mostrarNarrador);
        if (mostrarNarrador) {
            FadeTransition fade = new FadeTransition(Duration.millis(220), imgNarrador);
            fade.setFromValue(Math.min(imgNarrador.getOpacity(), 0.4));
            fade.setToValue(1.0);
            fade.play();
        }

        double op1 = (focoLutador == 2) ? 0.48 : 1.0;
        double op2 = (focoLutador == 1) ? 0.48 : 1.0;
        imgLutador1.setOpacity(op1);
        imgLutador2.setOpacity(op2);
        imgLutador1.setScaleX(focoLutador == 1 ? 1.05 : 1.0);
        imgLutador1.setScaleY(focoLutador == 1 ? 1.05 : 1.0);
        imgLutador2.setScaleX(focoLutador == 2 ? 1.05 : 1.0);
        imgLutador2.setScaleY(focoLutador == 2 ? 1.05 : 1.0);
    }

    /** Aplica as consequências da luta uma única vez, inclusive quando o jogador pula a animação. */
    private void exibirResultadoOficial() {
        if (resultadoExibido) {
            return;
        }
        resultadoExibido = true;
        pararDigitacao();

        ResultadoLuta resultado = contexto.match().getResultadoLuta();
        lblRound.setText("FIM DA LUTA");
        lblEmissor.setText("RESULTADO OFICIAL");
        lblTexto.setText(resultado.getDescricao().toUpperCase());

        String textoBanner;
        if (resultado.isVencedor(contexto.lutador1())) {
            textoBanner = "VITÓRIA!";
            atualizarPresencaPalco(true, 1);
            AudioManager.tocar(Som.CONFIRMAR);
        } else if (resultado.isEmpate()) {
            textoBanner = "EMPATE!";
            atualizarPresencaPalco(true, 0);
            AudioManager.tocar(Som.NOTIFICACAO);
        } else {
            textoBanner = "DERROTA!";
            atualizarPresencaPalco(true, 2);
            AudioManager.tocar(Som.ERRO);
        }
        mostrarBanner(textoBanner);

        List<Label> linhas = new ArrayList<>();
        String variante = jogo.getPlayer() != null && jogo.getPlayer().getSexo() == Sexo.FEMININO
                ? "feminino" : "masculino";
        boolean mundial = MundialFx.EVENTO.equals(contexto.tituloEvento());
        if (verificarPrimeirosPassos(resultado)) {
            if (!mundial) {
                mostrarIlustracao("cena_04_" + variante);
            }
            linhas.add(criarLinhaResumo("CONQUISTA DESBLOQUEADA: " + Conquistas.getNome(Conquistas.PRIMEIROS_PASSOS).toUpperCase(),
                    "luta-resumo-pos"));
        }

        if (contexto.aoFinalizar() != null) {
            List<String> resumo = contexto.aoFinalizar().apply(resultado);
            if (resumo != null) {
                for (String linha : resumo) {
                    String classe = linha.startsWith("+") ? "luta-resumo-pos"
                            : (linha.startsWith("!") ? "luta-resumo-alerta" : "luta-resumo-info");
                    String limpa = (linha.startsWith("+") || linha.startsWith("!")) ? linha.substring(1).trim() : linha;
                    linhas.add(criarLinhaResumo(limpa.toUpperCase(), classe));
                }
            }
        }

        salvarAutomaticamente();
        if (mundial) {
            MundialFx.Final fim = MundialFx.desfecho(jogo.getPlayer(), resultado.isVencedor(contexto.lutador1()));
            mostrarIlustracao("final_" + fim.numero + "_" + variante);
            lblRound.setText("FINAL " + fim.numero);
            lblEmissor.setText(fim.titulo.toUpperCase());
            lblTexto.setText(fim.texto);
        }

        if (!linhas.isEmpty()) {
            boxResumoResultado.getChildren().setAll(linhas);
            boxResumoResultado.setVisible(true);
            boxResumoResultado.setManaged(true);
        }

        atualizarVisibilidadePular();
        btnAvancar.setText("ENCERRAR COMBATE  →");
        lblAviso.setText("ENTER OU CLIQUE PARA VOLTAR");
    }

    /** Mostra ilustracao. */
    private void mostrarIlustracao(String nome) {
        URL recurso = getClass().getResource("/images/cutscenes/" + nome + ".png");
        if (recurso != null) {
            imgCenario.setImage(new Image(recurso.toExternalForm()));
            ajustarRecorteCenario();
        }
        palcoLuta.setVisible(false);
    }
    /** Cria linha do resumo do combate. */
    private Label criarLinhaResumo(String texto, String classeCss) {
        Label label = new Label(texto);
        label.setWrapText(true);
        label.getStyleClass().add(classeCss);
        return label;
    }

    /** Verifica condições da conquista Primeiros Passos. */
    private boolean verificarPrimeirosPassos(ResultadoLuta resultado) {
        if (!resultado.isVencedor(contexto.lutador1())) {
            return false;
        }
        try {
            return new ConquistaController().desbloquear(Conquistas.PRIMEIROS_PASSOS);
        } catch (FalhaCarregamentoException | FalhaSalvamentoException e) {
            return false;
        }
    }

    /** Salva automaticamente. */
    private void salvarAutomaticamente() {
        try {
            SaveGameController saves = new SaveGameController();
            if (saves.autoSaveHabilitado()) {
                saves.autoSave(jogo.getGameState());
            }
        } catch (FalhaSalvamentoException ignored) {
        }
    }

    /** Consulta a preferência de pular a animação de combate. */
    private boolean pularAnimacaoHabilitado() {
        Configuracao config = new Configuracao();
        try {
            new ConfiguracaoXML().carregar(config);
            return config.pularAnimacaoCombate;
        } catch (FalhaCarregamentoException | DadosInvalidosException e) {
            return false;
        }
    }

    /** Mostra banner. */
    private void mostrarBanner(String texto) {
        lblBannerCentral.setText(texto);
        lblBannerCentral.setVisible(true);
        lblBannerCentral.setManaged(true);

        FadeTransition fade = new FadeTransition(Duration.millis(220), lblBannerCentral);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();

        ScaleTransition scale = new ScaleTransition(Duration.millis(240), lblBannerCentral);
        scale.setFromX(0.82);
        scale.setFromY(0.82);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.play();
    }

    /** Oculta banner. */
    private void ocultarBanner() {
        lblBannerCentral.setVisible(false);
        lblBannerCentral.setManaged(false);
    }

    /** Anima efeito de impacto do golpe. */
    private void animarImpacto(Node alvo) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(55), alvo);
        shake.setFromX(0);
        shake.setByX(alvo == imgLutador2 ? 14 : -14);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.setOnFinished(e -> alvo.setTranslateX(0));
        shake.play();
    }

    /** Continua a sequência exibida na tela. */
    @FXML
    public void avancar() {
        if (digitacao != null && digitacao.getStatus() == Animation.Status.RUNNING) {
            concluirDigitacao();
            return;
        }
        if (resultadoExibido) {
            AudioManager.tocar(Som.CLIQUE);
            Tela retorno = contexto.telaRetorno() != null ? contexto.telaRetorno() : Tela.MAPA;
            // Se a luta liberou o próximo capítulo no local atual, mostra a história antes do retorno.
            if (SceneManager.iniciarCapituloSeDisponivel(jogo.getLocalAtual(), retorno)) return;
            SceneManager.irPara(retorno);
            return;
        }
        AudioManager.tocar(Som.CLIQUE);
        indicePasso++;
        mostrarPassoAtual();
    }

    /** Avança para o próximo ponto permitido da sequência. */
    @FXML
    public void pular() {
        if (!resultadoExibido && pularAnimacaoHabilitado()) {
            AudioManager.tocar(Som.CLIQUE);
            exibirResultadoOficial();
        }
    }

    /** Abre menu de pausa. */
    @FXML
    public void abrirMenuPausa() {
        if (SceneManager.pausaAtiva()) {
            return;
        }
        digitacaoPausadaPeloMenu = digitacao != null && digitacao.getStatus() == Animation.Status.RUNNING;
        if (digitacaoPausadaPeloMenu) {
            digitacao.pause();
        }
        boolean abriu = SceneManager.abrirMenuPausa(() -> {
            atualizarVisibilidadePular();
            if (digitacaoPausadaPeloMenu && digitacao != null && digitacao.getStatus() == Animation.Status.PAUSED) {
                digitacao.play();
            }
            digitacaoPausadaPeloMenu = false;
            Platform.runLater(rootPane::requestFocus);
        });
        if (!abriu && digitacaoPausadaPeloMenu && digitacao != null) {
            digitacao.play();
            digitacaoPausadaPeloMenu = false;
        }
    }

    /** Revela o texto aos poucos na caixa de diálogo. */
    private void mostrarDigitando(String texto) {
        pararDigitacao();
        textoCompleto = texto;
        caracteresExibidos = 0;
        lblTexto.setText("");
        digitacao = new Timeline(new KeyFrame(Duration.millis(22), event -> {
            caracteresExibidos = Math.min(caracteresExibidos + 1, textoCompleto.length());
            lblTexto.setText(textoCompleto.substring(0, caracteresExibidos));
            if (caracteresExibidos >= textoCompleto.length()) {
                pararDigitacao();
            }
        }));
        digitacao.setCycleCount(Timeline.INDEFINITE);
        digitacao.play();
    }

    /** Conclui exibição gradual do texto. */
    private void concluirDigitacao() {
        pararDigitacao();
        lblTexto.setText(textoCompleto);
    }

    /** Interrompe exibição gradual do texto. */
    private void pararDigitacao() {
        if (digitacao != null) {
            digitacao.stop();
            digitacao = null;
        }
    }
}
