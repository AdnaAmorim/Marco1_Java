package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.exception.DadosInvalidosException;
import com.exa863.anselmo_adna.exception.FalhaCarregamentoException;
import com.exa863.anselmo_adna.exception.FalhaSalvamentoException;
import com.exa863.anselmo_adna.persistencia.Configuracao;
import com.exa863.anselmo_adna.persistencia.ConfiguracaoXML;
import com.exa863.anselmo_adna.view.fx.Icone;
import com.exa863.anselmo_adna.view.fx.Som;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.util.function.IntConsumer;

/**
 * Controla o modal de configurações do jogo.
 *
 * @author Anselmo e Adna
 */
public class ConfiguracoesController {

    private static final int PASSO_VOLUME = 10;

    private final Configuracao config = new Configuracao();
    private final ConfiguracaoXML configXml = new ConfiguracaoXML();

    private int volumeMusicaInicial;
    private int volumeEfeitosInicial;

    @FXML private Label lblAutoSave;
    @FXML private Label lblPularCombate;
    @FXML private Label lblPularCutscene;
    @FXML private Label lblMusica;
    @FXML private Label lblEfeitos;
    @FXML private Button btnAutoSave;
    @FXML private Button btnPularCombate;
    @FXML private Button btnPularCutscene;
    @FXML private Slider sliderVolumeMusica;
    @FXML private Label lblVolumeMusica;
    @FXML private Slider sliderVolumeEfeitos;
    @FXML private Label lblVolumeEfeitos;
    @FXML private Label lblMensagem;
    @FXML private Button btnSalvar;
    @FXML private Button btnCancelar;

    @FXML
    public void initialize() {
        try {
            configXml.carregar(config);
        } catch (FalhaCarregamentoException | DadosInvalidosException e) {
            lblMensagem.setText(e.getMessage().toUpperCase());
        }

        volumeMusicaInicial = config.volumeMusica;
        volumeEfeitosInicial = config.volumeEfeitos;

        vincularSlider(sliderVolumeMusica, lblVolumeMusica, config.volumeMusica, valor -> {
            config.volumeMusica = valor;
            AudioManager.setVolumeMusica(valor);
            atualizarIconeMusica();
        });
        vincularSlider(sliderVolumeEfeitos, lblVolumeEfeitos, config.volumeEfeitos, valor -> {
            config.volumeEfeitos = valor;
            AudioManager.setVolumeEfeitos(valor);
            atualizarIconeEfeitos();
        });

        configurarIcones();
        atualizarBotoes();
    }

    private void configurarIcones() {
        IconManager.aplicar(lblAutoSave, Icone.FLOPPY_DRIVE, 16, IconManager.COR_DESTAQUE);
        IconManager.aplicar(lblPularCombate, Icone.FORWARD, 16, IconManager.COR_DESTAQUE);
        IconManager.aplicar(lblPularCutscene, Icone.NEXT, 16, IconManager.COR_DESTAQUE);
        atualizarIconeMusica();
        atualizarIconeEfeitos();
        IconManager.aplicarBotao(btnSalvar, Icone.FLOPPY_DRIVE, 17);
        IconManager.aplicarBotaoPerigo(btnCancelar, Icone.LEFT_ARROW, 16);
    }

    private void atualizarIconeMusica() {
        Icone icone = config.volumeMusica == 0 ? Icone.SPEAKER_CROSSED : Icone.MUSIC_1;
        IconManager.aplicar(lblMusica, icone, 16,
                config.volumeMusica == 0 ? IconManager.COR_PERIGO : IconManager.COR_DESTAQUE);
    }

    private void atualizarIconeEfeitos() {
        Icone icone = config.volumeEfeitos == 0 ? Icone.SPEAKER_CROSSED
                : config.volumeEfeitos <= 50 ? Icone.SPEAKER_0 : Icone.SPEAKER_1;
        IconManager.aplicar(lblEfeitos, icone, 16,
                config.volumeEfeitos == 0 ? IconManager.COR_PERIGO : IconManager.COR_DESTAQUE);
    }

    @FXML
    public void alternarAutoSave() {
        config.autoSave = !config.autoSave;
        AudioManager.tocar(config.autoSave ? Som.TOGGLE_ON : Som.TOGGLE_OFF);
        atualizarBotoes();
    }

    @FXML
    public void alternarPularCombate() {
        config.pularAnimacaoCombate = !config.pularAnimacaoCombate;
        AudioManager.tocar(config.pularAnimacaoCombate ? Som.TOGGLE_ON : Som.TOGGLE_OFF);
        atualizarBotoes();
    }

    @FXML
    public void alternarPularCutscene() {
        config.pularCutscene = !config.pularCutscene;
        AudioManager.tocar(config.pularCutscene ? Som.TOGGLE_ON : Som.TOGGLE_OFF);
        atualizarBotoes();
    }

    @FXML
    public void salvarEVoltar() {
        try {
            configXml.salvar(config);
            AudioManager.tocar(Som.CONFIRMAR);
            SceneManager.fecharModal(false);
        } catch (FalhaSalvamentoException e) {
            AudioManager.tocar(Som.ERRO);
            lblMensagem.setText(e.getMessage().toUpperCase());
        }
    }

    @FXML
    public void cancelar() {
        AudioManager.setVolumeMusica(volumeMusicaInicial);
        AudioManager.setVolumeEfeitos(volumeEfeitosInicial);
        SceneManager.fecharModal();
    }

    private void vincularSlider(Slider slider, Label label, int valorInicial, IntConsumer aoMudar) {
        int inicialArredondado = arredondarPasso(valorInicial);
        final int[] ultimoValor = { inicialArredondado };

        slider.setValue(inicialArredondado);
        label.setText(inicialArredondado + "%");
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int valor = arredondarPasso(newVal.doubleValue());
            if (valor != ultimoValor[0]) {
                ultimoValor[0] = valor;
                aoMudar.accept(valor);
                label.setText(valor + "%");
                AudioManager.tocar(Som.SLIDER_STEP);
            }
        });
    }

    private int arredondarPasso(double valor) {
        return (int) (Math.round(valor / PASSO_VOLUME) * PASSO_VOLUME);
    }

    private void atualizarBotoes() {
        atualizarToggle(btnAutoSave, config.autoSave);
        atualizarToggle(btnPularCombate, config.pularAnimacaoCombate);
        atualizarToggle(btnPularCutscene, config.pularCutscene);
    }

    private void atualizarToggle(Button botao, boolean ativo) {
        botao.setText(ativo ? "SIM" : "NÃO");
        botao.getStyleClass().remove("toggle-ativo");
        if (ativo) {
            botao.getStyleClass().add("toggle-ativo");
        }
    }
}
