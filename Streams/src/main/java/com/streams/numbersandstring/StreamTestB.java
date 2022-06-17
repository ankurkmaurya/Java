package com.streams.numbersandstring;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTestB {

	public static void main(String[] args){
		q1(); //Q1 : Given a list of integers, find out all the even numbers exist in the list
		System.out.println("");
		q2(); //Q2 : Given a list of integers, find out all the prime numbers exist in the list
		System.out.println("");
		q3(); //Q3 : Given a list of integers, find out all the numbers starting with 1 
		System.out.println("");
		q4(); //Q4 : Find duplicate elements in a given integers list
		System.out.println("");
		q5(); //Q5 : Given the list of integers, find the first element of the list
		System.out.println("");
		q6(); //Q6 : Given a list of integers, find the total number of elements present in the list
		System.out.println("");
		q7(); //Q7 : Given a list of integers, find the maximum value element present in it
		System.out.println("");
		q8(); //Q8 : Given a String, find the first non-repeated character in it
		System.out.println("");
		q9(); //Q9 : Given a String, find the first repeated character in it
		System.out.println("");
		q10(); //Q10 : Given a list of integers, sort all the values present in it
		System.out.println("");
		q11(); //Q11 : Given a list of integers, sort all the values present in it in descending order
		System.out.println("");
	}
	
	/*
	Q1 : Given a list of integers, find out all the even numbers exist in the list
	*/
	private static void q1() {
	    List<Integer> intgrLst = Arrays.asList(10,15,8,49,25,98,32);
	    List<Integer> evenList = intgrLst.stream().filter(i -> i%2==0)
	    		         .collect(Collectors.toList());
	    System.out.println("Q1 : Given a list of integers, find out all the even numbers exist in the list");
	    System.out.println("Numbers : " + intgrLst);
	    System.out.println("Even    : " + evenList);
	}
	
	/*
	Q2 : Given a list of integers, find out all the prime numbers exist in the list
	*/
	private static void q2() {
	    List<Integer> intgrLst = Arrays.asList(10,15,8,49,25,98,32);
	    List<Integer> primeList = intgrLst.stream().filter(i -> {
	    	boolean isPrime = true;
	    	for(int c=2; c<i/2; c++) {
	    		if(i%2==0) {
	    			isPrime = false;
	    			break;
	    			
	    		}
	    		}
	    	return isPrime;
	    	})
	      .collect(Collectors.toList());
	    System.out.println("Q2 : Given a list of integers, find out all the prime numbers exist in the list");
	    System.out.println("Numbers : " + intgrLst);
	    System.out.println("Prime   : " + primeList);
	}
	
	/*
	Q3 : Given a list of integers, find out all the numbers starting with 1 
	*/
	private static void q3() {
	    List<Integer> intgrLst = Arrays.asList(10,15,8,49,25,98,32);
	    List<String> primeList = intgrLst.stream()
	    		.map(i -> i + "")
	    		.filter(s -> s.startsWith("1"))
	    		.collect(Collectors.toList());
	    System.out.println("Q3 : Given a list of integers, find out all the numbers starting with 1");
	    System.out.println("Numbers       : " + intgrLst);
	    System.out.println("Starts with 1 : " + primeList);
	}
	
	/*
	Q4 : Find duplicate elements in a given integers list
	*/
	private static void q4() {
		List<Integer> intgrLst = Arrays.asList(10, 15, 8, 49, 25, 98, 98, 32, 15);
		Map<Integer, Long> duplMap = intgrLst.stream()
				                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		String duplStr = duplMap.entrySet().stream()
							               .filter(e -> e.getValue() > 1)
							               .map(e -> e.getKey() + "")
							               .collect(Collectors.joining(", ", "[", "]"));
		System.out.println("Q4 : Find duplicate elements in a given integers list");
		System.out.println("---: Approach 1");
		System.out.println("Numbers    : " + intgrLst);
		System.out.println("Duplicates : " + duplStr);

		Set<Integer> dupSet = new HashSet<>();
		List<Integer> duplLst = intgrLst.stream().filter(n -> !dupSet.add(n)).collect(Collectors.toList());
		System.out.println("---: Approach 2");
		System.out.println("Numbers    : " + intgrLst);
		System.out.println("Duplicates : " + duplLst);
	}
	
	/*
	Q5 : Given the list of integers, find the first element of the list
	*/
	private static void q5() {
	    List<Integer> intgrLst = Arrays.asList(10,15,8,49,25,98,98,32,15);
	    List<Integer> firstElement = intgrLst.stream()
	    		.limit(1)
	    		.collect(Collectors.toList());
	    System.out.println("Q5 : Given the list of integers, find the first element of the list");
	    System.out.println("---: Approach 1");
	    System.out.println("Numbers       : " + intgrLst);
	    System.out.println("First Element : " + firstElement);
	    
	    Optional<Integer> firstElem = intgrLst.stream().findFirst();
	    System.out.println("---: Approach 2");
	    System.out.println("Numbers       : " + intgrLst);
	    System.out.println("First Element : " + firstElem.get());
	}
	
	/*
	Q6 : Given a list of integers, find the total number of elements present in the list
	*/
	private static void q6() {
	    List<Integer> intgrLst = Arrays.asList(10,15,8,49,25,98,98,32,15);
	    Long noOfElements = intgrLst.stream().count();
	    System.out.println("Q6 : Given a list of integers, find the total number of elements present in the list");
	    System.out.println("Numbers         : " + intgrLst);
	    System.out.println("No. of Elements : " + noOfElements);
	}
	
	/*
	Q7 : Given a list of integers, find the maximum value element present in it
	*/
	private static void q7() {
	    List<Integer> intgrLst = Arrays.asList(10,15,8,49,25,98,103,32,15);
	    Integer maxElements = intgrLst.stream().max((o1, o2) -> o1.compareTo(o2)).get();
	    System.out.println("Q7 : Given a list of integers, find the maximum value element present in it");
	    System.out.println("Numbers      : " + intgrLst);
	    System.out.println("Max Elements : " + maxElements);
	}
	
	/*
	Q8 : Given a String, find the first non-repeated character in it
	*/
	private static void q8() {
		String inputStr = "Java Hungry Blog Alive is Awesome";
		
		Stream<Character> charStrm = inputStr.chars().mapToObj(c->(char)c);
		Map<Character,Long> charOccurMap = charStrm.filter(c->!c.equals(' '))
				                                   .collect(Collectors.groupingBy(Function.identity(), 
				                                		                          LinkedHashMap::new, 
				                                		                          Collectors.counting())
				                                		   );
		
		Character firstNonRepChar = charOccurMap.entrySet()
							                    .stream()
							                    .filter(e->e.getValue().equals(1L))
							                    .map(e->e.getKey())
							                    .findFirst()
							                    .get();
		
		Character lastNonRepChar = charOccurMap.entrySet()
								               .stream()
								               .filter(e->e.getValue().equals(1L))
								               .map(e->e.getKey())
								               .reduce((c1,c2)->c2) //***Here is Power of reduce() method in streams
								               .get();

	    System.out.println("Q8 : Given a String, find the first non-repeated character in it");
	    System.out.println("Input String            : " + inputStr);
	    //charOccurMap.entrySet().stream().forEach(System.out::println);
	    System.out.println("First Non Repeated Char : " + firstNonRepChar);
	    System.out.println("Last Non Repeated Char  : " + lastNonRepChar);
	}
	
	
	/*
	Q9 : Given a String, find the first repeated character in it
	*/
	private static void q9() {
		String inputStr = "Java Hungry Blog Alive is Awesome";
		
		Stream<Character> charStrm = inputStr.chars().mapToObj(c->(char)c);
		Map<Character,Long> charOccurMap = charStrm.filter(c->!c.equals(' '))
				                                   .collect(Collectors.groupingBy(Function.identity(), 
				                                		                          LinkedHashMap::new, 
				                                		                          Collectors.counting())
				                                		   );
		
		Character firstNonRepChar = charOccurMap.entrySet()
							                    .stream()
							                    .filter(e->e.getValue()>1L)
							                    .map(e->e.getKey())
							                    .findFirst()
							                    .get();
		
		Character lastNonRepChar = charOccurMap.entrySet()
								               .stream()
								               .filter(e->e.getValue()>1L)
								               .map(e->e.getKey())
								               .reduce((c1,c2)->c2) //***Here is Power of reduce() method in streams
								               .get();

	    System.out.println("Q9 : Given a String, find the first repeated character in it");
	    System.out.println("Input String            : " + inputStr);
	    //charOccurMap.entrySet().stream().forEach(System.out::println);
	    System.out.println("First Repeated Char : " + firstNonRepChar);
	    System.out.println("Last Repeated Char  : " + lastNonRepChar);
	}
	
	
	/*
	Q10 : Given a list of integers, sort all the values present in it
	*/
	private static void q10() {
	    List<Integer> intgrLst = Arrays.asList(10,15,8,49,25,98,98,32,15);
	    List<Integer> soretdLst = intgrLst.stream().sorted().collect(Collectors.toList());
	    System.out.println("Q10 : Given a list of integers, sort all the values present in it");
	    System.out.println("Numbers        : " + intgrLst);
	    System.out.println("Natural Sorted : " + soretdLst);
	}
	
	
	/*
	Q11 : Given a list of integers, sort all the values present in it in descending order
	*/
	private static void q11() {
	    List<Integer> intgrLst = Arrays.asList(10,15,8,49,25,98,98,32,15);
	    List<Integer> soretdLst = intgrLst.stream().sorted((e1,e2)->e2.compareTo(e1)).collect(Collectors.toList());
	    System.out.println("Q11 : Given a list of integers, sort all the values present in it in descending order");
	    System.out.println("Numbers           : " + intgrLst);
	    System.out.println("Descending Sorted : " + soretdLst);
	}
	
	
	
	
}
