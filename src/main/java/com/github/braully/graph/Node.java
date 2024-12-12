package com.github.braully.graph;
import java.util.Comparator;
/*
    classe para facilitar a implementação dos algoritmos que precisam comparar o custo
    ate um vertice do grafo.
*/

public class Node implements Comparator<Node>, Comparable<Node> {
    public int node;
    public int cost;
    public int parent;
 
    public Node(int node, int cost, int parent)
    {
        this.node = node;
        this.cost = cost;
        this.parent = parent;
    }
    
    public Node(int node, int cost)
    {
        this.node = node;
        this.cost = cost;
    }
 
    @Override public int compare(Node node1, Node node2)
    {
        if (node1.cost < node2.cost)
            return -1;
 
        if (node1.cost > node2.cost)
            return 1;
 
        return 0;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.cost, other.cost);
    }
}