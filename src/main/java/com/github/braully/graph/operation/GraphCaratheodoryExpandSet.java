package com.github.braully.graph.operation;

import com.github.braully.graph.GraphTO;
import static com.github.braully.graph.operation.GraphCaratheodoryAllSetOfSize.log;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.apache.commons.math3.util.CombinatoricsUtils;

public class GraphCaratheodoryExpandSet implements IGraphOperation {

    static final String type = "P3-Convexity";
    static final String description = "Caratheodory Expand Set";

    public static final int NEIGHBOOR_COUNT_INCLUDED = 1;
    public static final int INCLUDED = 2;
    public static final int PROCESSED = 3;

    public Map<String, Object> doOperation(GraphTO<Integer, Integer> graphRead) {
        long totalTimeMillis = -1;
        Collection<Integer> set = graphRead.getSet();
        totalTimeMillis = System.currentTimeMillis();
        Map<String, Object> result = new HashMap<>();

        if (set.size() >= 2) {
            int maxSizeSet = (graphRead.getVertexCount() + 1) / 2;
            int countNCarat = 0;
            for (int size = set.size() + 1; size <= maxSizeSet; size++) {
                Iterator<int[]> combinationsIterator = CombinatoricsUtils.combinationsIterator(graphRead.getVertexCount(), size);
                // precisa de melhorias
                while (combinationsIterator.hasNext()) {
                    int[] currentSet = combinationsIterator.next();
                    boolean isSubset = true;

                    for (Integer iv : set) {
                        boolean ba = false;
                        for (int i = 0; i < size; i++) {
                            ba = ba || currentSet[i] == iv;
                        }
                        isSubset = isSubset && ba;
                    }
                    if (isSubset) {
                        OperationConvexityGraphResult hsp3g = hsp3(graphRead, currentSet);
                        if (hsp3g != null) {
                            String key = "Caratheodory Superset-" + (countNCarat++);
                            result.put(key, hsp3g.caratheodorySet);
                            result.put("Max Caratheodory Superset", hsp3g.caratheodorySet);
                            break;
                        }
                    }
                }
            }
        }
        totalTimeMillis = System.currentTimeMillis() - totalTimeMillis;
        return result;
    }

    public OperationConvexityGraphResult hsp3(GraphTO<Integer, Integer> graph,
            int[] currentSet) {
        OperationConvexityGraphResult processedHullSet = null;
        processedHullSet = hsp3aux(graph, currentSet);
        if (processedHullSet == null || processedHullSet.partial == null || processedHullSet.partial.isEmpty()) {
            processedHullSet = null;
        }
        return processedHullSet;
    }

    public OperationConvexityGraphResult hsp3aux(GraphTO<Integer, Integer> graph, int[] currentSet) {
        int currentSetSize = 0;
        OperationConvexityGraphResult processedHullSet = null;
        Set<Integer> hsp3g = new HashSet<>();
        int[] aux = new int[graph.getVertexCount()];
        int[] auxc = new int[graph.getVertexCount()];
        for (int i = 0; i < aux.length; i++) {
            aux[i] = 0;
            auxc[i] = 0;
        }
        Queue<Integer> mustBeIncluded = new ArrayDeque<>();
        for (Integer v : currentSet) {
            mustBeIncluded.add(v);
            aux[v] = INCLUDED;
            auxc[v] = 1;
            currentSetSize++;
        }
        while (!mustBeIncluded.isEmpty()) {
            Integer verti = mustBeIncluded.remove();
            hsp3g.add(verti);
            Collection<Integer> neighbors = graph.getNeighbors(verti);

            for (int vertn : neighbors) {
                if (vertn == verti) {
                    continue;
                }
                if (vertn != verti && aux[vertn] < INCLUDED) {
                    aux[vertn] = aux[vertn] + NEIGHBOOR_COUNT_INCLUDED;
                    if (aux[vertn] == INCLUDED) {
                        mustBeIncluded.add(vertn);
                    }
                    auxc[vertn] = auxc[vertn] + auxc[verti];
                }
            }
            aux[verti] = PROCESSED;
        }
        boolean checkDerivated = false;
        for (int i = 0; i < graph.getVertexCount(); i++) {
            if (auxc[i] >= currentSet.length && aux[i] == PROCESSED) {
                checkDerivated = true;
                break;
            }
        }
        Set<Integer> partial = null;
        if (checkDerivated) {
            partial = calcDerivatedPartial(graph,
                    hsp3g, currentSet);
        }
        Set<Integer> setCurrent = new HashSet<>();
        for (int i : currentSet) {
            setCurrent.add(i);
        }
        processedHullSet = new OperationConvexityGraphResult();
        processedHullSet.caratheodoryNumber = currentSetSize;
        processedHullSet.auxProcessor = aux;
        processedHullSet.convexHull = hsp3g;
        processedHullSet.caratheodorySet = setCurrent;
        processedHullSet.partial = partial;
        return processedHullSet;
    }

