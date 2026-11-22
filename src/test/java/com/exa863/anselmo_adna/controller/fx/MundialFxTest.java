package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.model.character.Cores;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.character.Sexo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MundialFxTest {
    @Test
    void selecionaOsQuatroDesfechosEEncontraSuasIlustracoes() {
        var player = new Player(1, "Alex", "Atleta", Cores.VERDE, Sexo.FEMININO);
        assertEquals(MundialFx.Final.LEGADO, MundialFx.desfecho(player, false));
        assertEquals(MundialFx.Final.CAMPEAO, MundialFx.desfecho(player, true));
        player.concluirCapitulo("FLAG_CONTRATO_ACEITO");
        assertEquals(MundialFx.Final.DERROTA, MundialFx.desfecho(player, false));
        assertEquals(MundialFx.Final.TRAGEDIA, MundialFx.desfecho(player, true));
        for (var fim : MundialFx.Final.values()) {
            for (String sexo : new String[]{"feminino", "masculino"}) {
                assertNotNull(getClass().getResource("/images/cutscenes/final_" + fim.numero + "_" + sexo + ".png"));
            }
        }
    }
}
