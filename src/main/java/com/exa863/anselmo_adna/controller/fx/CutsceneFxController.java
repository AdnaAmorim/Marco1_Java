package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SaveGameController;
import com.exa863.anselmo_adna.controller.cenas.CutsceneController;
import com.exa863.anselmo_adna.exception.FalhaSalvamentoException;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.narrativa.Capitulo;
import com.exa863.anselmo_adna.model.narrativa.Dialogo;
import com.exa863.anselmo_adna.model.narrativa.TipoEmissor;
import com.exa863.anselmo_adna.model.narrativa.TipoGatilho;
import com.exa863.anselmo_adna.model.world.Local;
import com.exa863.anselmo_adna.view.fx.Som;
import com.exa863.anselmo_adna.view.fx.Tela;
import com.exa863.anselmo_adna.view.fx.Icone;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.exa863.anselmo_adna.model.character.Sexo;
import com.exa863.anselmo_adna.model.character.Cores;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Exibe o prólogo e os dez capítulos do grafo narrativo no mesmo palco JavaFX. */
public class CutsceneFxController {

    private enum Etapa { PROLOGO, CAPITULO, CONCLUIDA }

    @FXML private StackPane rootPane;
    @FXML private ImageView imgCenario;
    @FXML private HBox painelPersonagens;
    @FXML private ImageView imgProtagonista;
    @FXML private ImageView imgInterlocutor;
    @FXML private Label lblParte;
    @FXML private Label lblTitulo;
    @FXML private Label lblProgresso;
    @FXML private Label lblEmissor;
    @FXML private Label lblTexto;
    @FXML private Label lblAviso;
    @FXML private VBox caixaOpcoes;
    @FXML private Button btnAvancar;
    @FXML private Button btnPular;
    @FXML private Button btnMenuPausa;

    private Etapa etapa;
    private final GameController jogo = SceneManager.getGameController();
    private List<PrologoRoteiro.Quadro> quadros;
    private int indiceQuadro;
    private CutsceneController capituloController;
    private List<Dialogo> dialogos;
    private int indiceDialogo;
    private Timeline digitacao;
    private String textoCompleto = "";
    private int caracteresExibidos;
    private boolean finalizado;
    private final Map<String, Image> imagens = new HashMap<>();
    private String imagemAtual;
    private final Map<String, Image> spritesPersonagens = new HashMap<>();
    private String interlocutorAtual;
    private boolean digitacaoPausadaPeloMenu;
    private Tela telaRetorno = Tela.MAPA;

