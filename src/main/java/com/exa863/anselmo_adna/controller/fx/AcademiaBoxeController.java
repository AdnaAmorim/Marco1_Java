package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.controller.ConquistaController;
import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.MatchController;
import com.exa863.anselmo_adna.exception.FalhaCarregamentoException;
import com.exa863.anselmo_adna.exception.FalhaSalvamentoException;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.combat.EstiloLuta;
import com.exa863.anselmo_adna.model.combat.Lutador;
import com.exa863.anselmo_adna.model.combat.Match;
import com.exa863.anselmo_adna.model.combat.OponentesBoxe;
import com.exa863.anselmo_adna.model.combat.OponentesBoxe.InfoOponente;
import com.exa863.anselmo_adna.model.combat.estilos.Contragolpeador;
import com.exa863.anselmo_adna.model.combat.estilos.Nocauteador;
import com.exa863.anselmo_adna.model.combat.estilos.Velocista;
import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.model.world.DataDia;
import com.exa863.anselmo_adna.persistencia.Conquistas;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Cena em estilo visual novel da Academia de Boxe.
 * Gerencia inscrições nos circuitos Local, Estadual e Nacional e inicia o combate oficial.
 *
 * @author Anselmo e Adna
 */
public class AcademiaBoxeController {

    /** Largura cenário. */
    private static final double LARGURA_CENARIO = 960.0;
    /** Altura cenário. */
    private static final double ALTURA_CENARIO = 540.0;
    /** Limite lutas estaduais. */
    private static final int LIMITE_LUTAS_ESTADUAIS = 2;
    /** Limite lutas nacionais. */
    private static final int LIMITE_LUTAS_NACIONAIS = 2;

    /** Imagem academia boxe. */
    private static final String IMG_ACADEMIA_BOXE = "/images/locais/Academia_De_Boxe.png";
    /** Imagem oponente. */
    private static final String IMG_OPONENTE = "/images/personagens/lutador_desconhecido.png";

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
    /** Texto — categoria do campeonato. */
    @FXML private Label lblTagCampeonato;
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
    /** Texto — progresso lutas. */
    @FXML private Label lblProgressoLutas;
    /** Texto — texto. */
    @FXML private Label lblTexto;
    /** Painel — detalhes. */
    @FXML private VBox boxDetalhes;
    /** Box opções. */
    @FXML private VBox boxOpcoes;

    /** Controlador da partida compartilhado pela interface. */
    private final GameController jogo = SceneManager.getGameController();

    /** Oponente selecionado. */
    private InfoOponente oponenteSelecionado;
    /** Taxa selecionada. */
    private int taxaSelecionada;
    /** Tipo campeonato selecionado. */
    private int tipoCampeonatoSelecionado;

    /** Prepara os componentes da tela após o carregamento do FXML. */
    @FXML
    public void initialize() {
        configurarEscalaCenario();
        configurarIconesHud();
        configurarTeclado();
        carregarImagens();
        atualizarHud();
        verificarDisponibilidadeLuta();
    }

    /** Carrega imagens. */
    private void carregarImagens() {
        URL recFundo = getClass().getResource(IMG_ACADEMIA_BOXE);
        if (recFundo != null) {
            Image imagem = new Image(recFundo.toExternalForm());
            imgFundo.setImage(imagem);
            imgFundoBlur.setImage(imagem);
        }

        URL recAdv = getClass().getResource(IMG_OPONENTE);
        if (recAdv != null) {
            Image imgAdv = new Image(recAdv.toExternalForm());
            imgAdversario.setImage(imgAdv);
            double w = imgAdv.getWidth();
            double h = imgAdv.getHeight();
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
            lblProgressoLutas.setText("ESTADUAIS: 0/2 • NACIONAIS: 0/2");
            return;
        }

        Atributo attr = player.getAtributos();
        lblAtleta.setText(player.getNome().toUpperCase());
        lblSaude.setText(attr.getSaude() + "/100");
        lblEnergia.setText(attr.getEnergia() + "/100");
        lblDinheiro.setText("R$ " + player.getDinheiro());
        lblProgressoLutas.setText("ESTADUAIS: " + player.getLutasEstaduais() + "/" + LIMITE_LUTAS_ESTADUAIS
                + " • NACIONAIS: " + player.getLutasNacionais() + "/" + LIMITE_LUTAS_NACIONAIS);
    }

