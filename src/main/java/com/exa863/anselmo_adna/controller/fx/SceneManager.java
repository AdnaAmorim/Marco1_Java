package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.fx.modais.ConfirmacaoController;
import com.exa863.anselmo_adna.view.fx.Som;
import com.exa863.anselmo_adna.view.fx.Tela;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    private static final String CSS_GLOBAL = "/styles/global.css";

    private static StackPane containerRaiz;
    private static GameController gameController;
    private static boolean confirmacaoSaidaAberta;

    private SceneManager() {
    }

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

    public static GameController getGameController() {
        if (gameController == null) {
            gameController = new GameController();
        }
        return gameController;
    }

    public static void irPara(Tela tela) {
        verificarInicializacao();
        containerRaiz.getChildren().setAll(carregar(tela));
        AudioManager.tocarMusica(tela.getMusica());
    }

    public static void abrirModal(Tela tela) {
        verificarInicializacao();
        AudioManager.tocar(Som.ABRIR_MODAL);
        containerRaiz.getChildren().add(carregar(tela));
    }

    public static void abrirConfirmacao(String titulo, String mensagem, Runnable aoConfirmar) {
        abrirConfirmacao(titulo, mensagem, null, "CONFIRMAR", aoConfirmar);
    }

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

    public static void fecharModal() {
        fecharModal(true);
    }

    public static void fecharModal(boolean tocarSom) {
        verificarInicializacao();
        int total = containerRaiz.getChildren().size();
        if (total > 1) {
            containerRaiz.getChildren().remove(total - 1);
            confirmacaoSaidaAberta = false;
            if (tocarSom) {
                AudioManager.tocar(Som.FECHAR_MODAL);
            }
        }
    }

    private static void verificarInicializacao() {
        if (containerRaiz == null) {
            throw new IllegalStateException("SceneManager ainda não foi inicializado.");
        }
    }

    private static FXMLLoader criarLoader(Tela tela) {
        URL recurso = SceneManager.class.getResource(tela.getCaminhoFxml());
        if (recurso == null) {
            throw new IllegalArgumentException("Arquivo FXML não encontrado: " + tela.getCaminhoFxml());
        }
        return new FXMLLoader(recurso);
    }

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
