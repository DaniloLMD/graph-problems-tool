package com.github.braully.graph.operation;
import com.github.braully.graph.UndirectedSparseGraphTO;
import java.util.HashMap;
import java.util.Map;

public class DaniloNewOperationTest implements IGraphOperation {

    static final String type = "Danilo";
    static final String description = "Testando implementar uma nova operacao.";


    @Override
    public Map<String, Object> doOperation(UndirectedSparseGraphTO<Integer, Integer> graph) {
        Map<String, Object> response = new HashMap<>();

        response.put("Orientador", "Hebert");
        response.put("Qtd. Alunos", 3);
        response.put("Input", graph.getInputData());

        response.put("Algoritmos", "Caminho minimo & MST");

        return response;
    }

    public String getTypeProblem() {
        return type;
    }

    public String getName() {
        return description;
    }
}
