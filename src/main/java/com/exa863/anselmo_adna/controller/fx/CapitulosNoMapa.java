package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.model.narrativa.Capitulo;
import com.exa863.anselmo_adna.model.narrativa.TipoGatilho;
import com.exa863.anselmo_adna.model.world.Local;

import java.util.Optional;

/** Liga os gatilhos narrativos aos locais reais do mapa, sem alterar o grafo original. */
public final class CapitulosNoMapa {

    private CapitulosNoMapa() { }

    public static Optional<Capitulo> localizar(GameController jogo, Local local) {
        if (jogo == null || local == null || jogo.getPlayer() == null) return Optional.empty();
        // O capítulo 1 usa "casa" como alvo; os demais usam nomes como "Academia de Boxe".
        Optional<Capitulo> porId = jogo.getNarrativaController().obterCapituloDisponivel(
                TipoGatilho.ENTRAR_LOCAL, local.getId(), jogo.getPlayer());
        return porId.isPresent() ? porId : jogo.getNarrativaController().obterCapituloDisponivel(
                TipoGatilho.ENTRAR_LOCAL, local.getNome(), jogo.getPlayer());
    }
}
