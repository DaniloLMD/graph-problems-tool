package com.github.braully.graph.operation;
import com.github.braully.graph.GraphTO;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class DaniloDirectedWeightedGraphNewOperationTest implements IGraphOperation {

    static final String type = "Danilo";
    static final String description = "Direcionado e Ponderado";

    @Override
    public Map<String, Object> doOperation(GraphTO<Integer, Integer> graph) {
        Map<String, Object> response = new HashMap<>();

        if(!graph.isDirected){
            response.put("ERROR", "Grafo não direcionado");
            return response;

        }

        response.put("Vertices", graph.getVertices().toString());

        String[] edges = graph.getEdgeString().trim().split(",");
        String pesosFormat = "";

        for(int i = 0; i < graph.getEdgeCount(); i++){
            pesosFormat += edges[i] + ": " + graph.edgeWeights.get("" + i);
            if(i+1 != graph.getEdgeCount()) pesosFormat += ", ";
        }

        response.put("Pesos", pesosFormat);

        ArrayList<ArrayList<Integer>> adj = graph.getAdjMatrix();

        response.put("Matriz de Adjacencia", adj);

        response.put("Aresta de 0 pra 1", (adj.get(0).get(1) > -1 ? "SIM" : "NAO"));
        response.put("Aresta de 1 pra 0", (adj.get(1).get(0) > -1 ? "SIM" : "NAO"));

        return response;
    }

    public String getTypeProblem() {
        return type;
    }

    public String getName() {
        return description;
    }
}
