package com.github.braully.graph.operation;

import com.github.braully.graph.GraphTO;
import com.github.braully.graph.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class FordFulkerson implements IGraphOperation {

    static final String type = "Flow";
    static final String description = "Ford-Fulkerson";

    int vertexCount;
    int[][] capacity;
    int[] parent;

    @Override
    public Map<String, Object> doOperation(GraphTO<Integer, Integer> graph) {
        Map<String, Object> response = new HashMap<>();

        int source = 0, sink = graph.getVertexCount() - 1;
        if (graph.getInputData() != null && graph.getInputData().trim().length() > 0) {
            String[] input = graph.getInputData().trim().split(" ");
            if (input.length != 2) {
                response.put("Input inválido", "Digite 2 números separados por espaço");
                return response;
            }
            source = Integer.parseInt(input[0]);
            sink = Integer.parseInt(input[1]);

            if (!graph.isVertex(source) || !graph.isVertex(sink)) {
                response.put("Input inválido", "Vértices informados não existem");
                return response;
            }
        }

        vertexCount = graph.getVertexCount();

        // Construir matriz de capacidade do grafo
        buildCapacityMatrix(graph);

        int maxFlow = fordFulkerson(source, sink);

        response.put("Fluxo máximo de " + source + " para " + sink, maxFlow);

        return response;
    }

    private void buildCapacityMatrix(GraphTO<Integer, Integer> graph) {
        capacity = new int[vertexCount][vertexCount];
        ArrayList<ArrayList<Integer>> adj = graph.getAdjMatrix();

        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {
                int edge = adj.get(i).get(j);
                if (edge != -1) {
                    capacity[i][j] = graph.getEdgeWeight(edge);
                } else {
                    capacity[i][j] = 0;
                }
            }
        }
    }

    private boolean dfs(int s, int t, int[] parent, boolean[] visited) {
        visited[s] = true;
        if (s == t) return true;

        for (int v = 0; v < vertexCount; v++) {
            if (!visited[v] && capacity[s][v] > 0) {
                parent[v] = s;
                if (dfs(v, t, parent, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int fordFulkerson(int source, int sink) {
        parent = new int[vertexCount];
        int maxFlow = 0;

        while (true) {
            boolean[] visited = new boolean[vertexCount];
            // Busca caminho aumentante via DFS
            boolean foundPath = dfs(source, sink, parent, visited);

            if (!foundPath) break;

            // Encontrar fluxo mínimo no caminho aumentante
            int pathFlow = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, capacity[u][v]);
            }

            // Atualizar capacidades residuais
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                capacity[u][v] -= pathFlow;
                capacity[v][u] += pathFlow;  // capacidade residual reversa
            }

            maxFlow += pathFlow;
        }

        return maxFlow;
    }

    public String getTypeProblem() {
        return type;
    }

    public String getName() {
        return description;
    }
}
