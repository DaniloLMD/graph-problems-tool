package com.github.braully.graph.operation;
import com.github.braully.graph.GraphTO;
import com.github.braully.graph.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.ArrayList;

class Edge {
    int v;
    int u;
    int w;

    public Edge(int v, int u, int w) {
        this.v = v;
        this.u = u;
        this.w = w;
    }
}

public class BellmanFord implements IGraphOperation {

    static final String type = "Danilo";
    static final String description = "Bellman-Ford";
    
    static int INF = Integer.MAX_VALUE;
    
    int distance[];
    int parent[];


    @Override
    public Map<String, Object> doOperation(GraphTO<Integer, Integer> graph) {
        Map<String, Object> response = new HashMap<>();

        Integer v = 0;
        if(graph.getInputData() != null && graph.getInputData().trim().length() > 0){
            String[] input = graph.getInputData().trim().split(" ");
            if(input.length != 1){
                response.put("Input inválido", "Digite o vertice de partida");
                return response;
            }
            
            v = Integer.parseInt(input[0]);

            if(!graph.isVertex(v)){
                response.put("Input inválido", "Vértices informados não existem");
                return response;
            }
        }

        if(!graph.isDirected){
            for(int i = 0; i < graph.getEdgeCount(); i++){
                if(graph.getEdgeWeight(i) < 0){
                    response.put("Ciclo negativo", "Grafo não orientado com peso negativo");
                    return response;
                }
            }
        }
        
        ArrayList<Integer> negative_cicle = bellmanford(graph, v);
        if(negative_cicle != null){
            response.put("Ciclo negativo", negative_cicle);
            return response;
        }

        String distances = "";

        for(int i = 0; i < graph.getVertexCount(); i++){
            distances += "[" + i + ": " + (distance[i] == INF ? "INF" : distance[i]) + "]";
            if(i+1 != graph.getVertexCount()) distances += " , ";
        }
        
        response.put("Peso do caminho minimo", distances);



        return response;
    }

    public ArrayList<Integer> bellmanford(GraphTO<Integer,Integer> graph, int source){

        distance = new int[graph.getVertexCount()];
        parent = new int[graph.getVertexCount()];
        boolean[] visited = new boolean[graph.getVertexCount()];

        ArrayList<ArrayList<Integer>> adj = graph.getAdjMatrix();
        
        String[] edges_str = graph.getEdgeString() != null ? graph.getEdgeString().trim().split(",") : null;
        ArrayList<Edge> edges = new ArrayList<>();

        if (edges_str != null) {
            for (int i = 0; i < edges_str.length; i++) {
                String[] vs = edges_str[i].split("-");
                if (vs.length >= 2) {
                    int v = Integer.parseInt(vs[0].trim());
                    int u = Integer.parseInt(vs[1].trim());
                    

                    int edge_id = adj.get(v).get(u);
                    int edge_weight = graph.getEdgeWeight(edge_id);

                    edges.add(new Edge(v, u, edge_weight));
                    if(!graph.isDirected) edges.add(new Edge(u, v, edge_weight));
                }
            }
        }

        for(int i = 0; i < graph.getVertexCount(); i++){
            visited[i] = false;
            distance[i] = INF;
            parent[i] = -1;
        }

        distance[source] = 0;

        int x = -1;
        for(int i = 0; i < graph.getVertexCount(); i++){
            x = -1;
            
            for(Edge e: edges){
                if(distance[e.v] < INF){
                    if(distance[e.u] > distance[e.v] + e.w){
                        distance[e.u] = distance[e.v] + e.w;
                        parent[e.u] = e.v;
                        x = e.u;
                    }
                }
            }
        }

        if(x != -1){
            ArrayList<Integer> cycle = new ArrayList<>();
            
            for(int i = 0; i < graph.getVertexCount(); i++){
                x = parent[x];
            }

            for(int at = x; ; at = parent[at]){
                cycle.add(at);
                if(at == x && cycle.size() > 1) break;
            }


            ArrayList<Integer> reversed_cycle = new ArrayList<>();
            for(int i = cycle.size() - 1; i >= 0; i--){
                reversed_cycle.add(cycle.get(i));
            }

            return reversed_cycle;
        }
        

        return null;
    }


    public String getTypeProblem() {
        return type;
    }

    public String getName() {
        return description;
    }
}
