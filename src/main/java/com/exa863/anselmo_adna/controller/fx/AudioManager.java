package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.exception.DadosInvalidosException;
import com.exa863.anselmo_adna.exception.FalhaCarregamentoException;
import com.exa863.anselmo_adna.persistencia.Configuracao;
import com.exa863.anselmo_adna.persistencia.ConfiguracaoXML;
import com.exa863.anselmo_adna.view.fx.Musica;
import com.exa863.anselmo_adna.view.fx.Som;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

/**
 * Gerencia a reprodução de efeitos sonoros e música respeitando o volume configurado.
 *
 * @author Anselmo e Adna
 */
public final class AudioManager {

    private static final double GANHO_MAXIMO = 0.80;
    private static final Map<Som, AudioClip> CACHE = new EnumMap<>(Som.class);

    private static double volumeEfeitos = 0.60 * GANHO_MAXIMO;
    private static double volumeMusica = 0.80 * GANHO_MAXIMO;
    private static Musica musicaTocando;
    private static MediaPlayer playerMusica;
    private static MediaPlayer playerVinheta;

    private AudioManager() {
    }

    public static void inicializar() {
        Configuracao config = new Configuracao();
        try {
            new ConfiguracaoXML().carregar(config);
        } catch (FalhaCarregamentoException | DadosInvalidosException ignored) {
            // Mantém o volume padrão se não houver configuração salva.
        }
        setVolumeMusica(config.volumeMusica);
        setVolumeEfeitos(config.volumeEfeitos);
    }

    public static void tocar(Som som) {
        if (som == null || volumeEfeitos <= 0.0) {
            return;
        }
        AudioClip clip = obterClip(som);
        if (clip != null) {
            clip.play(volumeEfeitos);
        }
    }

    public static void tocarUmaVez(Musica musica) {
        if (musica == null || volumeMusica <= 0.0) {
            return;
        }
        URL recurso = AudioManager.class.getResource(musica.getCaminho());
        if (recurso == null) {
            return;
        }
        if (playerVinheta != null) {
            playerVinheta.stop();
            playerVinheta.dispose();
        }
        MediaPlayer player = new MediaPlayer(new Media(recurso.toExternalForm()));
        playerVinheta = player;
        player.setVolume(volumeMusica);
        player.setOnEndOfMedia(() -> {
            player.dispose();
            if (playerVinheta == player) {
                playerVinheta = null;
            }
        });
        player.play();
    }

    public static void tocarMusica(Musica musica) {
        if (musica == null) {
            pararMusica();
            return;
        }
        if (musica == musicaTocando && playerMusica != null) {
            return;
        }

        pararMusica();
        URL recurso = AudioManager.class.getResource(musica.getCaminho());
        if (recurso == null) {
            return;
        }
        musicaTocando = musica;
        playerMusica = new MediaPlayer(new Media(recurso.toExternalForm()));
        playerMusica.setCycleCount(MediaPlayer.INDEFINITE);
        playerMusica.setVolume(volumeMusica);
        playerMusica.play();
    }

    public static void pararMusica() {
        if (playerMusica != null) {
            playerMusica.stop();
            playerMusica.dispose();
            playerMusica = null;
        }
        musicaTocando = null;
    }

    public static void setVolumeEfeitos(int percentual) {
        volumeEfeitos = (limitarPercentual(percentual) / 100.0) * GANHO_MAXIMO;
    }

    public static void setVolumeMusica(int percentual) {
        volumeMusica = (limitarPercentual(percentual) / 100.0) * GANHO_MAXIMO;
        if (playerMusica != null) {
            playerMusica.setVolume(volumeMusica);
        }
    }

    public static void aplicarHoverNosBotoes(Parent raiz) {
        if (raiz == null) {
            return;
        }
        for (Node filho : raiz.getChildrenUnmodifiable()) {
            if (filho instanceof Button botao) {
                botao.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                    if (!botao.isDisabled()) {
                        tocar(Som.HOVER);
                    }
                });
            }
            if (filho instanceof Parent subParent) {
                aplicarHoverNosBotoes(subParent);
            }
        }
    }

    private static AudioClip obterClip(Som som) {
        return CACHE.computeIfAbsent(som, s -> {
            URL recurso = AudioManager.class.getResource(s.getCaminho());
            return recurso != null ? new AudioClip(recurso.toExternalForm()) : null;
        });
    }

    private static int limitarPercentual(int valor) {
        return Math.max(0, Math.min(100, valor));
    }
}
