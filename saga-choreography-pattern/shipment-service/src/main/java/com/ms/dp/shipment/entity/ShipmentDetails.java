package com.ms.dp.shipment.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ms.dp.common.enums.DeliveryStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="SHIPMENT_DETAILS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentDetails {
	
	@Id
	private Integer orderId;
	private String userId;
	private Integer productId;
	private String productName;
	private Integer quantity;
	private Integer amount;
	private String address;
	
	@Enumerated(EnumType.STRING)
	private DeliveryStatus deliveryStatus;
	

}
