package com.ms.dp.common.dto;

import com.ms.dp.common.enums.DeliveryStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotifyRequest {
	
	private Integer orderId;
	private String userId;
	private Integer productId;
	private String productName;
	private Integer quantity;
	private Integer amount;
	private String address;
	private DeliveryStatus deliveryStatus;
	

}
