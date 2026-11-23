package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.model.narrativa.Capitulo;
import com.exa863.anselmo_adna.model.world.Local;
import com.exa863.anselmo_adna.controller.fx.modais.ConfirmacaoController;
import com.exa863.anselmo_adna.view.fx.LutaContexto;
import com.exa863.anselmo_adna.view.fx.Som;
import com.exa863.anselmo_adna.view.fx.Tela;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Controla o carregamento, a troca de telas e os modais no JavaFX.
 *
 * @author Anselmo e Adna
 */
public final class SceneManager {

    /** Css global. */
    private static final String CSS_GLOBAL = "/styles/global.css";

    /** Container raiz. */
    private static StackPane containerRaiz;
    /** Game controller. */
    private static GameController gameController;
    /** Confirmação saida aberta. */
    private static boolean confirmacaoSaidaAberta;

    private static Parent menuPausa;
    /** Tela desfocada. */
    private static Node telaDesfocada;
    /** Efeito anterior. */
    private static Effect efeitoAnterior;
    /** Ação executada ao retomar a tela pausada. */
    private static Runnable aoRetomarPausa;
    // Contexto temporário: capítulos disparados pelo mapa usam o mesmo FXML do prólogo.
    /** Inicio capítulo pendente. */
    private static InicioCapitulo inicioCapituloPendente;

    /** Capítulo pendente e tela de retorno após a cena. */
    public record InicioCapitulo(Capitulo capitulo, Tela telaRetorno) { }

    /** Entra no capítulo disponível para o local, respeitando dependências e requisitos. */
    public static boolean iniciarCapituloSeDisponivel(Local local, Tela telaRetorno) {
        if (local == null || gameController == null || gameController.getPlayer() == null) return false;
        var disponivel = CapitulosNoMapa.localizar(gameController, local);
        if (disponivel.isEmpty()) return false;
        inicioCapituloPendente = new InicioCapitulo(disponivel.get(),
                telaRetorno != null ? telaRetorno : Tela.MAPA);
        try {
            irPara(Tela.CUTSCENE);
            return true;
        } catch (RuntimeException e) {
            inicioCapituloPendente = null;
            throw e;
        }
    }

    /** Consumido uma única vez durante o initialize da Cutscene.fxml. */
    public static InicioCapitulo consumirInicioCapitulo() {
        InicioCapitulo inicio = inicioCapituloPendente;
        inicioCapituloPendente = null;
        return inicio;
    }

    /** Impede a criação de instâncias desta classe utilitária. */
    private SceneManager() {
    }

    /** Prepara os recursos necessários à inicialização. */
    public static void inicializar(Stage stage, Tela telaInicial) {
        AudioManager.inicializar();
        gameController = new GameController();
        containerRaiz = new StackPane(carregar(telaInicial));

        Scene scene = new Scene(containerRaiz);
        URL cssGlobal = SceneManager.class.getResource(CSS_GLOBAL);
        if (cssGlobal != null) {
            scene.getStylesheets().add(cssGlobal.toExternalForm());
        }
        stage.setScene(scene);
        AudioManager.tocarMusica(telaInicial.getMusica());
    }

    /** Monta a abertura sobre o menu já carregado para que a animação possa usar a posição do logo do menu. */
    public static void inicializarComAbertura(Stage stage, Tela abertura, Tela telaPrincipal) {
        AudioManager.inicializar();
        gameController = new GameController();
        containerRaiz = new StackPane(carregar(telaPrincipal), carregar(abertura));

        Scene scene = new Scene(containerRaiz);
        URL cssGlobal = SceneManager.class.getResource(CSS_GLOBAL);
        if (cssGlobal != null) {
            scene.getStylesheets().add(cssGlobal.toExternalForm());
        }
        stage.setScene(scene);
    }

    /** Retorna controlador da partida compartilhado pelas telas. */
    public static GameController getGameController() {
        if (gameController == null) {
            gameController = new GameController();
        }
        return gameController;
    }

    /** Substitui a tela atual pela tela solicitada. */
    public static void irPara(Tela tela) {
        verificarInicializacao();
        limparEstadoPausa();
        containerRaiz.getChildren().setAll(carregar(tela));
        AudioManager.tocarMusica(tela.getMusica());
    }

    /** Inicia luta. */
    public static void iniciarLuta(LutaContexto contexto) {
        verificarInicializacao();
        limparEstadoPausa();
        FXMLLoader loader = criarLoader(Tela.LUTA);
        try {
            Parent raiz = loader.load();
            AudioManager.aplicarHoverNosBotoes(raiz);
            LutaController controller = loader.getController();
            controller.iniciar(contexto);
            containerRaiz.getChildren().setAll(raiz);
            AudioManager.tocarMusica(Tela.LUTA.getMusica());
        } catch (IOException e) {
            throw new RuntimeException("Falha ao carregar a tela de luta.", e);
        }
    }

    /** Sobrepõe a tela solicitada à tela atual. */
    public static void abrirModal(Tela tela) {
        verificarInicializacao();
        AudioManager.tocar(Som.ABRIR_MODAL);
        containerRaiz.getChildren().add(carregar(tela));
    }