    @FXML
    public void initialize() {
        Player player = jogo.getPlayer();
        if (player == null) {
            throw new IllegalStateException("Crie um personagem antes de abrir a cutscene.");
        }
        rootPane.setFocusTraversable(true);
        IconManager.aplicarBotao(btnMenuPausa, Icone.PAUSE, 18);
        imgCenario.fitWidthProperty().bind(rootPane.widthProperty());
        imgCenario.fitHeightProperty().bind(rootPane.heightProperty());
        // Dimensiona sprites de 340x780 de acordo com a altura da janela.
        imgProtagonista.fitHeightProperty().bind(rootPane.heightProperty().multiply(0.78));
        imgInterlocutor.fitHeightProperty().bind(rootPane.heightProperty().multiply(0.78));
        // Deixa os retratos mais parecidos com a composicao das telas de mapa e locais:
        // o protagonista fica mais para a esquerda e o interlocutor um pouco para a direita.
        imgProtagonista.setTranslateX(-170);
        imgProtagonista.translateYProperty().bind(rootPane.heightProperty().multiply(0.035));
        imgInterlocutor.setTranslateX(95);
        painelPersonagens.spacingProperty().bind(rootPane.widthProperty().multiply(0.10));
        painelPersonagens.setVisible(false);
        painelPersonagens.setManaged(false);
        rootPane.widthProperty().addListener((obs, antes, depois) -> ajustarRecorte());
        rootPane.heightProperty().addListener((obs, antes, depois) -> ajustarRecorte());
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
            if (depois == null) pararDigitacao();
        });
        SceneManager.InicioCapitulo inicio = SceneManager.consumirInicioCapitulo();
        if (inicio != null) {
            telaRetorno = inicio.telaRetorno();
            iniciarCapitulo(inicio.capitulo());
        } else {
            quadros = PrologoRoteiro.criar(player);
            etapa = Etapa.PROLOGO;
            indiceQuadro = 0;
            mostrarQuadro();
        }
    }

    private void mostrarQuadro() {
        ocultarPersonagens(); // O prologo ja possui artes completas, sem retratos sobrepostos.
        if (indiceQuadro >= quadros.size()) {
            iniciarPrimeiroCapitulo();
            return;
        }
        PrologoRoteiro.Quadro quadro = quadros.get(indiceQuadro);
        mostrarImagemPrologo(indiceQuadro);
        lblParte.setText("PRÓLOGO  /  AS ORIGENS");
        lblTitulo.setText(quadro.titulo());
        lblProgresso.setText("CENA " + (indiceQuadro + 1) + " DE " + quadros.size());
        lblEmissor.setText("NARRADOR");
        lblAviso.setText("ENTER OU CLIQUE PARA CONTINUAR");
        btnAvancar.setText(indiceQuadro == quadros.size() - 1 ? "INICIAR CAPÍTULO  →" : "PRÓXIMA CENA  →");
        btnAvancar.setDisable(false);
        btnPular.setDisable(false);
        caixaOpcoes.getChildren().clear();
        caixaOpcoes.setManaged(false);
        caixaOpcoes.setVisible(false);
        mostrarDigitando(quadro.texto());
    }

    /** Usa as imagens enviadas para os seis quadros; 1, 4, 5 e 6 mudam conforme o personagem. */
    private void mostrarImagemPrologo(int indice) {
        boolean feminina = jogo.getPlayer().getSexo() == Sexo.FEMININO;
        String genero = feminina ? "feminino" : "masculino";
        String arquivo = switch (indice) {
            case 0 -> "01_anos_atras_" + genero + ".png";
            case 1 -> "02_sonho_do_pai.png";
            case 2 -> "03_acidente.png";
            case 3 -> "04_nova_casa_" + genero + ".png";
            case 4 -> "05_dezoito_anos_" + genero + ".png";
            case 5 -> "06_inicio_sonho_" + genero + ".png";
            default -> throw new IllegalArgumentException("Cena desconhecida: " + indice);
        };
        mostrarImagem(arquivo);
    }

    /** Exibe a arte do porão correspondente ao sexo escolhido na criação do personagem. */
    private void mostrarImagemPorao() {
        boolean feminina = jogo.getPlayer().getSexo() == Sexo.FEMININO;
        mostrarImagem("capitulo_01_porao_" + (feminina ? "feminino" : "masculino") + ".png");
    }

    /** Carrega imagens de cutscenes e também os cenários existentes de cada local. */
    private void mostrarImagem(String arquivo) {
        String caminho = arquivo.startsWith("/") ? arquivo : "/images/cutscenes/" + arquivo;
        if (caminho.equals(imagemAtual)) return;
        Image imagem = imagens.computeIfAbsent(caminho, p -> {
            URL recurso = getClass().getResource(p);
            if (recurso == null) throw new IllegalStateException("Imagem da cutscene não encontrada: " + p);
            return new Image(recurso.toExternalForm(), false);
        });
        imagemAtual = caminho;
        imgCenario.setImage(imagem);
        ajustarRecorte();
        FadeTransition transicao = new FadeTransition(Duration.millis(480), imgCenario);
        transicao.setFromValue(0.30);
        transicao.setToValue(1.0);
        transicao.play();
    }

    /** Recorta apenas as bordas para preencher toda a janela sem esticar a arte. */
    private void ajustarRecorte() {
        Image imagem = imgCenario.getImage();
        if (imagem == null || rootPane.getWidth() <= 0 || rootPane.getHeight() <= 0) return;
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

    private void iniciarPrimeiroCapitulo() {
        // Enquanto a exploração JavaFX não está pronta, a casa é acessada
        // automaticamente para disparar o capítulo 1 com o gatilho existente.
        Local localAtual = jogo.getLocalAtual();
        if ("cidade_natal".equals(localAtual.getId())) {
            localAtual.getSubLocais().stream()
                    .filter(local -> "casa".equals(local.getId()))
                    .findFirst().ifPresent(jogo::entrarLocal);
        }
        Optional<Capitulo> disponivel = jogo.getNarrativaController()
                .obterCapituloDisponivel(TipoGatilho.ENTRAR_LOCAL, "casa", jogo.getPlayer());
        if (disponivel.isEmpty()) {
            mostrarConclusao("O PRÓLOGO TERMINOU", "Não há capítulo disponível para esta partida.", false);
            return;
        }
        telaRetorno = Tela.MAPA;
        iniciarCapitulo(disponivel.get());
    }

    /** Inicia qualquer capítulo sem recriar seus diálogos, escolhas ou consequências. */
    private void iniciarCapitulo(Capitulo capitulo) {
        finalizado = false;
        capituloController = new CutsceneController(capitulo, jogo.getGameState());
        dialogos = new ArrayList<>(capituloController.getDialogos());
        indiceDialogo = 0;
        interlocutorAtual = null;
        imgInterlocutor.setImage(null);
        etapa = Etapa.CAPITULO;
        mostrarImagemDoCapitulo(capitulo.getId());
        mostrarDialogo();
    }

    /** Para capítulos sem ilustração exclusiva, usa o fundo existente do respectivo local. */
    private void mostrarImagemDoCapitulo(String id) {
        String imagem = switch (id) {
            case "CAPITULO_01" -> null; // usa a versão masculina/feminina do porão
            case "CAPITULO_02" -> "/images/locais/Academia_Cidade_Natal.png";
            case "CAPITULO_03", "CAPITULO_04", "CAPITULO_05", "CAPITULO_08" ->
                    "/images/locais/Academia_De_Boxe.png";
            case "CAPITULO_06" -> "/images/locais/Academia_Profissional.png";
            case "CAPITULO_07" -> "/images/locais/Quarto.png";
            case "CAPITULO_09" -> "/images/cidades/Cidade_B_Noite.png";
            case "CAPITULO_10" -> "/images/locais/Campeonato_Mundial.png";
            default -> "/images/locais/Academia_De_Boxe.png";
        };
        if (imagem == null) mostrarImagemPorao();
        else mostrarImagem(imagem);
    }

    private int numeroCapitulo() {
        String id = capituloController.getCapitulo().getId();
        try {
            return Integer.parseInt(id.substring(id.lastIndexOf('_') + 1));
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private void mostrarDialogo() {
        if (indiceDialogo >= dialogos.size()) {
            concluirCapitulo();
            return;
        }
        Dialogo dialogo = dialogos.get(indiceDialogo);
        String imagemEspecial = imagemEspecialDoDialogo(dialogo);
        if (imagemEspecial != null) {
            mostrarImagem(imagemEspecial);
            ocultarPersonagens();
        } else {
            String cena = switch (numeroCapitulo()) {
                case 3 -> "05";
                case 4 -> "01";
                case 5 -> "02";
                case 7 -> "03";
                case 9 -> "06";
                default -> null;
            };
            if (cena != null && indiceDialogo == 0) {
                String sexo = jogo.getPlayer().getSexo() == Sexo.FEMININO ? "feminino" : "masculino";
                mostrarImagem("cena_" + cena + "_" + sexo + ".png");
                ocultarPersonagens();
            } else {
                if (cena != null) mostrarImagemDoCapitulo(capituloController.getCapitulo().getId());
                atualizarPersonagens(dialogo);
            }
        }
        lblParte.setText(String.format("CAPÍTULO %02d  /  O LEGADO DO RINGUE", numeroCapitulo()));
        lblTitulo.setText(capituloController.getCapitulo().getTitulo().toUpperCase());
        lblProgresso.setText("DIÁLOGO " + (indiceDialogo + 1) + " DE " + dialogos.size());
        lblEmissor.setText(dialogo.getNomeEmissor().toUpperCase());
        lblAviso.setText(dialogo.isEscolha() ? "A SUA DECISÃO AFETA A HISTÓRIA" : "ENTER OU CLIQUE PARA CONTINUAR");
        caixaOpcoes.getChildren().clear();
        caixaOpcoes.setVisible(dialogo.isEscolha());
        caixaOpcoes.setManaged(dialogo.isEscolha());
        btnAvancar.setText("CONTINUAR  →");
        btnAvancar.setDisable(dialogo.isEscolha());
        btnPular.setDisable(dialogo.isEscolha());

        if (dialogo.isEscolha()) {
            pararDigitacao();
            lblTexto.setText(dialogo.getTexto());
            for (int i = 0; i < dialogo.getOpcoes().size(); i++) {
                final int indiceOpcao = i;
                Button botao = new Button((i + 1) + ".  " + dialogo.getOpcoes().get(i).getTexto());
                botao.getStyleClass().add("cutscene-opcao");
                botao.setMaxWidth(Double.MAX_VALUE);
                botao.setWrapText(true);
                botao.setOnAction(event -> processarEscolha(dialogo, indiceOpcao));
                caixaOpcoes.getChildren().add(botao);
            }
        } else {
            mostrarDigitando(dialogo.getTexto());
            if (dialogo.getTipoEmissor() == TipoEmissor.SISTEMA) AudioManager.tocar(Som.NOTIFICACAO);
        }
    }

    private String imagemEspecialDoDialogo(Dialogo dialogo) {
        if (etapa != Etapa.CAPITULO) return null;
        if (numeroCapitulo() != 9 || dialogo == null || dialogo.isEscolha()) return null;

        String sexo = jogo.getPlayer().getSexo() == Sexo.FEMININO ? "feminino" : "masculino";
        Player player = jogo.getPlayer();

        if (indiceDialogo == 0) {
            return "cena_06_" + sexo + ".png";
        }
        if (player.isCapituloConcluido("ROMANCE_OLIVIA")) {
            return "romance_olivia_" + sexo + ".png";
        }
        if (player.isCapituloConcluido("ROMANCE_JAMES")) {
            return "romance_james_" + sexo + ".png";
        }
        if (player.isCapituloConcluido("FOCO_NA_CARREIRA")) {
            return "protagonista_sozinho_" + sexo + ".png";
        }
        return null;
    }

    /**
     * Personagens da novel: protagonista a esquerda, interlocutor a direita.
     * No Capitulo 1, o protagonista nao aparece sobre a arte do porao, conforme pedido.
     * Os arquivos sao os PNGs originais enviados, com transparencia e 4 cores de olhos.
     * Nao altera o texto, a logica de escolhas ou os dados salvos do jogo.
     */
    private void atualizarPersonagens(Dialogo dialogo) {
        if (etapa != Etapa.CAPITULO) return;

        boolean mostrarProtagonista = numeroCapitulo() >= 2;
        if (mostrarProtagonista) {
            Player player = jogo.getPlayer();
            imgProtagonista.setImage(spritePersonagem(spriteProtagonista(player)));
            imgProtagonista.setVisible(true);
            imgProtagonista.setManaged(true);
        } else {
            imgProtagonista.setImage(null);
            imgProtagonista.setVisible(false);
            imgProtagonista.setManaged(false);
        }

        if (dialogo.getTipoEmissor() == TipoEmissor.PERSONAGEM
                && dialogo.getPersonagem() != null
                && !(dialogo.getPersonagem() instanceof Player)) {
            String nome = dialogo.getPersonagem().getNome();
            if (!nome.equalsIgnoreCase(interlocutorAtual == null ? "" : interlocutorAtual)) {
                interlocutorAtual = nome;
                String arquivo = spriteInterlocutor(nome);
                // Nao inventa sprites para personagens que ainda nao foram enviados.
                imgInterlocutor.setImage(arquivo == null ? null : spritePersonagem(arquivo));
            }
        }

        boolean haInterlocutor = imgInterlocutor.getImage() != null;
        imgInterlocutor.setVisible(haInterlocutor);
        imgInterlocutor.setManaged(haInterlocutor);

        boolean exibirPainel = mostrarProtagonista || haInterlocutor;
        painelPersonagens.setManaged(exibirPainel);
        painelPersonagens.setVisible(exibirPainel);

        boolean falaJogador = dialogo.getTipoEmissor() == TipoEmissor.PERSONAGEM
                && dialogo.getPersonagem() instanceof Player;
        boolean falaInterlocutor = dialogo.getTipoEmissor() == TipoEmissor.PERSONAGEM && !falaJogador;
        if (mostrarProtagonista) {
            imgProtagonista.setOpacity(falaJogador ? 1.0 : falaInterlocutor ? 0.62 : 0.83);
        }
        if (haInterlocutor) {
            imgInterlocutor.setOpacity(falaInterlocutor ? 1.0 : falaJogador ? 0.62 : 0.83);
        }
    }

    private String spriteProtagonista(Player player) {
        String genero = player.getSexo() == Sexo.FEMININO ? "Feminina" : "Masculino";
        Cores cor = player.getCorOlhos();
        String sufixo = switch (cor == null ? Cores.CASTANHO : cor) {
            case AZUL -> "_Olhos_Azuis";
            case PRETO -> "_Olhos_Pretos";
            case VERDE -> "_Olhos_Verdes";
            case CASTANHO -> "";
        };
        return "Protagonista_" + genero + sufixo + ".png";
    }

    private String spriteInterlocutor(String nome) {
        return switch (nome.toLowerCase(Locale.ROOT).trim()) {
            case "olivia" -> "Olivia.png";
            case "mestre smith" -> "Mestre_Smith.png";
            case "chris" -> "Chris.png";
            case "victor noor" -> "Victor_Noor.png";
            case "anthony" -> "Anthony.png";
            case "alexandra cruz" -> "Alexandra_Cruz.png";
            case "james" -> "James.png";
            default -> null;
        };
    }

    private Image spritePersonagem(String arquivo) {
        String caminho = "/images/visual_novel/" + arquivo;
        return spritesPersonagens.computeIfAbsent(caminho, key -> {
            URL recurso = getClass().getResource(key);
            if (recurso == null) {
                throw new IllegalStateException("Sprite da visual novel nao encontrado: " + key);
            }
            return new Image(recurso.toExternalForm(), false);
        });
    }

    private void ocultarPersonagens() {
        painelPersonagens.setVisible(false);
        painelPersonagens.setManaged(false);
    }

    private void processarEscolha(Dialogo dialogo, int indiceOpcao) {
        if (etapa != Etapa.CAPITULO || indiceDialogo >= dialogos.size()
                || dialogos.get(indiceDialogo) != dialogo) return;
        Dialogo.Opcao opcao = dialogo.getOpcoes().get(indiceOpcao);
        List<Dialogo> avisos = capituloController.processarEscolha(dialogo, indiceOpcao);
        // As falas de consequência aparecem antes da próxima fala normal.
        dialogos.addAll(indiceDialogo + 1, opcao.getDesfecho());
        dialogos.addAll(indiceDialogo + 1, avisos);
        AudioManager.tocar(Som.CONFIRMAR);
        indiceDialogo++;
        mostrarDialogo();
    }

    private void concluirCapitulo() {
        if (finalizado) return;
        finalizado = true;
        capituloController.finalizarCapitulo();
        String aviso = "A sua escolha foi registrada no jogo.";
        SaveGameController saves = new SaveGameController();
        try {
            if (saves.autoSaveHabilitado()) {
                saves.autoSave(jogo.getGameState());
                aviso = "Capítulo concluído e progresso salvo automaticamente.";
            }
        } catch (FalhaSalvamentoException ex) {
            aviso = "Capítulo concluído, mas não foi possível salvar. Use o menu de saves.";
        }
        mostrarConclusao("CAPÍTULO " + String.format("%02d", numeroCapitulo()) + " CONCLUÍDO",
                aviso, true);
    }

    private void mostrarConclusao(String titulo, String aviso, boolean capituloConcluido) {
        ocultarPersonagens();
        etapa = Etapa.CONCLUIDA;
        pararDigitacao();
        lblParte.setText("O LEGADO DO RINGUE");
        lblTitulo.setText(titulo);
        lblProgresso.setText(capituloConcluido ? "PROGRESSO REGISTRADO" : "FIM DO PRÓLOGO");
        lblEmissor.setText("SISTEMA");
        lblTexto.setText(aviso + "\n\nContinue explorando os locais do mapa para encontrar novos capítulos."
                + (capituloConcluido && numeroCapitulo() == 10
                ? "\nNa Cidade B, selecione o Campeonato Mundial para disputar o cinturão."
                : ""));
        lblAviso.setText("ENTER OU CLIQUE PARA CONTINUAR");
        caixaOpcoes.getChildren().clear();
        caixaOpcoes.setVisible(false);
        caixaOpcoes.setManaged(false);
        btnPular.setDisable(true);
        btnAvancar.setDisable(false);
        btnAvancar.setText(telaRetorno == Tela.MAPA ? "IR PARA O MAPA  →" : "CONTINUAR EXPLORANDO  →");
    }

    @FXML
    public void avancar() {
        if (digitacao != null && digitacao.getStatus() == Animation.Status.RUNNING) {
            concluirDigitacao();
            return;
        }
        switch (etapa) {
            case PROLOGO -> { indiceQuadro++; mostrarQuadro(); }
            case CAPITULO -> {
                if (indiceDialogo < dialogos.size() && dialogos.get(indiceDialogo).isEscolha()) return;
                indiceDialogo++;
                mostrarDialogo();
            }
            case CONCLUIDA -> {
                // Se o próximo capítulo acontece no mesmo local e já está liberado, inicia em sequência.
                if (!SceneManager.iniciarCapituloSeDisponivel(jogo.getLocalAtual(), telaRetorno)) {
                    SceneManager.irPara(telaRetorno);
                }
            }
        }
    }

    @FXML
    public void pular() {
        if (etapa == Etapa.PROLOGO) {
            pararDigitacao();
            indiceQuadro = quadros.size();
            iniciarPrimeiroCapitulo();
        } else if (etapa == Etapa.CAPITULO) {
            pararDigitacao();
            while (indiceDialogo < dialogos.size() && !dialogos.get(indiceDialogo).isEscolha()) {
                indiceDialogo++;
            }
            mostrarDialogo(); // Nunca pula decisões: ações e consequências são preservadas.
        }
    }

    /** Congela o efeito de digitação enquanto o modal de pausa estiver aberto. */
    @FXML
    public void abrirMenuPausa() {
        if (SceneManager.pausaAtiva()) return;
        digitacaoPausadaPeloMenu = digitacao != null
                && digitacao.getStatus() == Animation.Status.RUNNING;
        if (digitacaoPausadaPeloMenu) digitacao.pause();
        boolean abriu = SceneManager.abrirMenuPausa(() -> {
            if (digitacaoPausadaPeloMenu && digitacao != null
                    && digitacao.getStatus() == Animation.Status.PAUSED) {
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

    @FXML
    public void voltarAoMenu() {
        if (etapa == Etapa.CONCLUIDA) {
            SceneManager.irPara(Tela.MENU_INICIAL);
            return;
        }
        pararDigitacao();
        SceneManager.abrirConfirmacao(
                "SAIR DA HISTÓRIA",
                "VOLTAR AO MENU SEM TERMINAR A HISTÓRIA?",
                "O PROGRESSO NÃO SALVO PODE SER PERDIDO.",
                "SAIR",
                () -> SceneManager.irPara(Tela.MENU_INICIAL)
        );
    }

    private void mostrarDigitando(String texto) {
        pararDigitacao();
        textoCompleto = texto;
        caracteresExibidos = 0;
        lblTexto.setText("");
        digitacao = new Timeline(new KeyFrame(Duration.millis(24), event -> {
            caracteresExibidos = Math.min(caracteresExibidos + 1, textoCompleto.length());
            lblTexto.setText(textoCompleto.substring(0, caracteresExibidos));
            if (caracteresExibidos >= textoCompleto.length()) pararDigitacao();
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
