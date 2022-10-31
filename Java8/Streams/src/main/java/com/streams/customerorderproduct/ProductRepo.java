
package com.streams.customerorderproduct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProductRepo {
    
    public List<Product> findAll() { 
    	List<Product> productList = new ArrayList<>();
        Product p1 = new Product(1L, "Counter Strike", "Games", 184.83);
        Product p2 = new Product(2L, "Walking Monkey", "Toys", 12.66);
        Product p3 = new Product(3L, "Potato", "Grocery", 498.02);
        Product p4 = new Product(4L, "Clapping Monkey", "Toys", 536.80);
        Product p5 = new Product(5L, "Contra", "Games", 458.20);
        Product p6 = new Product(6L, "Flying Robot", "Toys", 146.52);
        Product p7 = new Product(7L, "Harry Potter", "Books", 656.42);
        Product p8 = new Product(8L, "Barbie 204", "Baby", 41.46);
        Product p9 = new Product(9L, "Homo Sapiens", "Books", 697.57);
        Product p10 = new Product(10L, "Barbie 100", "Baby", 366.90);
        Product p11 = new Product(11L, "Lantern", "Toys", 95.50);
        Product p12 = new Product(12L, "Pumpkin", "Grocery", 302.19);
        Product p13 = new Product(13L, "Monster Truck", "Toys", 295.37);
        Product p14 = new Product(14L, "Onion", "Grocery", 534.64);
        Product p15 = new Product(15L, "Barbie 500", "Baby", 623.58);

        Product[] products = {p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15};
        productList.addAll(Arrays.asList(products));
        return productList;
    } 
    
}


