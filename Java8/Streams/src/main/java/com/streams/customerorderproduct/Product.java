
package com.streams.customerorderproduct;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

	private Long id;

	private String name;

	private String category;
	
	@With
	private Double price;
	
}
