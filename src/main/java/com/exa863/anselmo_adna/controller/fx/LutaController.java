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

    private record PassoLuta(String roundLabel, String emissor, String texto, String banner,
                             int alvoImpacto, boolean mostrarNarrador, int focoLutador) {
    }

    @FXML private StackPane rootPane;
    @FXML private ImageView imgCenario;
    @FXML private Button btnMenuPausa;
    @FXML private Label lblEvento;
    @FXML private Label lblRound;

    @FXML private Label lblNome1;
    @FXML private Label lblEstilo1;
    @FXML private Label lblHp1;
    @FXML private Label lblSt1;
    @FXML private ProgressBar barHp1;
    @FXML private ProgressBar barSt1;
    @FXML private Label lblAttr1;

    @FXML private Label lblNome2;
    @FXML private Label lblEstilo2;
    @FXML private Label lblHp2;
    @FXML private Label lblSt2;
    @FXML private ProgressBar barHp2;
    @FXML private ProgressBar barSt2;
    @FXML private Label lblAttr2;

    @FXML private StackPane palcoLuta;
    @FXML private ImageView imgLutador1;
    @FXML private ImageView imgLutador2;
    @FXML private ImageView imgNarrador;
    @FXML private Label lblBannerCentral;

    @FXML private Label lblEmissor;
    @FXML private Label lblTexto;
    @FXML private VBox boxResumoResultado;
    @FXML private Label lblAviso;
    @FXML private Button btnPular;
    @FXML private Button btnAvancar;

    private final GameController jogo = SceneManager.getGameController();
    private LutaContexto contexto;
    private List<PassoLuta> passos;
    private int indicePasso;
    private boolean resultadoExibido;

    private Timeline digitacao;
    private String textoCompleto = "";
    private int caracteresExibidos;
    private boolean digitacaoPausadaPeloMenu;

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

    private void atualizarVisibilidadePular() {
        boolean visivel = !resultadoExibido && pularAnimacaoHabilitado();
        btnPular.setVisible(visivel);
        btnPular.setManaged(visivel);
    }

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

    private String resolverImagemPlayer() {
        Player player = jogo.getPlayer();
        if (player == null) {
            return "/images/personagens/masculino_castanho.png";
        }
        String sexo = player.getSexo() == Sexo.FEMININO ? "feminino" : "masculino";
        Cores cor = player.getCorOlhos() != null ? player.getCorOlhos() : Cores.CASTANHO;
        return "/images/personagens/" + sexo + "_" + cor.name().toLowerCase() + ".png";
    }

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

    private int detectarAlvoImpacto(String texto, String nome1, String nome2) {
        int pos1 = texto.indexOf(nome1);
        int pos2 = texto.indexOf(nome2);
        if (pos1 >= 0 && pos2 >= 0) {
            return pos1 < pos2 ? 2 : 1;
        }
        return 0;
    }

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
        String variante = jogo.getPlayer().getSexo() == Sexo.FEMININO ? "feminino" : "masculino";
        boolean mundial = MundialFx.EVENTO.equals(contexto.tituloEvento());
        if (verificarPrimeirosPassos(resultado)) {
            if (!mundial) mostrarIlustracao("cena_04_" + variante);
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

    private void mostrarIlustracao(String nome) {
        imgCenario.setImage(new Image(getClass().getResource("/images/cutscenes/" + nome + ".png").toExternalForm()));
        ajustarRecorteCenario();
        palcoLuta.setVisible(false);
    }

    private Label criarLinhaResumo(String texto, String classeCss) {
        Label label = new Label(texto);
        label.setWrapText(true);
        label.getStyleClass().add(classeCss);
        return label;
    }

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

    private void salvarAutomaticamente() {
        try {
            SaveGameController saves = new SaveGameController();
            if (saves.autoSaveHabilitado()) {
                saves.autoSave(jogo.getGameState());
            }
        } catch (FalhaSalvamentoException ignored) {
        }
    }

    private boolean pularAnimacaoHabilitado() {
        Configuracao config = new Configuracao();
        try {
            new ConfiguracaoXML().carregar(config);
            return config.pularAnimacaoCombate;
        } catch (FalhaCarregamentoException | DadosInvalidosException e) {
            return false;
        }
    }

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

    private void ocultarBanner() {
        lblBannerCentral.setVisible(false);
        lblBannerCentral.setManaged(false);
    }

    private void animarImpacto(Node alvo) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(55), alvo);
        shake.setFromX(0);
        shake.setByX(alvo == imgLutador2 ? 14 : -14);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.setOnFinished(e -> alvo.setTranslateX(0));
        shake.play();
    }

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

    @FXML
    public void pular() {
        if (!resultadoExibido && pularAnimacaoHabilitado()) {
            AudioManager.tocar(Som.CLIQUE);
            exibirResultadoOficial();
        }
    }

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

    private void concluirDigitacao() {
        pararDigitacao();
        lblTexto.setText(textoCompleto);
    }

    private void pararDigitacao() {
        if (digitacao != null) {
            digitacao.stop();
            digitacao = null;
        }
    }
}
