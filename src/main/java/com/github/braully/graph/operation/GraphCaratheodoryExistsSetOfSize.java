package com.github.braully.graph.operation;

import com.github.braully.graph.GraphTO;
import java.util.Map;

/**
 *
 * @author Braully Rocha da Silva
 */
public class GraphCaratheodoryExistsSetOfSize extends GraphCaratheodoryNumberBinary {

    static final String type = "P3-Convexity";
    static final String description = "Caratheodory Exists Set of Size";

    public Map<String, Object> doOperation(GraphTO<Integer, Integer> graph) {
        OperationConvexityGraphResult processedCaratheodroySet = null;
        Map<String, Object> result = null;
        if (graph == null) {
            return result;
        }

        Integer size = null;

        try {
            String inputData = graph.getInputData();
            size = Integer.parseInt(inputData);
        } catch (Exception e) {

        }
        if (size == null) {
            throw new IllegalArgumentException("Input invalid (not integer): " + graph.getInputData());
        }

        if (size >= 2) {
            processedCaratheodroySet = findCaratheodroySetBruteForce(graph, size);
        }

        if (processedCaratheodroySet == null) {
            processedCaratheodroySet = new OperationConvexityGraphResult();
        }
        processedCaratheodroySet.caratheodoryNumber = null;
        Map<String, Object> toMap = processedCaratheodroySet.toMap();
        return toMap;
    }

    public String getTypeProblem() {
        return type;
    }

    public String getName() {
        return description;
    }
}
