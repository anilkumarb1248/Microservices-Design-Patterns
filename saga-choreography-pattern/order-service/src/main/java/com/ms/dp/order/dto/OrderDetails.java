package com.ms.dp.order.dto;

import com.ms.dp.common.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetails {
	
	private Integer orderId;
	private String userId;
	private Integer productId;
	private Integer quantity;
	private Integer amount;
	private Integer addressId;
	private OrderStatus orderStatus;

}
