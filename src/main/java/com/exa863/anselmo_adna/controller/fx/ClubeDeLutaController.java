package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.MatchController;
import com.exa863.anselmo_adna.model.character.Cores;
import com.exa863.anselmo_adna.model.character.Personagem;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.character.Sexo;
import com.exa863.anselmo_adna.model.combat.EstiloLuta;
import com.exa863.anselmo_adna.model.combat.Lutador;
import com.exa863.anselmo_adna.model.combat.Match;
import com.exa863.anselmo_adna.model.combat.estilos.Contragolpeador;
import com.exa863.anselmo_adna.model.combat.estilos.Nocauteador;
import com.exa863.anselmo_adna.model.combat.estilos.Velocista;
import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.model.world.DataDia;
import com.exa863.anselmo_adna.view.fx.Icone;
import com.exa863.anselmo_adna.view.fx.LutaContexto;
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
import java.util.Random;

/**
 * Cena em estilo visual novel do Clube de Luta clandestino.
 * Apresenta o adversário sorteado, a aposta e a seleção de postura antes do combate.
 *
 * @author Anselmo e Adna
 */
public class ClubeDeLutaController {

    /** Largura cenário. */
    private static final double LARGURA_CENARIO = 960.0;
    /** Altura cenário. */
    private static final double ALTURA_CENARIO = 540.0;
    /** Imagem clube. */
    private static final String IMG_CLUBE = "/images/locais/Clube_De_Luta.png";
    /** Imagem adversário. */
    private static final String IMG_ADVERSARIO = "/images/personagens/lutador_desconhecido.png";

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
    /** Imagem — adversario. */
    @FXML private ImageView imgAdversario;
    /** Texto — nome adversario. */
    @FXML private Label lblNomeAdversario;
    /** Texto — estilo adversario. */
    @FXML private Label lblEstiloAdversario;
    /** Texto — força do adversário. */
    @FXML private Label lblForcaAdv;
    /** Texto — agilidade do adversário. */
    @FXML private Label lblAgilidadeAdv;
    /** Texto — resistência do adversário. */
    @FXML private Label lblResistenciaAdv;
    /** Texto — inteligência do adversário. */
    @FXML private Label lblInteligenciaAdv;
    /** Texto exibido — prêmio adversário. */
    @FXML private Label lblPremioAdversario;

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
    /** Texto — aposta. */
    @FXML private Label lblAposta;
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

    /** Força oponente. */
    private int forcaOponente;
    /** Agilidade oponente. */
    private int agilidadeOponente;
    /** Resistência oponente. */
    private int resistenciaOponente;
    /** Inteligência oponente. */
    private int inteligenciaOponente;
    /** Dinheiro aposta. */
    private int dinheiroAposta;
    /** Estilo oponente. */
    private EstiloLuta estiloOponente;

    /** Prepara os componentes da tela após o carregamento do FXML. */
    @FXML
    public void initialize() {
        configurarEscalaCenario();
        configurarIconesHud();
        configurarTeclado();
        carregarFundo();
        atualizarHud();
        verificarCondicoesEntrada();
    }

