package com.ms.dp.common.dto;

import com.ms.dp.common.enums.DeliveryStatus;
import com.ms.dp.common.enums.OrderStatus;
import com.ms.dp.common.enums.PaymentStatus;
import com.ms.dp.common.enums.ProductStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
	
	private Integer orderId;
	private String userId;
	private Integer productId;
	private Integer quantity;
	private Integer amount;
	private Integer addressId;
	
	private OrderStatus orderStatus;
	private ProductStatus productStatus;
	private PaymentStatus paymentStatus;
	private DeliveryStatus deliveryStatus;

}
