package com.github.braully.graph.generator;

import com.github.braully.graph.GraphTO;
import java.util.Map;

public class GraphGeneratorWheel extends GraphGeneratorCycle {

    static final String description = "Wheel";

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public GraphTO<Integer, Integer> generateGraph(Map parameters) {
        Integer nvertices = getIntegerParameter(parameters, N_VERTICES);
        if (nvertices == null) {
            nvertices = DEFAULT_NVERTICES;
        }

        GraphTO<Integer, Integer> graph = super.generateGraph(parameters);
        graph.setName("W" + nvertices);
        graph.addVertex(nvertices);
        int ec = graph.getEdgeCount();
        for (int i = 0; i < nvertices; i++) {
            graph.addEdge(ec++, i, nvertices);
        }
        return graph;
    }
}
