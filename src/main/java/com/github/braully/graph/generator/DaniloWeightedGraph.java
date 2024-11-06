package com.github.braully.graph.generator;

import com.github.braully.graph.WeightedUndirectedSparseGraphTO;
import com.github.braully.graph.UndirectedSparseGraphTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DaniloWeightedGraph extends AbstractGraphGenerator {

    static final String N_VERTICES = "N";
    static final String EDGES = "Arestas";
    static final String WEIGHTS = "Pesos";
    static final String[] parameters = {N_VERTICES, EDGES, WEIGHTS};
    static final String description = "Grafo Ponderado";
    static final Integer DEFAULT_NVERTICES = 5;

    @Override
    public String[] getParameters() {
        return parameters;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public UndirectedSparseGraphTO<Integer, Integer> generateGraph(Map parameters) {
        Integer nvertices = getIntegerParameter(parameters, N_VERTICES);
        String edges = getStringParameter(parameters, EDGES);
        String weights = getStringParameter(parameters, WEIGHTS);
    
        if (nvertices == null) {
            nvertices = DEFAULT_NVERTICES;
        }

        return (UndirectedSparseGraphTO<Integer, Integer>) generate(nvertices, edges, weights);
    }

    public WeightedUndirectedSparseGraphTO<Integer, Integer> generate(Integer nvertices, String edges, String weights) {
        WeightedUndirectedSparseGraphTO<Integer, Integer> graph = new WeightedUndirectedSparseGraphTO<>();
        graph.setName("Ponderado");

        Integer[] vertexs = new Integer[nvertices];

        for (int i = 0; i < nvertices; i++) {
            vertexs[i] = i;
            graph.addVertex(vertexs[i]);
        }

        graph.addEdgesFromString(edges, weights);

        return graph;
    }
}
