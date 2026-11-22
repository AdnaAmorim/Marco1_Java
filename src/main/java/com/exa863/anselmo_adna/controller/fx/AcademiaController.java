package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.model.world.DataDia;
import com.exa863.anselmo_adna.model.world.Local;
import com.exa863.anselmo_adna.view.fx.Icone;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Cena em estilo visual novel da academia de musculação (Cidade Natal e Profissional).
 * Cobra a diária de entrada e permite escolher qual atributo treinar.
 *
 * @author Anselmo e Adna
 */
public class AcademiaController {

    private static final double LARGURA_CENARIO = 960.0;
    private static final double ALTURA_CENARIO = 540.0;
    private static final int CUSTO_ENTRADA = 50;
    private static final int CUSTO_ENERGIA = 25;

    private static final String IMG_ACADEMIA_NATAL = "/images/locais/Academia_Cidade_Natal.png";
    private static final String IMG_ACADEMIA_PROFISSIONAL = "/images/locais/Academia_Profissional.png";

    @FXML private StackPane rootPane;
    @FXML private StackPane areaCenario;
    @FXML private Pane cenarioCanvas;
    @FXML private ImageView imgFundo;

    @FXML private Label lblAtleta;
    @FXML private Label lblSaude;
    @FXML private Label lblEnergia;
    @FXML private Label lblDinheiro;
    @FXML private Label lblDataHora;
    @FXML private Button btnPausa;

    @FXML private Label lblFalante;
    @FXML private Label lblTreinos;
    @FXML private Label lblTexto;
    @FXML private VBox boxDetalhes;
    @FXML private VBox boxOpcoes;

    private final GameController jogo = SceneManager.getGameController();
    private final Random random = new Random();
    private String tituloAcademia;

    @FXML
    public void initialize() {
        configurarEscalaCenario();
        configurarIconesHud();
        configurarTeclado();
        carregarFundoPorLocal();
        atualizarHud();
        verificarEntrada();
    }

