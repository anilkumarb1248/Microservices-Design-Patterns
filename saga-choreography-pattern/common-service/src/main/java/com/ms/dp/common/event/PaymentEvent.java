package com.ms.dp.common.event;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import com.ms.dp.common.dto.OrderRequest;
import com.ms.dp.common.dto.PaymentRequest;
import com.ms.dp.common.dto.ProductRequest;
import com.ms.dp.common.enums.PaymentStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentEvent implements Event, Serializable {

	private static final long serialVersionUID = -8435499428261978845L;
	
	private UUID eventId = UUID.randomUUID();
	private LocalDate eventDate = LocalDate.now();

	private PaymentRequest paymentRequest;
	private PaymentStatus paymentStatus;

	private OrderRequest orderRequest;
	private ProductRequest productRequest;

	public PaymentEvent(PaymentRequest paymentRequest, PaymentStatus paymentStatus, OrderRequest orderRequest,
			ProductRequest productRequest) {
		this.paymentRequest = paymentRequest;
		this.paymentStatus = paymentStatus;
		this.orderRequest = orderRequest;
		this.productRequest = productRequest;
	}

}
