package com.ms.dp.order.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Entity
@Table(name = "ORDERS")
public class OrderEntity {

	@Id
	@GeneratedValue
	private Integer orderId;
	private String userId;
	private Integer productId;
	private Integer quantity;
	private Integer amount;
	private Integer addressId;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	@Enumerated(EnumType.STRING)
	private ProductStatus productStatus;
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;
	@Enumerated(EnumType.STRING)
	private DeliveryStatus deliveryStatus;

}