    /** Abre um menu de pausa sem trocar a cena, aplicando Blur apenas ao jogo atrás. */
    public static boolean abrirMenuPausa(Runnable aoRetomar) {
        verificarInicializacao();
        if (menuPausa != null || containerRaiz.getChildren().size() != 1) return false;
        Parent modal = carregar(Tela.MENU_PAUSA);
        telaDesfocada = containerRaiz.getChildren().get(0);
        efeitoAnterior = telaDesfocada.getEffect();
        telaDesfocada.setEffect(new GaussianBlur(13));
        aoRetomarPausa = aoRetomar;
        menuPausa = modal;
        AudioManager.tocar(Som.ABRIR_MODAL);
        containerRaiz.getChildren().add(modal);
        return true;
    }

    /** Indica se o menu de pausa está aberto. */
    public static boolean pausaAtiva() {
        return menuPausa != null;
    }

    /** Só fecha a pausa quando ela for o modal do topo (sem configurações abertas). */
    public static void fecharMenuPausa() {
        verificarInicializacao();
        int total = containerRaiz.getChildren().size();
        if (menuPausa == null || total <= 1 || containerRaiz.getChildren().get(total - 1) != menuPausa) return;
        containerRaiz.getChildren().remove(menuPausa);
        Runnable aoRetomar = aoRetomarPausa;
        limparEstadoPausa();
        AudioManager.tocar(Som.FECHAR_MODAL);
        if (aoRetomar != null) aoRetomar.run();
    }

    /** Abre confirmação. */
    public static void abrirConfirmacao(String titulo, String mensagem, Runnable aoConfirmar) {
        abrirConfirmacao(titulo, mensagem, null, "CONFIRMAR", aoConfirmar);
    }

    /** Abre confirmação. */
    public static void abrirConfirmacao(String titulo, String mensagem, String aviso,
                                        String textoConfirmar, Runnable aoConfirmar) {
        verificarInicializacao();
        FXMLLoader loader = criarLoader(Tela.CONFIRMACAO);
        try {
            Parent modal = loader.load();
            AudioManager.aplicarHoverNosBotoes(modal);
            ConfirmacaoController controller = loader.getController();
            controller.configurar(titulo, mensagem, aviso, textoConfirmar, aoConfirmar);
            AudioManager.tocar(Som.ABRIR_MODAL);
            containerRaiz.getChildren().add(modal);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao carregar o modal de confirmação.", e);
        }
    }

    /** Solicita confirmação de saída do jogo. */
    public static void solicitarSaida() {
        if (confirmacaoSaidaAberta) {
            return;
        }
        confirmacaoSaidaAberta = true;
        abrirConfirmacao(
                "SAIR DO JOGO",
                "TEM CERTEZA QUE DESEJA ENCERRAR O BOXING GAME?",
                "QUALQUER PROGRESSO NÃO SALVO SERÁ PERDIDO.",
                "SAIR",
                () -> System.exit(0)
        );
    }

    /** Retira o modal do topo da tela. */
    public static void fecharModal() {
        fecharModal(true);
    }

    /** Retira o modal do topo da tela. */
    public static void fecharModal(boolean tocarSom) {
        verificarInicializacao();
        int total = containerRaiz.getChildren().size();
        if (total > 1) {
            Node modal = containerRaiz.getChildren().get(total - 1);
            if (modal == menuPausa) {
                fecharMenuPausa();
                return;
            }
            containerRaiz.getChildren().remove(total - 1);
            confirmacaoSaidaAberta = false;
            if (tocarSom) {
                AudioManager.tocar(Som.FECHAR_MODAL);
            }
        }
    }

    /** Limpa referências ao menu de pausa e à tela de fundo. */
    private static void limparEstadoPausa() {
        if (telaDesfocada != null) telaDesfocada.setEffect(efeitoAnterior);
        telaDesfocada = null;
        efeitoAnterior = null;
        menuPausa = null;
        aoRetomarPausa = null;
    }

    /** Verifica inicialização do gerenciador de cenas. */
    private static void verificarInicializacao() {
        if (containerRaiz == null) {
            throw new IllegalStateException("SceneManager ainda não foi inicializado.");
        }
    }

    /** Cria carregador do arquivo FXML da tela. */
    private static FXMLLoader criarLoader(Tela tela) {
        URL recurso = SceneManager.class.getResource(tela.getCaminhoFxml());
        if (recurso == null) {
            throw new IllegalArgumentException("Arquivo FXML não encontrado: " + tela.getCaminhoFxml());
        }
        return new FXMLLoader(recurso);
    }

    /** Carrega o conteúdo FXML da tela solicitada. */
    private static Parent carregar(Tela tela) {
        try {
            Parent raiz = criarLoader(tela).load();
            AudioManager.aplicarHoverNosBotoes(raiz);
            return raiz;
        } catch (IOException e) {
            throw new RuntimeException("Falha ao carregar a tela: " + tela.getCaminhoFxml(), e);
        }
    }
}