    private void carregarFundoPorLocal() {
        Local atual = jogo.getLocalAtual();
        boolean profissional = atual != null && "academia_profissional".equalsIgnoreCase(atual.getId());
        tituloAcademia = profissional ? "ACADEMIA PROFISSIONAL" : "ACADEMIA";

        String caminho = profissional ? IMG_ACADEMIA_PROFISSIONAL : IMG_ACADEMIA_NATAL;
        URL recurso = getClass().getResource(caminho);
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

    private void verificarEntrada() {
        Player player = jogo.getPlayer();
        if (player == null) {
            sairDaAcademia();
            return;
        }

        Atributo attr = player.getAtributos();
        int diaAtual = jogo.getDataDia().getDias();

        if (!player.podeTreinarHoje(diaAtual)) {
            lblFalante.setText(tituloAcademia + " • LIMITE DIÁRIO");
            lblTexto.setText("O TREINADOR BARROU VOCÊ: VOCÊ JÁ COMPLETOU OS "
                    + Player.MAX_TREINOS_POR_DIA + " TREINOS PERMITIDOS HOJE!");
            mostrarDetalhes(List.of(
                    criarLinhaDetalhe("VÁ PARA SUA CASA NO MAPA DESCANSAR E DORMIR PARA AVANÇAR O DIA.", "vn-linha-alerta")
            ));
            definirBotaoSaida("VOLTAR AO MAPA");
            return;
        }

        if (attr.getEnergia() < CUSTO_ENERGIA) {
            lblFalante.setText(tituloAcademia + " • SEM ENERGIA");
            lblTexto.setText("O TREINADOR BARROU VOCÊ NA ENTRADA! É PRECISO TER PELO MENOS "
                    + CUSTO_ENERGIA + " DE ENERGIA PARA TREINAR.");
            mostrarDetalhes(List.of(
                    criarLinhaDetalhe("ENERGIA ATUAL: " + attr.getEnergia() + "/100. DESCANSE EM CASA PRIMEIRO.", "vn-linha-alerta")
            ));
            definirBotaoSaida("VOLTAR AO MAPA");
            return;
        }

        exibirRecepcao(player);
    }

    private void exibirRecepcao(Player player) {
        lblFalante.setText("RECEPÇÃO • " + tituloAcademia);
        lblTexto.setText("A CATRACA DE ACESSO ESTÁ BLOQUEADA. PAGUE A DIÁRIA PARA LIBERAR OS APARELHOS.");
        mostrarDetalhes(List.of(
                criarLinhaDetalhe("TAXA DE ENTRADA: R$ " + CUSTO_ENTRADA
                        + "  •  SEU DINHEIRO: R$ " + player.getDinheiro(), "vn-linha-info")
        ));

        boxOpcoes.getChildren().setAll(
                criarBotaoOpcao("PAGAR R$ " + CUSTO_ENTRADA + " E ENTRAR", Icone.CART, false, this::pagarEntrada),
                criarBotaoOpcao("VOLTAR AO MAPA", Icone.LEFT_ARROW, true, this::sairDaAcademia)
        );
        AudioManager.aplicarHoverNosBotoes(boxOpcoes);
    }

    private void pagarEntrada() {
        Player player = jogo.getPlayer();
        if (player == null) {
            return;
        }

        if (player.getDinheiro() < CUSTO_ENTRADA) {
            AudioManager.tocar(Som.ERRO);
            lblFalante.setText("RECEPÇÃO • SEM DINHEIRO");
            lblTexto.setText("DINHEIRO INSUFICIENTE PARA PAGAR A DIÁRIA DA ACADEMIA (R$ " + CUSTO_ENTRADA + ").");
            mostrarDetalhes(List.of(
                    criarLinhaDetalhe("SEU DINHEIRO ATUAL: R$ " + player.getDinheiro(), "vn-linha-alerta")
            ));
            definirBotaoSaida("VOLTAR AO MAPA");
            return;
        }

        AudioManager.tocar(Som.CONFIRMAR);
        player.setDinheiro(player.getDinheiro() - CUSTO_ENTRADA);
        atualizarHud();
        exibirAparelhos(player);
    }

    private void exibirAparelhos(Player player) {
        Atributo attr = player.getAtributos();
        lblFalante.setText(tituloAcademia + " • TREINAMENTO");
        lblTexto.setText("CATRACA LIBERADA! ESCOLHA SEU FOCO DE TREINO (-25 EN • +2H • 50% CHANCE DE +1):");
        mostrarDetalhes(List.of(
                criarLinhaDetalhe("FORÇA " + attr.getForca() + "/10  •  AGILIDADE " + attr.getAgilidade()
                        + "/10  •  RESISTÊNCIA " + attr.getResistencia() + "/10  •  INTELIGÊNCIA "
                        + attr.getInteligencia() + "/10", "vn-linha-info")
        ));

        boxOpcoes.getChildren().setAll(
                criarBotaoOpcao("FORÇA • SACO E HALTERES", Icone.STAR, false,
                        () -> treinarAtributo("FORÇA", () -> attr.setForca(attr.getForca() + 1), attr.getForca())),
                criarBotaoOpcao("AGILIDADE • CORDA E ESQUIVA", Icone.STAR, false,
                        () -> treinarAtributo("AGILIDADE", () -> attr.setAgilidade(attr.getAgilidade() + 1), attr.getAgilidade())),
                criarBotaoOpcao("RESISTÊNCIA • CIRCUITO CARDIO", Icone.STAR, false,
                        () -> treinarAtributo("RESISTÊNCIA", () -> attr.setResistencia(attr.getResistencia() + 1), attr.getResistencia())),
                criarBotaoOpcao("INTELIGÊNCIA • TREINO TÁTICO", Icone.STAR, false,
                        () -> treinarAtributo("INTELIGÊNCIA", () -> attr.setInteligencia(attr.getInteligencia() + 1), attr.getInteligencia()))
        );
        AudioManager.aplicarHoverNosBotoes(boxOpcoes);
    }

    private void treinarAtributo(String nome, Runnable incremento, int valorAtual) {
        Player player = jogo.getPlayer();
        if (player == null) {
            return;
        }
        Atributo attr = player.getAtributos();
        int diaAtual = jogo.getDataDia().getDias();

        if (valorAtual >= Atributo.MAX_HABILIDADE) {
            AudioManager.tocar(Som.ERRO);
            player.registrarTreino(diaAtual);
            atualizarHud();
            lblFalante.setText("NÍVEL MÁXIMO ATINGIDO");
            lblTexto.setText("SUA " + nome + " JÁ ATINGIU O ÁPICE (" + Atributo.MAX_HABILIDADE + "/10)!");
            mostrarDetalhes(List.of(
                    criarLinhaDetalhe("NÃO É POSSÍVEL EVOLUIR MAIS ESTE ATRIBUTO. SUA ENERGIA FOI PRESERVADA.", "vn-linha-info")
            ));
            definirBotaoSaida("SAIR DA ACADEMIA");
            return;
        }

        AudioManager.tocar(Som.CONFIRMAR);
        attr.setEnergia(attr.getEnergia() - CUSTO_ENERGIA);
        jogo.getDataDia().avancarMinutos(120);
        player.registrarTreino(diaAtual);

        boolean evoluiu = random.nextInt(100) < 50;
        List<Label> detalhes = new ArrayList<>();

        lblFalante.setText("SESSÃO DE TREINO CONCLUÍDA");
        if (evoluiu) {
            incremento.run();
            lblTexto.setText("EXCELENTE DESEMPENHO! SEU TREINO DE " + nome + " RENDEU RESULTADO.");
            detalhes.add(criarLinhaDetalhe("+1 " + nome + " (NOVO NÍVEL: "
                    + (valorAtual + 1) + " / " + Atributo.MAX_HABILIDADE + ")", "vn-linha-positiva"));
        } else {
            lblTexto.setText("O TREINO FOI ÁRDUO, MAS SEU CORPO NÃO ABSORVEU O GANHO DESTA VEZ.");
            detalhes.add(criarLinhaDetalhe(nome + " PERMANECE EM "
                    + valorAtual + " / " + Atributo.MAX_HABILIDADE + ".", "vn-linha-alerta"));
        }

        atualizarHud();
        detalhes.add(criarLinhaDetalhe("-" + CUSTO_ENERGIA + " ENERGIA (RESTANTE: " + attr.getEnergia()
                + "/100)  •  +2 HORAS (" + jogo.getDataDia().getHoraFormatada() + ")", "vn-linha-info"));
        mostrarDetalhes(detalhes);
        definirBotaoSaida("SAIR DA ACADEMIA");
    }

    private void sairDaAcademia() {
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

    private void definirBotaoSaida(String texto) {
        boxOpcoes.getChildren().setAll(criarBotaoOpcao(texto, Icone.LEFT_ARROW, true, this::sairDaAcademia));
        AudioManager.aplicarHoverNosBotoes(boxOpcoes);
    }

    private Label criarLinhaDetalhe(String texto, String classeCss) {
        Label label = new Label(texto);
        label.setWrapText(true);
        label.getStyleClass().add(classeCss);
        return label;
    }

    private void mostrarDetalhes(List<Label> linhas) {
        boxDetalhes.getChildren().setAll(linhas);
        boxDetalhes.setVisible(true);
        boxDetalhes.setManaged(true);
    }
}
