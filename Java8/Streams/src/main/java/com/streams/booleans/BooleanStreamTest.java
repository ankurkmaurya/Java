
package com.streams.booleans;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author Ankur Maurya
 * 
 */
public class BooleanStreamTest {
 
    public static void main(String[] args) {
       
        boolean[] bolArrayF = {true, false, true, true, true};
        boolean[] bolArrayT = {true, true, true, true, true};
        
        Stream<Boolean> streamBolF = IntStream.range(0, bolArrayF.length).mapToObj(i -> bolArrayF[i]);	
        Stream<Boolean> streamBolT = IntStream.range(0, bolArrayT.length).mapToObj(i -> bolArrayT[i]);
        
        System.out.println("---- Convert boolean[] to List<Boolean> ----");
        List<Boolean> bolLF = streamBolF.collect(Collectors.toList());
        System.out.println(bolLF);
        List<Boolean> bolLT = streamBolT.collect(Collectors.toList());
        System.out.println(bolLT);
        System.out.println("");
        
        System.out.println("---- Count Number of True/False in List ----");
        System.out.println("F TRUE Count - " + bolLF.stream().filter(e-> e.equals(Boolean.TRUE)).count());
        System.out.println("T TRUE Count - " + bolLT.stream().filter(e-> e.equals(Boolean.TRUE)).count());
        System.out.println("");
        
        System.out.println("---- Boolean Value Accumulator ----");
        boolean bolF = bolLF.stream().reduce((a, b) -> a && b).get();
        System.out.println("Accumulator Value F - " + bolF);
        boolean bolT = bolLT.stream().reduce((a, b) -> a && b).get();
        System.out.println("Accumulator Value T - " + bolT);
        
    }
    
}
