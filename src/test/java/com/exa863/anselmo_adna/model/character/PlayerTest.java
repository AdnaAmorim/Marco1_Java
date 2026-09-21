package com.exa863.anselmo_adna.model.character;

import com.exa863.anselmo_adna.model.stats.Atributo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes das regras de negócio do jogador principal.
 * Testa controle de dinheiro, limite de treinos por dia, intervalo de lutas e romance.
 *
 * @author Anselmo e Adna
 */
public class PlayerTest {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player(1, "Rocky", "Lutador iniciante", Cores.CASTANHO, Sexo.MASCULINO);
    }

    @Test
    public void testInicializacaoPadrao() {
        assertEquals("Rocky", player.getNome());
        assertEquals(100, player.getDinheiro());
        assertNotNull(player.getAtributos());
        assertNotNull(player.getInventario());
        assertFalse(player.possuiRomance());
    }

    @Test
    public void testLimiteDeTreinosNoMesmoDia() {
        int dia = 1;

        // O jogador começa podendo treinar 5 vezes no dia
        assertEquals(5, player.getTreinosRestantes(dia));
        assertTrue(player.podeTreinarHoje(dia));

        // Realiza os 5 treinos
        for (int i = 0; i < 5; i++) {
            player.registrarTreino(dia);
        }

        // Não deve mais poder treinar no dia 1
        assertEquals(0, player.getTreinosRestantes(dia));
        assertFalse(player.podeTreinarHoje(dia));

        // No dia seguinte o limite reseta para 5 treinos
        assertTrue(player.podeTreinarHoje(dia + 1));
        assertEquals(5, player.getTreinosRestantes(dia + 1));
    }

    @Test
    public void testIntervaloDeLutasDeBoxe() {
        // Nunca lutou, então pode lutar no dia 1
        assertTrue(player.podeLutarBoxe(1));

        // Registra luta no dia 1
        player.registrarLutaBoxe(1);

        // Não pode lutar duas vezes no mesmo dia
        assertFalse(player.podeLutarBoxe(1));
        assertEquals(2, player.getProximoDiaLutaBoxe());

        // No dia seguinte já pode lutar de novo
        assertTrue(player.podeLutarBoxe(2));
    }

    @Test
    public void testRegraDeRomanceUnico() {
        // De início pode começar romance com qualquer pessoa
        assertTrue(player.podeRomancearCom("Olivia"));
        assertTrue(player.podeRomancearCom("Clara"));

        // Inicia romance com a Olivia
        player.definirRomance("Olivia");
        assertTrue(player.possuiRomance());
        assertEquals("Olivia", player.getPersonagemRomance());

        // Pode continuar com a Olivia, mas não com outra personagem
        assertTrue(player.podeRomancearCom("Olivia"));
        assertFalse(player.podeRomancearCom("Clara"));
    }

    @Test
    public void testAfinidadeComPersonagens() {
        Personagem treinador = new Personagem(2, "Mestre Silva", "Treinador experiente", Cores.PRETO, Sexo.MASCULINO);

        assertEquals(0, player.getAfinidade(treinador));

        player.alterarAfinidade(treinador, 20);
        assertEquals(20, player.getAfinidade(treinador));

        player.alterarAfinidade(treinador, -5);
        assertEquals(15, player.getAfinidade(treinador));
    }

    @Test
    public void testConclusaoDeCapitulos() {
        assertFalse(player.isCapituloConcluido("CAPITULO_01"));

        player.concluirCapitulo("CAPITULO_01");
        assertTrue(player.isCapituloConcluido("CAPITULO_01"));
    }

    @Test
    public void testModoDev() {
        player.ativarModoDev();

        assertEquals(20000, player.getDinheiro());
        assertEquals(Atributo.MAX_SAUDE, player.getAtributos().getSaude());
        assertEquals(Atributo.MAX_ENERGIA, player.getAtributos().getEnergia());
        assertEquals(Atributo.MAX_HABILIDADE, player.getAtributos().getForca());
        assertEquals(Atributo.MAX_HABILIDADE, player.getAtributos().getAgilidade());
    }
}
