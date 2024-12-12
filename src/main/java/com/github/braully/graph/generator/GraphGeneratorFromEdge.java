package com.github.braully.graph.generator;

import com.github.braully.graph.GraphTO;
import java.util.Map;

public class GraphGeneratorFromEdge extends AbstractGraphGenerator {

    static final String N_VERTICES = "Nº Vertices";
    static final String STRING_EDGES = "Edge-string";
    static final String[] parameters = {N_VERTICES, STRING_EDGES};
    static final String description = "From Edges String";
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

        if (nvertices == null) {
            nvertices = DEFAULT_NVERTICES;
        }

        String strEdges = getStringParameter(parameters, STRING_EDGES);

        GraphTO<Integer, Integer> graph = new GraphTO<>();
        graph.setName("ES" + N_VERTICES);

        graph.addEdgesFromString(strEdges);
        return graph;
    }

}
