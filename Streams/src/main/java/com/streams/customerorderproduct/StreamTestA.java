
package com.streams.customerorderproduct;

import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


public class StreamTestA {
    
    private static final ProductRepo productRepo; 
    private static final OrderRepo orderRepo;  
    
    
   static {
        productRepo = new ProductRepo(); 
        orderRepo = new OrderRepo();   
   }

    
   public static void main(String[] args){
	   
       ex1(); //Exercise 1 — Obtain a list of products belongs to category 'Books' with price > 100
       System.out.println(" ");
       ex2(); //Exercise 2 — Obtain a list of order with products belong to category 'Baby'
       System.out.println(" ");
       ex3(); //Exercise 3 — Obtain a list of product with category = 'Toys' and then apply 10% discount
       System.out.println(" ");
       ex4(); //Exercise 4 — Obtain a list of products ordered by customer of tier 2 between 01-Feb-2022 and 01-Apr-2022
       System.out.println(" ");
       ex5(); //Exercise 5 — Get the cheapest products of “Books” category
       System.out.println(" ");
       ex6(); //Exercise 6 — Get the 3 most recent placed order
       System.out.println(" ");
       ex7(); //Exercise 7 — Get a list of orders which were ordered on 30-Mar-2022, log the order records to the console and then return its product list
       System.out.println(" ");
       ex8(); //Exercise 8 — Calculate total lump sum of all orders placed in Feb 2021
       System.out.println(" ");
       ex9(); //Exercise 9 — Calculate order average payment placed in Feb 2021
       System.out.println(" ");
       ex10(); //Exercise 10 — Obtain a collection of statistic figures (i.e. sum, average, max, min, count) for all products of category 'Books'
       System.out.println(" ");
       ex11(); //Exercise 11 — Obtain a data map with order id and order’s product list
       System.out.println(" ");
       ex12(); //Exercise 12 — Obtain a data map with order id and order’s product count
       System.out.println(" ");
       ex13(); //Exercise 13 — Produce a data map with order records grouped by customer
       System.out.println(" ");
       ex14(); //Exercise 14 — Produce a data map grouped by customer with number of order placed
       System.out.println(" ");
       ex15(); //Exercise 15 — Produce a data map with order record and product total sum
       System.out.println(" ");
       ex16(); //Exercise 16 — Obtain a data map with list of product name by category
       System.out.println(" ");
	   ex17(); //Exercise 17 — Obtain a data map with no of products in each category
       System.out.println(" ");
       ex18(); //Exercise 18 — Get the most expensive product by category
       System.out.println(" ");

   }
    

   
   /*
      Exercise 1 — Obtain a list of products belongs to category 'Books' with price > 100
   */
    public static void ex1() {
        long startTime = System.currentTimeMillis();
        List<Product> products = productRepo.findAll().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase("books"))
                .filter(p -> p.getPrice()>100)
                .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("Exercise 1 - Obtain a list of products belongs to category 'Books' with price > 100 [execution time: %1$d ms]", (endTime - startTime)));
        products.forEach(System.out::println);
    }
   
    
    /*
       Exercise 2 — Obtain a list of order with products belong to category 'Baby'
    */
    public static void ex2() {
        long startTime = System.currentTimeMillis();
        List<Order> orders = orderRepo.findAll().stream()
                .filter(o -> o.getProducts().stream()
                        .anyMatch(p -> p.getCategory().equalsIgnoreCase("baby"))
                )
                .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("Exercise 2 - Obtain a list of order with products belong to category 'Baby' [execution time: %1$d ms]", (endTime - startTime)));
        orders.forEach(System.out::println);
    }
    
    
    /*
      Exercise 3 — Obtain a list of product with category = 'Toys' and then apply 10% discount
    */
     public static void ex3() {
        long startTime = System.currentTimeMillis();
        List<Product> products = productRepo.findAll().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase("toys"))
                .map(p -> p.withPrice(p.getPrice() + (p.getPrice()*0.1)))
                .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("Exercise 3 — Obtain a list of product with category = 'Toys' and then apply 10% discount [execution time: %1$d ms]", (endTime - startTime)));
        products.forEach(System.out::println);
    }
   
     
     /*
        Exercise 4 — Obtain a list of products ordered by customer of tier 2 between 01-Feb-2022 and 01-Apr-2022
     */
	public static void ex4() {
		long startTime = System.currentTimeMillis();
		List<Product> products = orderRepo.findAll().stream()
				.filter(o -> o.getCustomer().getTier() == 2)
				.filter(o -> o.getOrderDate().isEqual(LocalDate.of(2022, Month.FEBRUARY, 01)) ||
						     o.getOrderDate().isAfter(LocalDate.of(2022, Month.FEBRUARY, 01))
						)
				.filter(o -> o.getOrderDate().isEqual(LocalDate.of(2022, Month.APRIL, 01)) ||
					         o.getOrderDate().isBefore(LocalDate.of(2022, Month.APRIL, 01))
					   )
				.flatMap(o -> o.getProducts().stream())
				.distinct()
				.collect(Collectors.toList());
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("Exercise 4 — Obtain a list of products ordered by customer of tier 2 between 01-Feb-2022 and 01-Apr-2022 [execution time: %1$d ms]", (endTime - startTime)));
		products.forEach(System.out::println);
	}
   
   
	 /*
	    Exercise 5 — Get the cheapest products of “Books” category
	 */
	public static void ex5() {
		long startTime = System.currentTimeMillis();
		List<Product> products = productRepo.findAll().stream()
				.filter(p -> p.getCategory().equalsIgnoreCase("books"))
				.sorted(Comparator.comparing(Product::getPrice))
				.limit(1)
				.collect(Collectors.toList());
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("Exercise 5I — Get the cheapest products of “Books” category. [execution time: %1$d ms]", (endTime-startTime)));
		products.forEach(System.out::println);
		
		long startTimeA = System.currentTimeMillis();
		Optional<Product> optProductA = productRepo.findAll().stream()
				.filter(p -> p.getCategory().equalsIgnoreCase("books"))
				.sorted(Comparator.comparing(Product::getPrice))
				.findFirst();
		long endTimeA = System.currentTimeMillis();
		System.out.println(String.format("Exercise 5II — Get the cheapest products of “Books” category. [execution time: %1$d ms]", (endTimeA-startTimeA)));
		System.out.println(optProductA.get());
		
		long startTimeB = System.currentTimeMillis();
		Optional<Product> optProductB = productRepo.findAll().stream()
				.filter(p -> p.getCategory().equalsIgnoreCase("books"))
				.min(Comparator.comparing(Product::getPrice));
		long endTimeB = System.currentTimeMillis();
		System.out.println(String.format("Exercise 5III — Get the cheapest products of “Books” category. [execution time: %1$d ms]", (endTimeB-startTimeB)));
		System.out.println(optProductB.get());
	}
	
	
	/*
	   Exercise 6 — Get the 3 most recent placed order
    */
	public static void ex6() {
		long startTime = System.currentTimeMillis();
		List<Order> orders = orderRepo.findAll().stream()
				.sorted(Comparator.comparing(Order::getOrderDate, (d1, d2) -> d2.compareTo(d1)))
				.limit(3)
				.collect(Collectors.toList());
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("Exercise 6I — Get the 3 most recent placed order. [execution time: %1$d ms]", (endTime - startTime)));
		orders.forEach(System.out::println);
		
		long startTimeA = System.currentTimeMillis();
		List<Order> ordersA = orderRepo.findAll().stream()
				.sorted(Comparator.comparing(Order::getOrderDate).reversed())
				.limit(3)
				.collect(Collectors.toList());
		long endTimeA = System.currentTimeMillis();
		System.out.println(String.format("Exercise 6II — Get the 3 most recent placed order. [execution time: %1$d ms]", (endTimeA - startTimeA)));
		ordersA.forEach(System.out::println);
	}
	
	
	/*
	   Exercise 7 — Get a list of orders which were ordered on 30-Mar-2022, log the order records to the console and then return its product list
	*/
	public static void ex7() {
		long startTime = System.currentTimeMillis();
		List<Product> products = orderRepo.findAll().stream()
				.filter(o -> o.getOrderDate().equals(LocalDate.of(2022, Month.MARCH, 30)))
				.peek(System.out::println)
				.flatMap(o -> o.getProducts().stream())
				.distinct()
				.collect(Collectors.toList());
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("Exercise 7 — Get a list of orders which were ordered on 30-Mar-2022, log the order records to the console and then return its product list. [execution time: %1$d ms]", (endTime - startTime)));
		products.forEach(System.out::println);
		
	}
	
	
	/*
	   Exercise 8 — Calculate total lump sum of all orders placed in Feb 2021
	*/
	public static void ex8() {
		long startTime = System.currentTimeMillis();
		Double totalOrderSum = orderRepo.findAll().stream()
				.filter(o -> o.getOrderDate().isEqual(LocalDate.of(2022, Month.FEBRUARY, 1)) ||
						     o.getOrderDate().isAfter(LocalDate.of(2022, Month.FEBRUARY, 1))
						)
				.filter(o -> o.getOrderDate().isBefore(LocalDate.of(2022, Month.FEBRUARY, 28)) ||
					         o.getOrderDate().isEqual(LocalDate.of(2022, Month.FEBRUARY, 28))
					)
				.flatMap(o -> o.getProducts().stream())
				.mapToDouble(Product::getPrice)
				.sum();
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("Exercise 8 — Calculate total lump sum of all orders placed in Feb 2021. [execution time: %1$d ms]", (endTime - startTime)));
		System.out.println(String.format("SUM - %.2f",totalOrderSum));
	}

	
	/*
	   Exercise 9 — Calculate order average payment placed in Feb 2021
	*/
	public static void ex9() {
		long startTime = System.currentTimeMillis();
		Double avgPayment = orderRepo.findAll().stream()
				.filter(o -> o.getOrderDate().isEqual(LocalDate.of(2022, Month.FEBRUARY, 1)) ||
						     o.getOrderDate().isAfter(LocalDate.of(2022, Month.FEBRUARY, 1))
						)
				.filter(o -> o.getOrderDate().isBefore(LocalDate.of(2022, Month.FEBRUARY, 28)) ||
					         o.getOrderDate().isEqual(LocalDate.of(2022, Month.FEBRUARY, 28))
					)
				.flatMap(o -> o.getProducts().stream())
				.mapToDouble(Product::getPrice)
				.average().getAsDouble();
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("Exercise 9 — Calculate order average payment placed in Feb 2021. [execution time: %1$d ms]", (endTime - startTime)));
		System.out.println(String.format("AVERAGE - %.2f",avgPayment));
	}
	
	
	/*
	   Exercise 10 — Obtain a collection of statistic figures (i.e. sum, average, max, min, count) for all products of category 'Books'
	*/
	public static void ex10() {
		long startTime = System.currentTimeMillis();
        DoubleSummaryStatistics statsPrice = productRepo.findAll().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase("books"))
                .mapToDouble(Product::getPrice)
                .summaryStatistics();
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("Exercise 10 — Obtain a collection of statistic figures (i.e. sum, average, max, min, count) for all products of category 'Books'. [execution time: %1$d ms]", (endTime - startTime)));
        System.out.println(statsPrice);
	}
	
	
	/*
	   Exercise 11 — Obtain a data map with order id and order’s product list
	*/
	public static void ex11() {
		long startTime = System.currentTimeMillis();
		Map<Long, List<Product>> orderMap = orderRepo.findAll().stream()
				               .collect(Collectors.groupingBy(Order::getId, 
				            		    Collectors.flatMapping(o -> o.getProducts().stream(), Collectors.toList())));
		long endTime = System.currentTimeMillis();
        System.out.println(String.format("Exercise 11I — Obtain a data map with order id and order’s product list. [execution time: %1$d ms]", (endTime - startTime)));
        orderMap.forEach((e,v) -> {
        	System.out.println("K -" + e +" " + v);
        });
        
        
        long startTimeA = System.currentTimeMillis();
		Map<Long, List<Product>> orderMapA = orderRepo.findAll().stream()
	                                  .collect(Collectors.toMap(Order::getId, 
	                                		  o -> o.getProducts().stream().collect(Collectors.toList())));
		long endTimeA = System.currentTimeMillis();
        System.out.println(String.format("Exercise 11II — Obtain a data map with order id and order’s product list. [execution time: %1$d ms]", (endTimeA - startTimeA)));
        orderMapA.forEach((e,v) -> {
        	System.out.println("K -" + e +" " + v);
        });     		   
	}
	
	
	/*
	   Exercise 12 — Obtain a data map with order id and order’s product count
	*/
	public static void ex12() {
		long startTime = System.currentTimeMillis();
		Map<Long, Integer> orderWiseProductCount = orderRepo.findAll().stream()
				               .collect(Collectors.toMap(Order::getId, o->o.getProducts().size()));
		
		long endTime = System.currentTimeMillis();
        System.out.println(String.format("Exercise 12 — Obtain a data map with order id and order’s product count. [execution time: %1$d ms]", (endTime - startTime)));
	     orderWiseProductCount.forEach((e,v) -> {
	     	System.out.println("Order ID - " + e +" Product Count - " + v);
	     });
	}
	
	
	/*
	   Exercise 13 — Produce a data map with order records grouped by customer
	*/
	public static void ex13() {
		long startTime = System.currentTimeMillis();
		Map<Customer, List<Order>> custOrderMap = orderRepo.findAll().stream()
				                  .collect(Collectors.groupingBy(Order::getCustomer));
		
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("Exercise 13 — Produce a data map with order records grouped by customer. [execution time: %1$d ms]", (endTime - startTime)));
		custOrderMap.forEach((e,v) -> {
		 System.out.println(e +" " + v);
		});
	}
	
	
	/*
	   Exercise 14 — Produce a data map grouped by customer with number of order placed
	*/
	public static void ex14() {
		long startTime = System.currentTimeMillis();
		Map<Customer, Long> custOrderMap = orderRepo.findAll().stream()
				                 .collect(Collectors.groupingBy(Order::getCustomer, Collectors.counting()));
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("Exercise 14 — Produce a data map grouped by customer with number of order placed. [execution time: %1$d ms]", (endTime - startTime)));
		custOrderMap.forEach((e,v) -> {
		 System.out.println(e.getName() +" Number of order placed - " + v);
		});
	}
	
	
	/*
	   Exercise 15 — Produce a data map with order record and product total sum
	*/
	public static void ex15() {
		long startTime = System.currentTimeMillis();
		Map<Order, Double> custOrderMap = orderRepo.findAll().stream()
				                    .collect(Collectors.toMap(Function.identity(), 
				                     o->o.getProducts().stream()
				                     .collect(Collectors.summingDouble(Product::getPrice))
				                     ));
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("Exercise 15 — Produce a data map with order record and product total sum. [execution time: %1$d ms]", (endTime - startTime)));
		custOrderMap.forEach((e,v) -> {
		 System.out.println("Order ID - " + e.getId() +" Order Sum - " + v);
		});
	}
	
	
	/*
	   Exercise 16 — Obtain a data map with list of product name by category
	*/
	public static void ex16() {
		long startTime = System.currentTimeMillis();
		Map<String, List<String>> custOrderMap = productRepo.findAll().stream().collect(
				                                Collectors.groupingBy(Product::getCategory, 
				                                                      Collectors.mapping(Product::getName, Collectors.toList())
				                                                      ));
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("Exercise 16 — Obtain a data map with list of product name by category. [execution time: %1$d ms]", (endTime - startTime)));
		custOrderMap.forEach((e, v) -> {
			System.out.println("Category : " + e + ", Products : " + v);
		});
	}
	
	

	/*
	   Exercise 17 — Obtain a data map with no of products in each category
	*/
	public static void ex17() {
		long startTime = System.currentTimeMillis();
		Map<String, Long> custOrderMap = productRepo.findAll().stream()
				                    .collect(Collectors.groupingBy(Product::getCategory, Collectors.mapping(Product::getName, Collectors.counting()))
				                    		);
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("Exercise 17 — Obtain a data map with no of products in each category. [execution time: %1$d ms]", (endTime - startTime)));
		custOrderMap.forEach((e, v) -> {
			System.out.println("Category : " + e + ", Products Count : " + v);
		});
	}
	
	
	/*
	   Exercise 18 — Get the most expensive product by category
	*/
	public static void ex18() {
		long startTime = System.currentTimeMillis();
		Map<String, Optional<Product>> custOrderMap = productRepo.findAll().stream()
										.collect(Collectors.groupingBy(Product::getCategory, 
												 Collectors.maxBy((p1,p2)-> p1.getPrice().compareTo(p2.getPrice()))
												 ));
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("Exercise 18 — Get the most expensive product by category. [execution time: %1$d ms]", (endTime - startTime)));
		custOrderMap.forEach((e, v) -> {
			Product p = v.get();
			System.out.println("Category : " + e + ", Product : " + p.getName() + ", Price : " + p.getPrice());
		});
	}
	
	
	
	
	
	
	
	
}
