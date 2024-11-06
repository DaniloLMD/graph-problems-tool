package com.github.braully.graph;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import java.util.*;

public class WeightedUndirectedSparseGraphTO<V extends Number, E extends Number> extends UndirectedSparseGraphTO {
    // private Map<E, Integer> edgeWeights;

    public WeightedUndirectedSparseGraphTO() {
        super();
    }

    public WeightedUndirectedSparseGraphTO(String strEdgesGraph, String strWeights) {
        this();
        addEdgesFromString(strEdgesGraph, strWeights);
    }

    public void addEdgesFromString(String strEdges, String strWeights) {
        String[] edges = strEdges != null ? strEdges.trim().split(",") : null;
        String[] weights = strWeights != null ? strWeights.trim().split(",") : null;
        
        if (edges != null && weights != null && edges.length == weights.length) {
            int countEdge = this.getEdgeCount();
            try {
                for (int i = 0; i < edges.length; i++) {
                    String[] vs = edges[i].split("-");
                    if (vs.length >= 2) {
                        V source = (V) (Number) Integer.parseInt(vs[0].trim());
                        V target = (V) (Number) Integer.parseInt(vs[1].trim());
                        Integer weight = Integer.parseInt(weights[i].trim());
                        E edge = (E) (Number) countEdge++;

                        addEdge(edge, source, target, weight);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public E addEdge(V v1, V v2, Integer weight) {
        E edge = (E) (Number) this.getEdgeCount();
        addEdge(edge, v1, v2, weight);
        return edge;
    }

    public void addEdge(E edge, V v1, V v2, Integer weight) {
        super.addEdge(edge, v1, v2);
        edgeWeights.put(edge, weight);
    }

    // public Integer getEdgeWeight(E edge) {
    //     return (Integer) edgeWeights.get(edge);
    // }

    // public void setEdgeWeight(E edge, Integer weight) {
    //     edgeWeights.put(edge, weight);
    // }

    // @JsonIgnore
    // @Override
    // public Collection<E> getEdges() {
    //     return super.getEdges();
    // }

    @JsonIgnore
    public Map<E, Integer> getEdgeWeights() {
        return Collections.unmodifiableMap(edgeWeights);
    }

    public void setEdgeWeights(Map<E, Integer> weights) {
        edgeWeights = new HashMap<>(weights);
    }

    // @Override
    // public void setVertices(Collection<V> vertices) {
    //     if (vertices != null) {
    //         for (V vertex : vertices) {
    //             this.addVertex(vertex);
    //         }
    //     }
    // }

    @Override
    public WeightedUndirectedSparseGraphTO<V, E> clone() {
        WeightedUndirectedSparseGraphTO<V,E> clone = (WeightedUndirectedSparseGraphTO) super.clone();
        clone.setEdgeWeights(this.edgeWeights);

        return clone;
    }

}
