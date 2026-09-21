package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.model.character.Cores;
import com.exa863.anselmo_adna.model.character.Personagem;
import com.exa863.anselmo_adna.model.character.Sexo;
import com.exa863.anselmo_adna.model.combat.Lutador;
import com.exa863.anselmo_adna.model.combat.ResultadoLuta;
import com.exa863.anselmo_adna.model.combat.TipoVitoria;
import com.exa863.anselmo_adna.model.combat.estilos.Velocista;
import com.exa863.anselmo_adna.model.stats.Atributo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes do controlador de combates.
 * Verifica o cálculo de pontuação dos golpes e a decisão do vencedor (Nocaute, Decisão ou Empate).
 *
 * @author Anselmo e Adna
 */
public class FightControllerTest {

    private FightController controller;
    private Velocista estilo;
    private Personagem p1;
    private Personagem p2;

    @BeforeEach
    public void setUp() {
        controller = new FightController();
        estilo = new Velocista();
        p1 = new Personagem(1, "Campeão", "Lutador do canto vermelho", Cores.PRETO, Sexo.MASCULINO);
        p2 = new Personagem(2, "Desafiante", "Lutador do canto azul", Cores.AZUL, Sexo.MASCULINO);
    }

    @Test
    public void testCalculoDePontosLutadorMaisForte() {
        Atributo forte = new Atributo(100, 10, 10, 10, 10, 100);
        Atributo fraco = new Atributo(50, 2, 2, 2, 2, 50);

        Lutador lutadorForte = new Lutador(p1, forte, estilo);
        Lutador lutadorFraco = new Lutador(p2, fraco, estilo);

        double pontosForte = controller.calcularPontos(lutadorForte.getAtributosEfetivos(), lutadorFraco.getAtributosEfetivos());
        double pontosFraco = controller.calcularPontos(lutadorFraco.getAtributosEfetivos(), lutadorForte.getAtributosEfetivos());

        assertTrue(pontosForte > pontosFraco, "Lutador com melhores atributos deve pontuar mais");
    }

    @Test
    public void testVitoriaPorNocauteComDiferencaGrande() {
        // Diferença extrema de atributos
        Atributo maximo = new Atributo(100, 10, 10, 10, 10, 100);
        Atributo minimo = new Atributo(30, 1, 1, 1, 1, 30);

        Lutador campeao = new Lutador(p1, maximo, estilo);
        Lutador iniciante = new Lutador(p2, minimo, estilo);

        ResultadoLuta resultado = controller.resolverCombate(campeao, iniciante);

        assertEquals(campeao, resultado.getVencedor());
        assertEquals(iniciante, resultado.getPerdedor());
        assertEquals(TipoVitoria.NOCAUTE, resultado.getTipoVitoria());
        assertFalse(resultado.isEmpate());
        assertTrue(resultado.isVencedor(campeao));
    }

    @Test
    public void testLutaTerminaEmEmpateComLutadoresIguais() {
        Atributo iguais = new Atributo(100, 5, 5, 5, 5, 80);

        Lutador l1 = new Lutador(p1, iguais, estilo);
        Lutador l2 = new Lutador(p2, iguais, estilo);

        ResultadoLuta resultado = controller.resolverCombate(l1, l2);

        assertTrue(resultado.isEmpate());
        assertEquals(TipoVitoria.EMPATE, resultado.getTipoVitoria());
        assertNull(resultado.getVencedor());
        assertNull(resultado.getPerdedor());
    }

    @Test
    public void testVitoriaPorDecisaoUnanime() {
        // Diferença moderada para cair na faixa de decisão unânime (saldo entre 8 e 25)
        Atributo bom = new Atributo(100, 5, 5, 5, 5, 80);
        Atributo medio = new Atributo(90, 4, 4, 4, 4, 70);

        Lutador l1 = new Lutador(p1, bom, estilo);
        Lutador l2 = new Lutador(p2, medio, estilo);

        ResultadoLuta resultado = controller.resolverCombate(l1, l2);

        assertEquals(l1, resultado.getVencedor());
        assertEquals(TipoVitoria.DECISAO_UNANIME, resultado.getTipoVitoria());
    }
}
