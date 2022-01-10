package com.ms.dp.product.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Product")
public class Product {
	
	@Id
	private Integer productId;
	private String productName;
	private Integer price;
	private Integer availabilityCount;

}
