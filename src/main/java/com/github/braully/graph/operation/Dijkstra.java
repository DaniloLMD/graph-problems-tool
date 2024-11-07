package com.github.braully.graph.operation;
import com.github.braully.graph.GraphTO;
import com.github.braully.graph.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.ArrayList;


public class Dijkstra implements IGraphOperation {

    static final String type = "Danilo";
    static final String description = "Dijkstra";
    static int INF = Integer.MAX_VALUE;

    int[] parent;


    @Override
    public Map<String, Object> doOperation(GraphTO<Integer, Integer> graph) {
        Map<String, Object> response = new HashMap<>();

        int v = 0, u = graph.getVertexCount() - 1;
        if(graph.getInputData() != null && graph.getInputData().trim().length() > 0){
            String[] input = graph.getInputData().trim().split(" ");
            if(input.length != 2){
                response.put("Input inválido", "Digite 2 numeros separados por espaço");
                return response;
            }
            v = Integer.parseInt(input[0]);
            u = Integer.parseInt(input[1]);

            if(!graph.isVertex(v) || !graph.isVertex((u))){
                response.put("Input inválido", "Vértices informados não existem");
                return response;
            }
        }

        int distance = dijkstra(graph, v, u);

        if(distance == INF){
            response.put("Caminho mínimo de " + v + " para " + u,  "Não existe");
        }
        else{
            response.put("Caminho mínimo de " + v + " para " + u, getShortestPath(v, u));
            response.put("Peso do caminho mínimo de " + v + " para "  + u, distance);
        }
        
        


        return response;
    }

    public ArrayList<Integer> getShortestPath(int v, int u){

        ArrayList<Integer> path = new ArrayList<>();
        ArrayList<Integer> shortest_path = new ArrayList<>();

        for(int at = u; at != v; at = parent[at]){
            path.add(at);
        }
        path.add(v);

        for(int i = path.size() - 1; i >= 0; i--){
            shortest_path.add(path.get(i));
        }

        return shortest_path;
    }

    public int dijkstra(GraphTO<Integer,Integer> graph, int v, int u){

        int[] distances = new int[graph.getVertexCount()];
        boolean[] visited = new boolean[graph.getVertexCount()];
        parent = new int[graph.getVertexCount()];

        ArrayList<ArrayList<Integer>> adj = graph.getAdjMatrix();

        for(int i = 0; i < graph.getVertexCount(); i++){
            visited[i] = false;
        }

        PriorityQueue<Node> q = new PriorityQueue<>();

        distances[u] = INF;

        q.add(new Node(v, 0, v));

        while(q.size() > 0){
            Node n = q.poll();
            v = n.node;
            int w = n.cost;
            int p = n.parent;
            if(visited[v]) continue;
            

            visited[v] = true;
            distances[v] = w;
            parent[v] = p;
            if(v == u) break;
            
            for(int i = 0; i < graph.getVertexCount(); i++){
                if(i == v) continue;
                int edge = adj.get(v).get(i);
                if(edge == -1) continue;
                int edge_weight = graph.getEdgeWeight(edge);
                q.add(new Node(i, w + edge_weight, v));
            }
        }

        return distances[u];
    }

    public String getTypeProblem() {
        return type;
    }

    public String getName() {
        return description;
    }
}
