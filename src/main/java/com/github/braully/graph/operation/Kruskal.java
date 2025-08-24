package com.github.braully.graph.operation;
import com.github.braully.graph.GraphTO;
import com.github.braully.graph.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.ArrayList;


public class Kruskal implements IGraphOperation {

    static final String type = "Mininum Spanning Tree";
    static final String description = "Kruskal";

    int[] parent, rank;
    ArrayList<String> addedEdges;

    @Override
    public Map<String, Object> doOperation(GraphTO<Integer, Integer> graph) {
        Map<String, Object> response = new HashMap<>();

        int peso = Kruskal(graph);
        response.put("Peso da MST", peso);
        response.put("Arestas adicionadas", addedEdges);

        return response;
    }

    public int Kruskal(GraphTO<Integer,Integer> graph){
        
        parent = new int[graph.getVertexCount()];
        rank = new int[graph.getVertexCount()];
        for(int i = 0; i < graph.getVertexCount(); i++){
            parent[i] = i;
            rank[i] = 0;
        }

        
        String edgeString = graph.getEdgeString();
        String[] edges = edgeString != null ? edgeString.trim().split(",") : null;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        for(int i = 0; i < graph.getEdgeCount(); i++){
            pq.add(new Node(i, graph.getEdgeWeight(i)));
        }
        
        int peso = 0;
        addedEdges = new ArrayList<>();
        while(pq.size() > 0){
            Node node = pq.poll();
            int edge = node.node;
            int w = node.cost;

            String[] vs = edges[edge].split("-");
            if (vs.length >= 2) {
                Integer u =  Integer.parseInt(vs[0].trim());
                Integer v =  Integer.parseInt(vs[1].trim());

                if(union(u,v)){
                    peso += w;
                    addedEdges.add(edges[edge]);
                }

            }
        }


        return peso;
    }

    public int find(int v){
        if(v == parent[v]) return v;
        return parent[v] = find(parent[v]);
    }

    boolean union(int v, int u){   
        v = find (v);
        u = find(u);
        
        if(v == u) return false;
        if(rank[v] > rank[u]){
            int t = v;
            v = u;
            u = t;
        }

        parent[v] = u;
        rank[u]++;
         
        return true;
    }

    public String getTypeProblem() {
        return type;
    }

    public String getName() {
        return description;
    }
}
