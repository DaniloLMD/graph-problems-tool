package com.github.braully.graph.generator;

import com.github.braully.graph.GraphTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DaniloDirectedWeightedGraph extends DaniloWeightedGraph {
    static final String description = "Grafo Ponderado Direcionado";

    @Override
    public String getDescription() {
        return description;
    }


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
        GraphTO<Integer, Integer> graph = super.generate(nvertices, edges, weights);
        graph.isDirected = true;

        return graph;
    }
}
