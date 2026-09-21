package com.exa863.anselmo_adna.model.narrativa;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Grafo direcionado que organiza a ordem dos capítulos da história usando a biblioteca JGraphT.
 * Garante que um capítulo só fique disponível quando seus pré-requisitos forem concluídos.
 *
 * @author Anselmo e Adna
 */
public class GrafoCapitulos {

    private final DirectedAcyclicGraph<String, DefaultEdge> grafo;
    private final Map<String, Capitulo> capitulosPorId;

    /**
     * Monta o grafo adicionando os capítulos e ligando as dependências entre eles.
     *
     * @param capitulos Capítulos que fazem parte da história.
     */
    public GrafoCapitulos(Collection<Capitulo> capitulos) {
        this.grafo = new DirectedAcyclicGraph<>(DefaultEdge.class);
        this.capitulosPorId = capitulos.stream()
                .collect(Collectors.toMap(Capitulo::getId, Function.identity()));

        construirGrafo(capitulos);
    }

    private void construirGrafo(Collection<Capitulo> capitulos) {
        for (Capitulo capitulo : capitulos) {
            grafo.addVertex(capitulo.getId());
        }

        for (Capitulo dependente : capitulos) {
            String dependenteId = dependente.getId();

            for (String preRequisitoId : dependente.getDependencias()) {
                if (!capitulosPorId.containsKey(preRequisitoId)) {
                    throw new IllegalStateException("Capítulo " + dependenteId
                            + " aponta para dependência inexistente: " + preRequisitoId);
                }

                grafo.addEdge(preRequisitoId, dependenteId);
            }
        }
    }

    public boolean estaDisponivel(String capituloId, Set<String> concluidos) {
        if (!grafo.containsVertex(capituloId) || concluidos.contains(capituloId)) {
            return false;
        }

        Set<String> preRequisitos = obterPreRequisitos(capituloId);
        return concluidos.containsAll(preRequisitos);
    }

    public Set<String> obterPreRequisitos(String capituloId) {
        if (!grafo.containsVertex(capituloId)) {
            return Collections.emptySet();
        }

        Set<String> preRequisitos = new HashSet<>();
        for (DefaultEdge aresta : grafo.incomingEdgesOf(capituloId)) {
            preRequisitos.add(grafo.getEdgeSource(aresta));
        }
        return Collections.unmodifiableSet(preRequisitos);
    }

    public List<Capitulo> obterOrdemCronologica() {
        List<Capitulo> ordem = new ArrayList<>();
        TopologicalOrderIterator<String, DefaultEdge> iterator = new TopologicalOrderIterator<>(grafo);

        while (iterator.hasNext()) {
            ordem.add(capitulosPorId.get(iterator.next()));
        }
        return Collections.unmodifiableList(ordem);
    }

    public Optional<Capitulo> buscarPorId(String id) {
        return Optional.ofNullable(capitulosPorId.get(id));
    }

    public Collection<Capitulo> getTodosCapitulos() {
        return Collections.unmodifiableCollection(capitulosPorId.values());
    }
}
