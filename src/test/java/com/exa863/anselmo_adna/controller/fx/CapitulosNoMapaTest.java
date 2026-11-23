package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.model.character.Cores;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.character.Sexo;
import com.exa863.anselmo_adna.model.world.Local;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Verifica que os capítulos são acionados pelo mapa na ordem e com os requisitos originais. */
public class CapitulosNoMapaTest {

    /** Controlador da partida compartilhado pela interface. */
    private GameController jogo;
    /** Protagonista usado neste contexto. */
    private Player player;

    @BeforeEach
    void preparar() {
        jogo = new GameController();
        player = new Player(1, "Atleta", "Teste", Cores.CASTANHO, Sexo.FEMININO);
        jogo.setPlayer(player);
    }

    /** Procura o local de teste pelo identificador. */
    private Local buscar(Local inicio, String id) {
        if (id.equals(inicio.getId())) return inicio;
        for (Local local : inicio.getSubLocais()) {
            Local encontrado = buscar(local, id);
            if (encontrado != null) return encontrado;
        }
        return null;
    }

    /** Confere se o local aciona o capítulo esperado. */
    private void confere(String idCapitulo, String idLocal) {
        Local local = buscar(jogo.getMapaGlobal(), idLocal);
        assertNotNull(local);
        assertEquals(idCapitulo, CapitulosNoMapa.localizar(jogo, local).orElseThrow().getId());
    }

    @Test
    void percorreDezCapitulosNaOrdemOriginal() {
        String[] locais = {"casa", "academia", "academia_boxe", "academia_boxe",
                "academia_boxe", "academia_profissional", "casa", "academia_boxe",
                "cidade_b", "campeonato_mundial"};
        for (int n = 1; n <= 10; n++) {
            if (n == 4) {
                player.registrarLutaLocal();
                player.registrarLutaLocal();
            }
            if (n == 6) {
                player.registrarLutaEstadual();
                player.registrarLutaEstadual();
            }
            if (n == 9) player.registrarLutaNacional();
            String id = String.format("CAPITULO_%02d", n);
            confere(id, locais[n - 1]);
            player.concluirCapitulo(id);
        }
        assertTrue(CapitulosNoMapa.localizar(jogo, buscar(jogo.getMapaGlobal(), "campeonato_mundial"))
                .isEmpty());
    }

    @Test
    void capituloQuatroNaoLiberaSemDuasLutasLocais() {
        player.concluirCapitulo("CAPITULO_01");
        player.concluirCapitulo("CAPITULO_02");
        player.concluirCapitulo("CAPITULO_03");
        Local local = buscar(jogo.getMapaGlobal(), "academia_boxe");
        assertTrue(CapitulosNoMapa.localizar(jogo, local).isEmpty());
        player.registrarLutaLocal();
        assertTrue(CapitulosNoMapa.localizar(jogo, local).isEmpty());
        player.registrarLutaLocal();
        confere("CAPITULO_04", "academia_boxe");
    }

    @Test
    void capituloNoveExigeUmaLutaNacional() {
        for (int n = 1; n <= 8; n++) {
            player.concluirCapitulo(String.format("CAPITULO_%02d", n));
        }
        Local cidadeB = buscar(jogo.getMapaGlobal(), "cidade_b");
        assertTrue(CapitulosNoMapa.localizar(jogo, cidadeB).isEmpty());
        player.registrarLutaNacional();
        confere("CAPITULO_09", "cidade_b");
    }
}
