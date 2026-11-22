package com.exa863.anselmo_adna.view.fx;

import com.exa863.anselmo_adna.model.combat.Lutador;
import com.exa863.anselmo_adna.model.combat.Match;
import com.exa863.anselmo_adna.model.combat.ResultadoLuta;

import java.util.List;
import java.util.function.Function;

/**
 * Dados genéricos de uma luta para alimentar a tela de combate JavaFX.
 *
 * @author Anselmo e Adna
 */
public record LutaContexto(
        String tituloEvento,
        String imagemCenario,
        String imagemOponente,
        String imagemNarrador,
        Lutador lutador1,
        Lutador lutador2,
        Match match,
        Tela telaRetorno,
        Function<ResultadoLuta, List<String>> aoFinalizar
) {
    public static final String NARRADOR_BAIRRO = "/images/personagens/narrador_bairro.png";
    public static final String NARRADOR_PROFISSIONAL = "/images/personagens/narrador_luta.png";

    public LutaContexto(String tituloEvento, String imagemCenario, String imagemOponente,
                        Lutador lutador1, Lutador lutador2, Match match, Tela telaRetorno,
                        Function<ResultadoLuta, List<String>> aoFinalizar) {
        this(tituloEvento, imagemCenario, imagemOponente, NARRADOR_BAIRRO,
                lutador1, lutador2, match, telaRetorno, aoFinalizar);
    }
}
