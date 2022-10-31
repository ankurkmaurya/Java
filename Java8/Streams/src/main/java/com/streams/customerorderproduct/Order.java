
package com.streams.customerorderproduct;



import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

	private Long id;
	private LocalDate orderDate;
	private LocalDate deliveryDate;
	private String status;
	private Customer customer;
        
	@EqualsAndHashCode.Exclude
	Set<Product> products;
}

