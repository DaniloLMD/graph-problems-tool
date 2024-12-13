package com.github.braully.graph.operation;
import com.github.braully.graph.GraphTO;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class FloydWarshall implements IGraphOperation {

    static final String type = "Danilo";
    static final String description = "Floyd-Warshall";
    
    static int INF = Integer.MAX_VALUE;
    
    int distance[][];
    int parent[][];

    @Override
    public Map<String, Object> doOperation(GraphTO<Integer, Integer> graph) {
        Map<String, Object> response = new HashMap<>();

        Integer v = 0, u = graph.getVertexCount() - 1;
        if(graph.getInputData() != null && graph.getInputData().trim().length() > 0){
            String[] input = graph.getInputData().trim().split(" ");
            if(input.length > 2){
                response.put("Input inválido", "Digite até dois vértices");
                return response;
            }
            if(input.length == 1) u = null;
            else u = Integer.parseInt(input[1]);
            
            v = Integer.parseInt(input[0]);

            if(!graph.isVertex(v) || (input.length != 1 && !graph.isVertex(u))){
                response.put("Input inválido", "Vértices informados não existem");
                return response;
            }
        }
        
        int n = graph.getVertexCount();
        floydWarshall(graph);

        if(!graph.isDirected){
            var adj = graph.getAdjMatrix();
            for(int i = 0; i < n; i++){
                if(distance[i][i] < 0){
                    for(int j = 0; j < n; j++){
                        if(j == i) continue;
                        int edge = adj.get(i).get(j);
                        if(edge == -1) continue;
                        if(graph.getEdgeWeight(edge) < 0){
                            response.put("Ciclo Negativo", i + " " + j + " " + i);
                            return response;
                        }
                    }
                }
            }
        }


        for(int i = 0; i < n; i++){
            if(distance[i][i] < 0){
                response.put("Ciclo negativo", getCycle(i, n));
                return response;
            }
        }

        if(u == null){
            String distances = "";
            int i = v;
            for(int j = 0; j < graph.getVertexCount(); j++){
                distances += "[" + j + ": " + (distance[i][j] == INF ? "INF" : distance[i][j]) + "]";
                if(j+1 != graph.getVertexCount()) distances += " , ";
            }

            response.put("Vertice " + i, distances);

            return response;
        }
        
        for(int i = 0; i < graph.getVertexCount(); i++){
            String distances = "";
            for(int j = 0; j < graph.getVertexCount(); j++){
                distances += "[" + j + ": " + (distance[i][j] == INF ? "INF" : distance[i][j]) + "]";
                if(j+1 != graph.getVertexCount()) distances += " , ";
            }

            response.put("Vertice " + i, distances);
        }

        String path = distance[v][u] < INF ? getPath(v,u) : "Não existe";

        response.put("Caminho de " + v + " para " + u, path);

        return response;
    }

    public boolean floydWarshall(GraphTO<Integer,Integer> graph){
        int n = graph.getVertexCount();
        distance = new int[n][];
        parent = new int[n][];
        for(int i = 0; i < n; i++){
            distance[i] = new int[n];
            parent[i] = new int[n];
        }

        var adj = graph.getAdjMatrix();

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                distance[i][j] = INF;

                int edge = adj.get(i).get(j);
                if(edge != -1){
                    distance[i][j] = graph.getEdgeWeight(edge);
                }

                if (distance[i][j] != INF && i != j)
                    parent[i][j] = i;
                else
                    parent[i][j] = -1;
            }
            distance[i][i] = 0;
        }

        for(int k = 0; k < n; k++){
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    if(distance[i][k] < INF && distance[k][j] < INF){
                        if(distance[i][j] > distance[i][k] + distance[k][j]){
                            distance[i][j] = distance[i][k] + distance[k][j];
                            parent[i][j] = parent[k][j];
                        }
                    }
                }
            }
        }

        for(int i = 0; i < n; i++){
            if(distance[i][i] < 0){
                return false;
            }
        }

        return false;
    }   

    public String getPath(int v, int u){
        if(v == u){
            return v + "";
        }
        if(parent[v][u] == -1) return "[ERROR]";
        return getPath(v, parent[v][u]) + " " + u;
    }
    
    public String getCycle(int v, int n){
        String cycle = "";
        boolean visited[] = new boolean[n];

        int u = v;
        while(true){
            if(visited[u]) break;
            visited[u] = true;
            cycle += " " + u;
            u = parent[v][u];
        }
        cycle += " " + u;

        String reversed_cycle = "";
        for(int i = cycle.length() - 1; i >= 0; i--){
            char c = cycle.charAt(i);
            reversed_cycle += c;
        }

        return reversed_cycle;
    }

    public String getTypeProblem() {
        return type;
    }

    public String getName() {
        return description;
    }
}
