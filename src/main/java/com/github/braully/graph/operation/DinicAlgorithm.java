package com.github.braully.graph.operation;

import com.github.braully.graph.GraphTO;

import java.util.*;

public class DinicAlgorithm implements IGraphOperation {

    static final String type = "Flow";
    static final String description = "Dinic";
    int vertexCount;
    int[][] capacity;
    int[][] flow;
    List<Integer>[] adj;
    int[] level;
    int[] ptr;

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
        buildGraph(graph);

        int maxFlow = dinic(source, sink);
        response.put("Fluxo máximo de " + source + " para " + sink, maxFlow);

        return response;
    }

    private void buildGraph(GraphTO<Integer, Integer> graph) {
        capacity = new int[vertexCount][vertexCount];
        flow = new int[vertexCount][vertexCount];
        adj = new ArrayList[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            adj[i] = new ArrayList<>();
        }

        ArrayList<ArrayList<Integer>> matrix = graph.getAdjMatrix();

        for (int u = 0; u < vertexCount; u++) {
            for (int v = 0; v < vertexCount; v++) {
                int edge = matrix.get(u).get(v);
                if (edge != -1) {
                    int w = graph.getEdgeWeight(edge);
                    capacity[u][v] = w;
                    adj[u].add(v);
                    adj[v].add(u); // para permitir fluxo reverso
                }
            }
        }
    }

    private boolean bfs(int source, int sink) {
        level = new int[vertexCount];
        Arrays.fill(level, -1);
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        level[source] = 0;

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v : adj[u]) {
                if (level[v] == -1 && flow[u][v] < capacity[u][v]) {
                    level[v] = level[u] + 1;
                    queue.add(v);
                }
            }
        }

        return level[sink] != -1;
    }

    private int dfs(int u, int sink, int pushed) {
        if (pushed == 0) return 0;
        if (u == sink) return pushed;

        for (; ptr[u] < adj[u].size(); ptr[u]++) {
            int v = adj[u].get(ptr[u]);
            if (level[v] == level[u] + 1 && flow[u][v] < capacity[u][v]) {
                int tr = dfs(v, sink, Math.min(pushed, capacity[u][v] - flow[u][v]));
                if (tr > 0) {
                    flow[u][v] += tr;
                    flow[v][u] -= tr;
                    return tr;
                }
            }
        }

        return 0;
    }

    private int dinic(int source, int sink) {
        int maxFlow = 0;

        while (bfs(source, sink)) {
            ptr = new int[vertexCount];
            int pushed;
            while ((pushed = dfs(source, sink, Integer.MAX_VALUE)) > 0) {
                maxFlow += pushed;
            }
        }

        return maxFlow;
    }

    @Override
    public String getTypeProblem() {
        return type;
    }

    @Override
    public String getName() {
        return description;
    }
}