    /** Consulta o dia da próxima luta antes de oferecer os circuitos ao jogador. */
    private void verificarDisponibilidadeLuta() {
        Player player = jogo.getPlayer();
        if (player == null) {
            sairDaAcademia();
            return;
        }

        int diaAtual = jogo.getDataDia().getDias();
        if (!player.podeLutarBoxe(diaAtual)) {
            ocultarCardAdversario();
            lblFalante.setText("ÁRBITRO • DIA DE DESCANSO");
            lblTexto.setText("\"HOJE NÃO HÁ LUTAS NO CARD OFICIAL, CAMPEÃO! VOCÊ JÁ LUTOU HOJE.\"");
            mostrarDetalhes(List.of(
                    criarLinhaDetalhe("DIA ATUAL: DIA " + diaAtual + "  •  PRÓXIMA LUTA: DIA "
                            + player.getProximoDiaLutaBoxe(), "vn-linha-info"),
                    criarLinhaDetalhe("APROVEITE PARA TREINAR NA ACADEMIA OU DESCANSAR EM CASA.", "vn-linha-positiva")
            ));
            definirBotaoSaida("VOLTAR AO MAPA");
            return;
        }

        exibirMenuCircuitos();
    }

    /** Exibe circuitos de luta disponíveis. */
    private void exibirMenuCircuitos() {
        Player player = jogo.getPlayer();
        if (player == null) {
            return;
        }
        ocultarCardAdversario();
        atualizarHud();

        boolean estadualEsgotado = player.getLutasEstaduais() >= LIMITE_LUTAS_ESTADUAIS;
        boolean nacionalLiberado = player.getLutasEstaduais() >= LIMITE_LUTAS_ESTADUAIS;
        boolean nacionalEsgotado = player.getLutasNacionais() >= LIMITE_LUTAS_NACIONAIS;

        lblFalante.setText("ACADEMIA DE BOXE • MENU DE LUTAS");
        lblTexto.setText("O ÁRBITRO AGUARDA A SUA INSCRIÇÃO. ESCOLHA O CIRCUITO EM QUE DESEJA LUTAR HOJE:");
        mostrarDetalhes(List.of(
                criarLinhaDetalhe("LOCAL: SEM TAXA (DIÁRIO)  •  ESTADUAL: R$ 500 -> PRÊMIO R$ 2.000", "vn-linha-info"),
                criarLinhaDetalhe("NACIONAL: R$ 1.500 -> PRÊMIO R$ 5.000 (LIBERA APÓS 2 LUTAS ESTADUAIS)", "vn-linha-info")
        ));

        String textoEstadual = estadualEsgotado
                ? "CINTURÃO ESTADUAL (ENCERRADO)"
                : "CINTURÃO ESTADUAL (R$ 500)";
        String textoNacional = !nacionalLiberado
                ? "CINTURÃO NACIONAL (BLOQUEADO)"
                : (nacionalEsgotado ? "CINTURÃO NACIONAL (ENCERRADO)" : "CINTURÃO NACIONAL (R$ 1.500)");

        boxOpcoes.getChildren().setAll(
                criarBotaoOpcao("CIRCUITO LOCAL DIÁRIO", Icone.STAR, false, this::selecionarCircuitoLocal),
                criarBotaoOpcao(textoEstadual, Icone.STAR, false, this::selecionarEstadual),
                criarBotaoOpcao(textoNacional, Icone.STAR, false, this::selecionarNacional),
                criarBotaoOpcao("SAIR DA ACADEMIA", Icone.LEFT_ARROW, true, this::sairDaAcademia)
        );
        AudioManager.aplicarHoverNosBotoes(boxOpcoes);
    }

    /** Seleciona circuito local. */
    private void selecionarCircuitoLocal() {
        AudioManager.tocar(Som.CLIQUE);
        int diaAtual = jogo.getDataDia().getDias();
        abrirCardLuta(OponentesBoxe.obterPorDia(diaAtual), 0, 0);
    }

    /** Seleciona estadual. */
    private void selecionarEstadual() {
        Player player = jogo.getPlayer();
        if (player == null) {
            return;
        }
        if (player.getLutasEstaduais() >= LIMITE_LUTAS_ESTADUAIS) {
            AudioManager.tocar(Som.ERRO);
            lblFalante.setText("CINTURÃO ESTADUAL • ENCERRADO");
            lblTexto.setText("VOCÊ JÁ DISPUTOU O CINTURÃO ESTADUAL O MÁXIMO DE VEZES (" + LIMITE_LUTAS_ESTADUAIS + ").");
            mostrarDetalhes(List.of(
                    criarLinhaDetalhe("ESSA VAGA ESTÁ ENCERRADA. AVANCE PARA O CINTURÃO NACIONAL!", "vn-linha-info")
            ));
            definirBotaoVoltar("VOLTAR AO MENU", this::exibirMenuCircuitos);
            return;
        }
        AudioManager.tocar(Som.CLIQUE);
        InfoOponente vitor = new InfoOponente(101, "Vitor", "A Rocha",
                "Campeão Estadual de defesa impenetrável.", new Contragolpeador(), 4, 4, 7, 5, 2000);
        abrirCardLuta(vitor, 500, 1);
    }