    /**
     * ∂H(S)=H(S)\⋃p∈SH(S\{p})
     *
     * @param graph
     * @param hsp3g
     * @param currentSet
     * @return
     */
    public Set<Integer> calcDerivatedPartial(GraphTO<Integer, Integer> graph,
            Set<Integer> hsp3g, int[] currentSet) {
        Set<Integer> partial = new HashSet<>();
        Queue<Integer> mustBeIncluded = new ArrayDeque<>();
        partial.addAll(hsp3g);

        for (Integer p : currentSet) {
            int[] aux = new int[graph.getVertexCount()];
            for (Integer v : currentSet) {
                if (!v.equals(p)) {
                    mustBeIncluded.add(v);
                    aux[v] = INCLUDED;
                }
            }
            while (!mustBeIncluded.isEmpty() && !partial.isEmpty()) {
                Integer verti = mustBeIncluded.remove();
                partial.remove(verti);
                Collection<Integer> neighbors = graph.getNeighbors(verti);
                for (int vertn : neighbors) {
                    if (vertn != verti) {
                        int previousValue = aux[vertn];
                        aux[vertn] = aux[vertn] + NEIGHBOOR_COUNT_INCLUDED;
                        if (previousValue < INCLUDED && aux[vertn] >= INCLUDED) {
                            mustBeIncluded.add(vertn);
                        }
                    }
                }
            }
        }
        return partial;
    }

    public Set<Integer> calcDerivatedPartial(GraphTO<Integer, Integer> graph,
            Set<Integer> hsp3g, Set<Integer> currentSet) {
        Set<Integer> partial = new HashSet<>();
        Queue<Integer> mustBeIncluded = new ArrayDeque<>();
        partial.addAll(hsp3g);

        for (Integer p : currentSet) {
            int[] aux = new int[graph.getVertexCount()];
            for (Integer v : currentSet) {
                if (!v.equals(p)) {
                    mustBeIncluded.add(v);
                    aux[v] = INCLUDED;
                }
            }
            while (!mustBeIncluded.isEmpty() && !partial.isEmpty()) {
                Integer verti = mustBeIncluded.remove();
                partial.remove(verti);
                Collection<Integer> neighbors = graph.getNeighbors(verti);
                for (int vertn : neighbors) {
                    if (vertn != verti) {
                        int previousValue = aux[vertn];
                        aux[vertn] = aux[vertn] + NEIGHBOOR_COUNT_INCLUDED;
                        if (previousValue < INCLUDED && aux[vertn] >= INCLUDED) {
                            mustBeIncluded.add(vertn);
                        }
                    }
                }
            }
        }
        return partial;
    }

    private OperationConvexityGraphResult hsp3(GraphTO<Integer, Integer> graphRead, Collection<Integer> set) {
        int[] arr = new int[set.size()];
        int i = 0;
        for (Integer v : set) {
            arr[i] = v;
            i++;
        }
        return hsp3(graphRead, arr);
    }

    public boolean isCaratheodorySet(GraphTO<Integer, Integer> graphRead,
            Set<Integer> buildMaxCaratheodorySet) {
        OperationConvexityGraphResult processedHullSet = hsp3(graphRead, buildMaxCaratheodorySet);
        return processedHullSet != null && processedHullSet.partial != null && !processedHullSet.partial.isEmpty();
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
