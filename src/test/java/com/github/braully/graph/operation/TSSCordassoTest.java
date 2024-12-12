package com.github.braully.graph.operation;

import com.github.braully.graph.GraphTO;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import org.junit.Test;

/**
 *
 * @author strike
 */
public class TSSCordassoTest {

    @Test
    public void testBasic() throws FileNotFoundException, IOException {
        GraphTSSCordasco tss = new GraphTSSCordasco();
        GraphTO<Integer, Integer> graphES = new GraphTO<Integer, Integer>("4-1,4-2,3-1,3-0,2-3,2-2,1-1,1-0");
        Set<Integer> tssCordasco = tss.tssCordasco(graphES);
        System.out.println(tssCordasco);
    }
}