    /** Seleciona nacional. */
    private void selecionarNacional() {
        Player player = jogo.getPlayer();
        if (player == null) {
            return;
        }
        if (player.getLutasEstaduais() < LIMITE_LUTAS_ESTADUAIS) {
            AudioManager.tocar(Som.ERRO);
            lblFalante.setText("CINTURÃO NACIONAL • BLOQUEADO");
            lblTexto.setText("É PRECISO DISPUTAR AS " + LIMITE_LUTAS_ESTADUAIS + " LUTAS DO CINTURÃO ESTADUAL ANTES.");
            mostrarDetalhes(List.of(
                    criarLinhaDetalhe("LUTAS ESTADUAIS DISPUTADAS: " + player.getLutasEstaduais()
                            + "/" + LIMITE_LUTAS_ESTADUAIS, "vn-linha-alerta")
            ));
            definirBotaoVoltar("VOLTAR AO MENU", this::exibirMenuCircuitos);
            return;
        }
        if (player.getLutasNacionais() >= LIMITE_LUTAS_NACIONAIS) {
            AudioManager.tocar(Som.ERRO);
            lblFalante.setText("CINTURÃO NACIONAL • ENCERRADO");
            lblTexto.setText("VOCÊ JÁ DISPUTOU O CINTURÃO NACIONAL O MÁXIMO DE VEZES (" + LIMITE_LUTAS_NACIONAIS + ").");
            mostrarDetalhes(List.of(
                    criarLinhaDetalhe("ESSA VAGA ESTÁ ENCERRADA. PREPARE-SE PARA O CAMPEONATO MUNDIAL!", "vn-linha-positiva")
            ));
            definirBotaoVoltar("VOLTAR AO MENU", this::exibirMenuCircuitos);
            return;
        }
        AudioManager.tocar(Som.CLIQUE);
        InfoOponente igor = new InfoOponente(102, "Igor", "O Carrasco",
                "Campeão Nacional explosivo e muito veloz.", new Velocista(), 7, 8, 6, 6, 5000);
        abrirCardLuta(igor, 1500, 2);
    }

    /** Abre ficha da luta. */
    private void abrirCardLuta(InfoOponente oponente, int taxa, int tipoCampeonato) {
        this.oponenteSelecionado = oponente;
        this.taxaSelecionada = taxa;
        this.tipoCampeonatoSelecionado = tipoCampeonato;

        String nomeCircuito = switch (tipoCampeonato) {
            case 1 -> "CINTURÃO ESTADUAL";
            case 2 -> "CINTURÃO NACIONAL";
            default -> "CIRCUITO LOCAL DIÁRIO";
        };

        lblTagCampeonato.setText(nomeCircuito);
        lblNomeAdversario.setText((oponente.nome() + " \"" + oponente.apelido() + "\"").toUpperCase());
        lblEstiloAdversario.setText("ESTILO: " + oponente.estilo().getNome().toUpperCase());
        lblForcaAdv.setText("FOR " + oponente.forca() + "/10");
        lblAgilidadeAdv.setText("AGI " + oponente.agilidade() + "/10");
        lblResistenciaAdv.setText("RES " + oponente.resistencia() + "/10");
        lblInteligenciaAdv.setText("INT " + oponente.inteligencia() + "/10");
        lblPremioAdversario.setText("PRÊMIO: R$ " + oponente.premio()
                + "  •  TAXA: " + (taxa > 0 ? "R$ " + taxa : "GRÁTIS"));
        cardAdversario.setVisible(true);
        cardAdversario.setManaged(true);

        lblFalante.setText("CARD DA LUTA OFICIAL");
        lblTexto.setText(oponente.descricao().toUpperCase());
        mostrarDetalhes(List.of(
                criarLinhaDetalhe("EXIGE SAÚDE >= 30 E ENERGIA >= 25  •  CUSTO DA LUTA: -35 EN E +3 HORAS", "vn-linha-info")
        ));

        String rotuloConfirmar = taxa > 0 ? "PAGAR R$ " + taxa + " E LUTAR" : "SUBIR NO RINGUE";
        boxOpcoes.getChildren().setAll(
                criarBotaoOpcao(rotuloConfirmar, Icone.EXCLAMATION_MARK, false, this::validarEAvancarPostura),
                criarBotaoOpcao("RECUAR AO MENU", Icone.LEFT_ARROW, true, this::exibirMenuCircuitos)
        );
        AudioManager.aplicarHoverNosBotoes(boxOpcoes);
    }

