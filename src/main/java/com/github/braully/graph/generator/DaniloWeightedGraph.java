package com.github.braully.graph.generator;

import com.github.braully.graph.GraphTO;
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
    public GraphTO<Integer, Integer> generateGraph(Map parameters) {
        Integer nvertices = getIntegerParameter(parameters, N_VERTICES);
        String edges = getStringParameter(parameters, EDGES);
        String weights = getStringParameter(parameters, WEIGHTS);
    
        if (nvertices == null) {
            nvertices = DEFAULT_NVERTICES;
        }

        return (GraphTO<Integer, Integer>) generate(nvertices, edges, weights);
    }

    public GraphTO<Integer, Integer> generate(Integer nvertices, String edges, String weights) {
        GraphTO<Integer, Integer> graph = new GraphTO<>(nvertices, edges, weights);
        graph.setName("Ponderado");

        return graph;
    }
}
