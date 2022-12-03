
package com.streams.customerorderproduct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomerRepo {
    
    public List<Customer> findAll() {
    	List<Customer> customerList = new ArrayList<>();
        Customer c1 = new Customer(1L, "Mahendra Jadhav", 1);
        Customer c2 = new Customer(2L, "Amol Gabhale", 1);
        Customer c3 = new Customer(3L, "Vishal Jamdade", 2);
        Customer c4 = new Customer(4L, "Mayur Gughe", 0);
        Customer c5 = new Customer(5L, "Rohit Gurav", 2);
        Customer c6 = new Customer(6L, "Amey Kalbate", 1);
        Customer c7 = new Customer(7L, "Manoj Pandey", 0);
        Customer c8 = new Customer(8L, "Amit Mishra", 3);
        Customer c9 = new Customer(9L, "Swapnil Mirjolkar", 0);
        Customer c10 = new Customer(10L, "Nikesh Patil", 1);
        Customer c11 = new Customer(11L, "Pradeep Jaiswal", 2);

        Customer[] customers = {c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11};
        customerList.addAll(Arrays.asList(customers));
        return customerList;
    }
    
    
}