    /** Carrega imagem de fundo. */
    private void carregarFundo() {
        URL recurso = getClass().getResource(IMG_CLUBE);
        if (recurso != null) {
            Image imagem = new Image(recurso.toExternalForm());
            imgFundo.setImage(imagem);
            imgFundoBlur.setImage(imagem);
        }

        URL recursoAdv = getClass().getResource(IMG_ADVERSARIO);
        if (recursoAdv != null) {
            Image imagemAdv = new Image(recursoAdv.toExternalForm());
            imgAdversario.setImage(imagemAdv);
            double w = imagemAdv.getWidth();
            double h = imagemAdv.getHeight();
            if (w > 0 && h > 0) {
                imgAdversario.setViewport(new Rectangle2D(w * 0.14, h * 0.10, w * 0.72, h * 0.44));
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

    /** Verifica condições de entrada do jogador. */
    private void verificarCondicoesEntrada() {
        Player player = jogo.getPlayer();
        if (player == null) {
            sairDoClube();
            return;
        }

        Atributo attr = player.getAtributos();
        if (attr.getSaude() <= 25 || attr.getEnergia() < 20) {
            cardAdversario.setVisible(false);
            cardAdversario.setManaged(false);
            lblAposta.setText("ACESSO NEGADO");
            lblFalante.setText("APOSTADOR DO CLUBE");
            lblTexto.setText("\"EI, PARCEIRO, VOCÊ MAL CONSEGUE PARAR EM PÉ! NÃO DURA 10 SEGUNDOS AQUI.\"");
            mostrarDetalhes(List.of(
                    criarLinhaDetalhe("SAÚDE (> 25) OU ENERGIA (>= 20) MUITO BAIXAS PARA A BRIGA DE RUA.", "vn-linha-alerta"),
                    criarLinhaDetalhe("VÁ ATÉ SUA CASA NO MAPA DESCANSAR E DORMIR PARA SE RECUPERAR.", "vn-linha-info")
            ));
            definirBotaoSaida("VOLTAR AO MAPA");
            return;
        }

        sortearOponente();
        atualizarCardAdversario();
        exibirMenuClube();
    }

    /** Atualiza ficha do adversário. */
    private void atualizarCardAdversario() {
        lblNomeAdversario.setText("LUTADOR DESCONHECIDO");
        lblEstiloAdversario.setText("ESTILO: " + estiloOponente.getNome().toUpperCase());
        lblForcaAdv.setText("FOR " + forcaOponente + "/10");
        lblAgilidadeAdv.setText("AGI " + agilidadeOponente + "/10");
        lblResistenciaAdv.setText("RES " + resistenciaOponente + "/10");
        lblInteligenciaAdv.setText("INT " + inteligenciaOponente + "/10");
        lblPremioAdversario.setText("PRÊMIO DA APOSTA: R$ " + dinheiroAposta);
        cardAdversario.setVisible(true);
        cardAdversario.setManaged(true);
    }

    /** Sorteia oponente. */
    private void sortearOponente() {
        forcaOponente = 1 + random.nextInt(2);
        agilidadeOponente = 1 + random.nextInt(2);
        resistenciaOponente = 1 + random.nextInt(2);
        inteligenciaOponente = 1;
        dinheiroAposta = 50 + random.nextInt(51);
        estiloOponente = switch (random.nextInt(3)) {
            case 0 -> new Velocista();
            case 1 -> new Nocauteador();
            default -> new Contragolpeador();
        };
    }

    /** Exibe opções do clube de luta. */
    private void exibirMenuClube() {
        lblAposta.setText("APOSTA: R$ " + dinheiroAposta);
        lblFalante.setText("CLUBE DE LUTA • BRIGAS CLANDESTINAS");
        lblTexto.setText("O AMBIENTE É SOMBRIO, COM CHEIRO DE FUMAÇA E APOSTAS. AQUI NÃO EXISTEM JUÍZES NEM REGRAS: APENAS A LEI DA RUA.");
        mostrarDetalhes(List.of(
                criarLinhaDetalhe("CUIDADO: BRIGAS CLANDESTINAS DE RUA CAUSAM FERIMENTOS PESADOS!", "vn-linha-alerta")
        ));

        boxOpcoes.getChildren().setAll(
                criarBotaoOpcao("ENCARAR A BRIGA DE RUA", Icone.EXCLAMATION_MARK, false, this::exibirEscolhaPostura),
                criarBotaoOpcao("VOLTAR AO MAPA", Icone.LEFT_ARROW, true, this::sairDoClube)
        );
        AudioManager.aplicarHoverNosBotoes(boxOpcoes);
    }

    /** Exibe estilos de luta disponíveis. */
    private void exibirEscolhaPostura() {
        AudioManager.tocar(Som.CLIQUE);
        lblFalante.setText("POSTURA NA BRIGA DE RUA");
        lblTexto.setText("ESCOLHA SUA POSTURA DE COMBATE:");

        HBox linhaVelocista = criarLinhaPostura("VELOCISTA",
                List.of("+AGI (15-75%)", "+20% EN"), List.of("-20% FOR"));
        HBox linhaNocauteador = criarLinhaPostura("NOCAUTEADOR",
                List.of("+FOR (15-75%)"), List.of("-20% AGI", "-20% EN"));
        HBox linhaContragolpeador = criarLinhaPostura("CONTRAGOLPEADOR",
                List.of("+RES (15-75%)", "+INT (15-75%)"), List.of("-15% AGI"));

        boxDetalhes.getChildren().setAll(linhaVelocista, linhaNocauteador, linhaContragolpeador);
        boxDetalhes.setVisible(true);
        boxDetalhes.setManaged(true);

        Button btnVel = criarBotaoOpcao("POSTURA VELOCISTA", Icone.STAR, false, () -> selecionarPostura(new Velocista()));
        Button btnNoc = criarBotaoOpcao("POSTURA NOCAUTEADOR", Icone.STAR, false, () -> selecionarPostura(new Nocauteador()));
        Button btnCon = criarBotaoOpcao("POSTURA CONTRAGOLPEADOR", Icone.STAR, false, () -> selecionarPostura(new Contragolpeador()));
        Button btnVoltar = criarBotaoOpcao("VOLTAR", Icone.LEFT_ARROW, true, this::exibirMenuClube);

        vincularDestaquePostura(btnVel, linhaVelocista);
        vincularDestaquePostura(btnNoc, linhaNocauteador);
        vincularDestaquePostura(btnCon, linhaContragolpeador);

        boxOpcoes.getChildren().setAll(btnVel, btnNoc, btnCon, btnVoltar);
        AudioManager.aplicarHoverNosBotoes(boxOpcoes);
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

    /** Seleciona postura. */
    private void selecionarPostura(EstiloLuta postura) {
        Player player = jogo.getPlayer();
        if (player == null) {
            return;
        }
        AudioManager.tocar(Som.CONFIRMAR);

        Lutador lutadorPlayer = new Lutador(player, player.getAtributos(), postura);
        Personagem pOponente = new Personagem(999, "Lutador Desconhecido", "Desconhecido", Cores.CASTANHO, Sexo.MASCULINO);
        Atributo attrOponente = new Atributo(100, forcaOponente, agilidadeOponente,
                resistenciaOponente, inteligenciaOponente, 100);
        Lutador lutadorOponente = new Lutador(pOponente, attrOponente, estiloOponente);

        Match match = new MatchController().executarPartida(lutadorPlayer, lutadorOponente);
        // Guarda a aposta desta luta para calcular o pagamento quando o resultado for exibido.
        int aposta = dinheiroAposta;

        LutaContexto contexto = new LutaContexto(
                "CLUBE DE LUTA • BRIGA DE RUA",
                IMG_CLUBE,
                IMG_ADVERSARIO,
                lutadorPlayer,
                lutadorOponente,
                match,
                Tela.CLUBE_DE_LUTA,
                resultado -> {
                    jogo.getDataDia().avancarMinutos(60);
                    player.getAtributos().setEnergia(Math.max(0, player.getAtributos().getEnergia() - 25));

                    if (resultado.isVencedor(lutadorPlayer)) {
                        player.setDinheiro(player.getDinheiro() + aposta);
                        int novaSaude = Math.max(15, player.getAtributos().getSaude() - 50);
                        player.getAtributos().setSaude(novaSaude);
                        return List.of(
                                "+VITÓRIA! PRÊMIO DA APOSTA: +R$ " + aposta + " (TOTAL: R$ " + player.getDinheiro() + ")",
                                "!FERIMENTOS PESADOS NA BRIGA DE RUA: SAÚDE RESTANTE " + novaSaude + "/100",
                                "TEMPO DECORRIDO: +1 HORA • ENERGIA RESTANTE: " + player.getAtributos().getEnergia() + "/100"
                        );
                    }
                    if (resultado.isEmpate()) {
                        int meio = aposta / 2;
                        player.setDinheiro(player.getDinheiro() + meio);
                        int novaSaude = Math.max(15, player.getAtributos().getSaude() - 40);
                        player.getAtributos().setSaude(novaSaude);
                        return List.of(
                                "+A BRIGA FOI INTERROMPIDA! DINHEIRO RECOLHIDO: +R$ " + meio,
                                "!SAÚDE RESTANTE: " + novaSaude + "/100 • ENERGIA: " + player.getAtributos().getEnergia() + "/100"
                        );
                    }
                    int novaSaude = Math.max(15, player.getAtributos().getSaude() - 60);
                    player.getAtributos().setSaude(novaSaude);
                    return List.of(
                            "!DERROTA NA BRIGA DE RUA! VOCÊ SAIU SEM NADA.",
                            "!ESTADO CRÍTICO: SAÚDE RESTANTE " + novaSaude + "/100 — VÁ PARA SUA CASA DESCANSAR!"
                    );
                }
        );

        SceneManager.iniciarLuta(contexto);
    }

    /** Retorna do clube ao mapa. */
    private void sairDoClube() {
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

    /** Define ação do botão de saída. */
    private void definirBotaoSaida(String texto) {
        boxOpcoes.getChildren().setAll(criarBotaoOpcao(texto, Icone.LEFT_ARROW, true, this::sairDoClube));
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
