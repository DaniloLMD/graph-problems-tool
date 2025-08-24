package com.github.braully.graph.operation;
import com.github.braully.graph.GraphTO;
import com.github.braully.graph.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.ArrayList;


public class Prim implements IGraphOperation {

    static final String type = "Mininum Spanning Tree";
    static final String description = "Prim";

    ArrayList<String> addedEdges;
    static int INF = Integer.MAX_VALUE;

    @Override
    public Map<String, Object> doOperation(GraphTO<Integer, Integer> graph) {
        Map<String, Object> response = new HashMap<>();

        int v = 0;

        if(graph.getInputData() != null && graph.getInputData().trim().length() > 0){
            String[] input = graph.getInputData().trim().split(" ");
            if(input.length > 1){
                response.put("Input inválido", "Digite um vertice para iniciar o algoritmo");
                return response;
            }
            v = Integer.parseInt(input[0]);

            if(!graph.isVertex(v)){
                response.put("Input inválido", "Vértice informado não existe");
                return response;
            }
        }

        int peso = prim(graph, v);

        if(peso == INF){
            response.put("Grafo não conexo", "Não há MST");
            return response;
        }

        response.put("Peso da MST", peso);
        response.put("Arestas adicionadas", addedEdges);

        return response;
    }

    public int prim(GraphTO<Integer,Integer> graph, int start){
        
        String edgeString = graph.getEdgeString();
        String[] edges = edgeString != null ? edgeString.trim().split(",") : null;
        
        addedEdges = new ArrayList<>();
        boolean[] used = new boolean[graph.getVertexCount()];
        var adj = graph.getAdjEdgesList();

        int peso = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>();

        pq.add(new Node(start, 0, -1));

        //4-2,3-1,3-0,2-0,2-1,1-1,1-0
        for(int i = 0; i < graph.getVertexCount(); i++){
            if(pq.size() == 0) return INF;

            int v = pq.peek().node;
            if(used[v]){
                pq.poll();
                i--;
                continue;
            }

            peso += pq.peek().cost;
            used[v] = true;

            
            int parent = pq.peek().parent;
            if(parent != -1) addedEdges.add(pq.peek().parent + "-" + v);
            
            pq.poll();

            for(int e: adj.get(v)){

                String[] at = edges[e].split("-");
                int x = Integer.parseInt(at[0]);
                int y = Integer.parseInt(at[1]);

                int u = (x == v) ? y : x;
                
                if(used[u]) continue;

                pq.add(new Node(u, graph.getEdgeWeight(e), v));
            }

        }

        return peso;
    }

    public String getTypeProblem() {
        return type;
    }

    public String getName() {
        return description;
    }
}

