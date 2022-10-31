
package com.streams.data.repository;

import java.util.List;
import java.util.stream.Collectors;

import com.streams.customerorderproduct.Customer;
import com.streams.customerorderproduct.CustomerRepo;
import com.streams.customerorderproduct.Order;
import com.streams.customerorderproduct.OrderRepo;
import com.streams.customerorderproduct.Product;
import com.streams.customerorderproduct.ProductRepo;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;



@DisplayName("Testing CustomerRepo class functionality.")
public class RepoTest {

    private final CustomerRepo customerRepo = new CustomerRepo(); 
    private final ProductRepo productRepo = new ProductRepo(); 
    private final OrderRepo orderRepo = new OrderRepo(); 
    
    
    @Test
    @DisplayName("Test to get all the customer list from repository")
    public void findAllCustomerTest() {
        long startTime = System.currentTimeMillis();
        List<Customer> customerList = customerRepo.findAll();
        long endTime = System.currentTimeMillis();

        System.out.println(String.format("exercise 1 - execution time: %1$d ms", (endTime - startTime)));
        assertTrue(!customerList.isEmpty(), "Customer List should not be empty.");
    }
    
    @Test
    @DisplayName("Test to get all the product list from repository")
    public void findAllProductTest() {
        long startTime = System.currentTimeMillis();
        List<Product> productList = productRepo.findAll();
        long endTime = System.currentTimeMillis();

        System.out.println(String.format("exercise 1 - execution time: %1$d ms", (endTime - startTime)));
        assertTrue(!productList.isEmpty(), "Product List should not be empty.");
    }
    
    @Test
    @DisplayName("Test to get all the order list from repository")
    public void findAllOrderTest() {
        long startTime = System.currentTimeMillis();
        List<Order> orderList = orderRepo.findAll();
        long endTime = System.currentTimeMillis();

        System.out.println(String.format("exercise 1 - execution time: %1$d ms", (endTime - startTime)));
        assertTrue(!orderList.isEmpty(), "Order List should not be empty.");
    }
    

    @Test
    @DisplayName("Exercise 1 — Obtain a list of products belongs to category “Books” with price > 100")
    public void ex1() {
        long startTime = System.currentTimeMillis();
        List<Product> products = productRepo.findAll().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase("books"))
                .filter(p -> p.getPrice()>100)
                .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("Exercise 1 - execution time: %1$d ms", (endTime - startTime)));
        assertTrue(!products.isEmpty(), "Order List should not be empty.");
    }
    
    

}