    /** Valida requisitos para escolher um estilo de luta. */
    private void validarEAvancarPostura() {
        Player player = jogo.getPlayer();
        if (player == null) {
            return;
        }
        Atributo attr = player.getAtributos();

        if (player.getDinheiro() < taxaSelecionada) {
            AudioManager.tocar(Som.ERRO);
            lblFalante.setText("INSCRIÇÃO RECUSADA • SEM DINHEIRO");
            lblTexto.setText("DINHEIRO INSUFICIENTE PARA A TAXA DE INSCRIÇÃO DE R$ " + taxaSelecionada + ".");
            mostrarDetalhes(List.of(
                    criarLinhaDetalhe("SEU DINHEIRO ATUAL: R$ " + player.getDinheiro(), "vn-linha-alerta")
            ));
            definirBotaoVoltar("VOLTAR AO MENU", this::exibirMenuCircuitos);
            return;
        }

        if (attr.getSaude() < 30 || attr.getEnergia() < 25) {
            AudioManager.tocar(Som.ERRO);
            lblFalante.setText("MÉDICO DA COMISSÃO");
            lblTexto.setText("O MÉDICO VETOU SUA PARTICIPAÇÃO POR ESTAR DEBILITADO!");
            mostrarDetalhes(List.of(
                    criarLinhaDetalhe("EXIGE SAÚDE >= 30 E ENERGIA >= 25 (ATUAL: "
                            + attr.getSaude() + " HP / " + attr.getEnergia() + " EN).", "vn-linha-alerta")
            ));
            definirBotaoVoltar("VOLTAR AO MENU", this::exibirMenuCircuitos);
            return;
        }

        exibirEscolhaPostura();
    }

    /** Exibe estilos de luta disponíveis. */
    private void exibirEscolhaPostura() {
        AudioManager.tocar(Som.CLIQUE);
        lblFalante.setText("ESCOLHA SUA POSTURA DE LUTA");
        lblTexto.setText("DEFINA SUA POSTURA ANTES DE SUBIR NO RINGUE:");

        HBox linhaVelocista = criarLinhaPostura("VELOCISTA",
                List.of("+AGI (15-75%)", "+20% EN"), List.of("-20% FOR"));
        HBox linhaNocauteador = criarLinhaPostura("NOCAUTEADOR",
                List.of("+FOR (15-75%)"), List.of("-20% AGI", "-20% EN"));
        HBox linhaContragolpeador = criarLinhaPostura("CONTRAGOLPEADOR",
                List.of("+RES (15-75%)", "+INT (15-75%)"), List.of("-15% AGI"));

        boxDetalhes.getChildren().setAll(linhaVelocista, linhaNocauteador, linhaContragolpeador);
        boxDetalhes.setVisible(true);
        boxDetalhes.setManaged(true);

        Button btnVel = criarBotaoOpcao("POSTURA VELOCISTA", Icone.STAR, false, () -> iniciarLuta(new Velocista()));
        Button btnNoc = criarBotaoOpcao("POSTURA NOCAUTEADOR", Icone.STAR, false, () -> iniciarLuta(new Nocauteador()));
        Button btnCon = criarBotaoOpcao("POSTURA CONTRAGOLPEADOR", Icone.STAR, false, () -> iniciarLuta(new Contragolpeador()));
        Button btnVoltar = criarBotaoOpcao("VOLTAR", Icone.LEFT_ARROW, true,
                () -> abrirCardLuta(oponenteSelecionado, taxaSelecionada, tipoCampeonatoSelecionado));

        vincularDestaquePostura(btnVel, linhaVelocista);
        vincularDestaquePostura(btnNoc, linhaNocauteador);
        vincularDestaquePostura(btnCon, linhaContragolpeador);

        boxOpcoes.getChildren().setAll(btnVel, btnNoc, btnCon, btnVoltar);
        AudioManager.aplicarHoverNosBotoes(boxOpcoes);
    }

