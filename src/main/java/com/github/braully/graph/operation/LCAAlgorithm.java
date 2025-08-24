package com.github.braully.graph.operation;

import com.github.braully.graph.GraphTO;
import java.util.*;

public class LCAAlgorithm implements IGraphOperation {
    static final String type = "General";
    static final String description = "Lowest Common Ancestor (LCA)";

    private int n, l, root;
    private List<Integer>[] adj;
    private int timer;
    private int[] tin, tout;
    private int[][] up;

    @Override
    public Map<String, Object> doOperation(GraphTO<Integer, Integer> graph) {
        Map<String, Object> response = new HashMap<>();
        String input = graph.getInputData() != null ? graph.getInputData().trim() : "";
        String[] tokens = input.isEmpty() ? new String[0] : input.split("\\s+");

        // obter número de vértices e matriz
        n = graph.getVertexCount();
        ArrayList<ArrayList<Integer>> mat = graph.getAdjMatrix();

        // verificação de árvore
        int edgeCount = 0;
        for (int u = 0; u < n; u++) {
            for (int v = u + 1; v < n; v++) {
                if (mat.get(u).get(v) != -1) edgeCount++;
            }
        }
        if (edgeCount != n - 1) {
            response.put("Erro", "O grafo de entrada não é uma árvore (deve ter n-1 arestas)");
            return response;
        }

        // construir adj
        adj = new ArrayList[n];
        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
                if (mat.get(u).get(v) != -1) adj[u].add(v);
            }
        }

        // definir raiz e/ou consulta LCA
        int queryU = -1, queryV = -1;
        if (tokens.length == 1) {
            // único token: define raiz
            try { root = Integer.parseInt(tokens[0]); } catch (Exception e) { root = 0; }
            if (root < 0 || root >= n) root = 0;
        } else if (tokens.length >= 2) {
            // dois tokens: raiz default e consulta
            try {
                queryU = Integer.parseInt(tokens[0]);
                queryV = Integer.parseInt(tokens[1]);
            } catch (Exception e) {
                response.put("Erro", "Entrada deve ser dois índices válidos de vértices para consulta LCA");
                return response;
            }
            root = 0;
        } else {
            root = 0;
        }

        // preprocess lifting
        l = (int) Math.ceil(Math.log(n) / Math.log(2));
        tin = new int[n]; tout = new int[n];
        up = new int[n][l + 1];
        timer = 0;
        dfs(root, root);

        if (queryU >= 0 && queryV >= 0) {
            if (queryU < 0 || queryU >= n || queryV < 0 || queryV >= n) {
                response.put("Erro", "Índices de vértice fora do intervalo [0, n-1]");
            } else {
                int ancestor = lca(queryU, queryV);
                response.put("LCA de " + queryU + " e " + queryV, ancestor);
            }
        } else {
            response.put("Descrição", "Pré-processamento completo. Utilize lca(u,v) no código para consultas futuras.");
        }
        return response;
    }

    private void dfs(int v, int p) {
        tin[v] = ++timer;
        up[v][0] = p;
        for (int i = 1; i <= l; ++i) up[v][i] = up[ up[v][i - 1] ][ i - 1 ];
        for (int u : adj[v]) if (u != p) dfs(u, v);
        tout[v] = ++timer;
    }

    private boolean isAncestor(int u, int v) {
        return tin[u] <= tin[v] && tout[u] >= tout[v];
    }

    public int lca(int u, int v) {
        if (isAncestor(u, v)) return u;
        if (isAncestor(v, u)) return v;
        for (int i = l; i >= 0; --i) if (!isAncestor(up[u][i], v)) u = up[u][i];
        return up[u][0];
    }

    @Override public String getTypeProblem() { return type; }
    @Override public String getName() { return description; }
}
