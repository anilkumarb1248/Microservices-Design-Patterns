package com.ms.dp.shipment.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="USER_ADDRESS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddress {
	
	@Id
	private Integer addressId;
	private String userId;
	private String houseNumber;
	private String street;
	private String city;
	private String state;
	private String zipcode;
	
	public String getAddress() {
		return new StringBuilder()
			.append(houseNumber).append(", ")
			.append(street).append(", ")
			.append(city).append(", ")
			.append(state).append(", ")
			.append(zipcode).toString();
		
	}
	
	

}