    /** Inicia luta. */
    private void iniciarLuta(EstiloLuta postura) {
        Player player = jogo.getPlayer();
        if (player == null || oponenteSelecionado == null) {
            return;
        }

        AudioManager.tocar(Som.CONFIRMAR);
        player.setDinheiro(player.getDinheiro() - taxaSelecionada);

        int diaAtual = jogo.getDataDia().getDias();
        int tipoCampeonato = tipoCampeonatoSelecionado;
        InfoOponente oponenteInfo = oponenteSelecionado;

        Lutador lutadorPlayer = new Lutador(player, player.getAtributos(), postura);
        Lutador lutadorOponente = oponenteInfo.criarLutador();
        Match match = new MatchController().executarPartida(lutadorPlayer, lutadorOponente);

        String tituloEvento = switch (tipoCampeonato) {
            case 1 -> "ACADEMIA DE BOXE • CINTURÃO ESTADUAL";
            case 2 -> "ACADEMIA DE BOXE • CINTURÃO NACIONAL";
            default -> "ACADEMIA DE BOXE • CIRCUITO LOCAL";
        };

        LutaContexto contexto = new LutaContexto(
                tituloEvento,
                IMG_ACADEMIA_BOXE,
                IMG_OPONENTE,
                lutadorPlayer,
                lutadorOponente,
                match,
                Tela.ACADEMIA_BOXE,
                resultado -> {
                    List<String> resumo = new ArrayList<>();

                    if (tipoCampeonato == 1) {
                        player.registrarLutaEstadual();
                        if (player.getLutasEstaduais() >= LIMITE_LUTAS_ESTADUAIS
                                && tentarDesbloquear(Conquistas.DESTAQUE_ESTADUAL)) {
                            resumo.add("+CONQUISTA DESBLOQUEADA: " + Conquistas.getNome(Conquistas.DESTAQUE_ESTADUAL));
                        }
                    } else if (tipoCampeonato == 2) {
                        player.registrarLutaNacional();
                        if (player.getLutasNacionais() >= LIMITE_LUTAS_NACIONAIS
                                && tentarDesbloquear(Conquistas.NOME_NACIONAL)) {
                            resumo.add("+CONQUISTA DESBLOQUEADA: " + Conquistas.getNome(Conquistas.NOME_NACIONAL));
                        }
                    } else if (resultado.isVencedor(lutadorPlayer)) {
                        // No circuito local, só as vitórias contam para a progressão.
                        player.registrarLutaLocal();
                    }

                    player.registrarLutaBoxe(diaAtual);
                    jogo.getDataDia().avancarMinutos(180);
                    player.getAtributos().setEnergia(Math.max(0, player.getAtributos().getEnergia() - 35));

                    if (resultado.isVencedor(lutadorPlayer)) {
                        player.setDinheiro(player.getDinheiro() + oponenteInfo.premio());
                        resumo.add("+VITÓRIA NO RINGUE! PRÊMIO RECEBIDO: +R$ " + oponenteInfo.premio()
                                + " (TOTAL: R$ " + player.getDinheiro() + ")");
                    } else if (resultado.isEmpate()) {
                        int meio = oponenteInfo.premio() / 2;
                        player.setDinheiro(player.getDinheiro() + meio);
                        int novaSaude = Math.max(15, player.getAtributos().getSaude() - 20);
                        player.getAtributos().setSaude(novaSaude);
                        resumo.add("+EMPATE! PRÊMIO DIVIDIDO: +R$ " + meio + " • SAÚDE RESTANTE: " + novaSaude + "/100");
                    } else {
                        int novaSaude = Math.max(15, player.getAtributos().getSaude() - 40);
                        player.getAtributos().setSaude(novaSaude);
                        resumo.add("!DERROTA NO RINGUE! SAÚDE RESTANTE: " + novaSaude + "/100 — TREINE MAIS E VOLTE FORTE!");
                    }
                    resumo.add("TEMPO DECORRIDO: +3 HORAS • ENERGIA RESTANTE: "
                            + player.getAtributos().getEnergia() + "/100");
                    return resumo;
                }
        );

        SceneManager.iniciarLuta(contexto);
    }

    /** Tenta registrar a conquista no progresso global. */
    private boolean tentarDesbloquear(String conquista) {
        try {
            return new ConquistaController().desbloquear(conquista);
        } catch (FalhaCarregamentoException | FalhaSalvamentoException e) {
            return false;
        }
    }

    /** Oculta ficha do adversário. */
    private void ocultarCardAdversario() {
        cardAdversario.setVisible(false);
        cardAdversario.setManaged(false);
    }

    /** Retorna da academia ao mapa. */
    private void sairDaAcademia() {
        AudioManager.tocar(Som.CLIQUE);
        jogo.voltarLocal();
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
        boxOpcoes.getChildren().setAll(criarBotaoOpcao(texto, Icone.LEFT_ARROW, true, this::sairDaAcademia));
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
