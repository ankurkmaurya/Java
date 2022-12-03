
package com.streams.customerorderproduct;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class OrderRepo {
  
    public List<Order> findAll() {
    	List<Order> orderList = new ArrayList<>();
        CustomerRepo custRepo = new CustomerRepo();
        ProductRepo prodRepo = new ProductRepo();
        
        List<Customer> cl = custRepo.findAll();
        List<Product> pl = prodRepo.findAll();

        Set<Product> prod1 = Set.of(pl.get(0), pl.get(1), pl.get(2), pl.get(8), pl.get(5), pl.get(6));
        Set<Product> prod2 = Set.of(pl.get(9), pl.get(12), pl.get(10), pl.get(2), pl.get(8), pl.get(13));
        Set<Product> prod3 = Set.of(pl.get(3), pl.get(4), pl.get(5), pl.get(6), pl.get(14));
        Set<Product> prod4 = Set.of(pl.get(14), pl.get(12), pl.get(7), pl.get(6));
        Set<Product> prod5 = Set.of(pl.get(1), pl.get(3), pl.get(6), pl.get(5), pl.get(11), pl.get(10));
        Set<Product> prod6 = Set.of(pl.get(0), pl.get(1), pl.get(2), pl.get(3), pl.get(9), pl.get(14));
        
        Order o1 = new Order(1L, LocalDate.of(2022, Month.APRIL, 05), LocalDate.of(2022, Month.MARCH, 8), "NEW", cl.get(2), prod1);
        Order o2 = new Order(2L, LocalDate.of(2022, Month.FEBRUARY, 28), LocalDate.of(2022, Month.MARCH, 5), "NEW", cl.get(2), prod2);
        Order o3 = new Order(3L, LocalDate.of(2022, Month.FEBRUARY, 10), LocalDate.of(2022, Month.FEBRUARY, 18), "DELIVERED", cl.get(6), prod3);
        Order o4 = new Order(4L, LocalDate.of(2022, Month.MARCH, 30), LocalDate.of(2022, Month.APRIL, 7), "DELIVERED", cl.get(9), prod4);
        Order o5 = new Order(5L, LocalDate.of(2022, Month.OCTOBER, 4), LocalDate.of(2022, Month.NOVEMBER, 12), "NEW", cl.get(1), prod5);
        Order o6 = new Order(6L, LocalDate.of(2022, Month.APRIL, 01), LocalDate.of(2022, Month.APRIL, 5), "PENDING", cl.get(10), prod6);
        
        Order[] orders = {o1, o2, o3, o4, o5, o6};
        orderList.addAll(Arrays.asList(orders));
        return orderList;
    }
    
}
