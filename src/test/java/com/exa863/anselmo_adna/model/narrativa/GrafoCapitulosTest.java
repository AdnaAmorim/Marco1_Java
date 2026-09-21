package com.exa863.anselmo_adna.model.narrativa;

import com.exa863.anselmo_adna.model.narrativa.capitulos.Capitulo01;
import com.exa863.anselmo_adna.model.narrativa.capitulos.Capitulo02;
import com.exa863.anselmo_adna.model.narrativa.capitulos.Capitulo03;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes do grafo direcionado da história (JGraphT).
 * Verifica se os pré-requisitos dos capítulos e a ordenação topológica funcionam corretamente.
 *
 * @author Anselmo e Adna
 */
public class GrafoCapitulosTest {

    private Capitulo01 cap1;
    private Capitulo02 cap2;
    private Capitulo03 cap3;
    private GrafoCapitulos grafo;

    @BeforeEach
    public void setUp() {
        cap1 = new Capitulo01();
        cap2 = new Capitulo02();
        cap3 = new Capitulo03();
        grafo = new GrafoCapitulos(List.of(cap1, cap2, cap3));
    }

    @Test
    public void testCapituloInicialDisponivelDePrimeira() {
        // Capítulo 1 não tem dependências, deve estar disponível logo no início
        assertTrue(grafo.estaDisponivel(Capitulo01.ID, Set.of()));
    }

    @Test
    public void testCapituloComDependenciaBloqueadoAteConcluirAnterior() {
        // Capítulo 2 depende do 1, não pode estar disponível de início
        assertFalse(grafo.estaDisponivel(Capitulo02.ID, Set.of()));

        // Após concluir o Capítulo 1, o 2 é liberado
        assertTrue(grafo.estaDisponivel(Capitulo02.ID, Set.of(Capitulo01.ID)));
    }

    @Test
    public void testCapituloJaConcluidoNaoFicaDisponivel() {
        // Se já foi concluído, não deve aparecer mais como disponível
        assertFalse(grafo.estaDisponivel(Capitulo01.ID, Set.of(Capitulo01.ID)));
    }

    @Test
    public void testObterPreRequisitos() {
        // Cap 1 não tem pré-requisitos
        assertTrue(grafo.obterPreRequisitos(Capitulo01.ID).isEmpty());

        // Cap 2 depende do Cap 1
        Set<String> preReqsCap2 = grafo.obterPreRequisitos(Capitulo02.ID);
        assertEquals(1, preReqsCap2.size());
        assertTrue(preReqsCap2.contains(Capitulo01.ID));

        // Cap 3 depende do Cap 2
        Set<String> preReqsCap3 = grafo.obterPreRequisitos(Capitulo03.ID);
        assertEquals(1, preReqsCap3.size());
        assertTrue(preReqsCap3.contains(Capitulo02.ID));
    }

    @Test
    public void testOrdenacaoTopologicaCronologica() {
        // Passa a lista fora de ordem para validar a ordenação do grafo
        GrafoCapitulos grafoDesordenado = new GrafoCapitulos(List.of(cap3, cap1, cap2));
        List<Capitulo> ordem = grafoDesordenado.obterOrdemCronologica();

        assertEquals(3, ordem.size());
        assertEquals(Capitulo01.ID, ordem.get(0).getId());
        assertEquals(Capitulo02.ID, ordem.get(1).getId());
        assertEquals(Capitulo03.ID, ordem.get(2).getId());
    }

    @Test
    public void testBuscarCapituloPorId() {
        Optional<Capitulo> achado = grafo.buscarPorId(Capitulo01.ID);
        assertTrue(achado.isPresent());
        assertEquals("Origens", achado.get().getTitulo());

        Optional<Capitulo> inexistente = grafo.buscarPorId("CAPITULO_99");
        assertTrue(inexistente.isEmpty());
    }
}
