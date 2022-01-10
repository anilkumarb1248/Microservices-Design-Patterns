package com.ms.dp.common.event;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import com.ms.dp.common.dto.NotifyRequest;
import com.ms.dp.common.dto.OrderRequest;
import com.ms.dp.common.dto.PaymentRequest;
import com.ms.dp.common.dto.ProductRequest;
import com.ms.dp.common.enums.DeliveryStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotifyEvent implements Event, Serializable {

	private static final long serialVersionUID = 4663745466382086997L;

	private UUID eventId = UUID.randomUUID();
	private LocalDate eventDate = LocalDate.now();

	private NotifyRequest notifyRequest;
	private DeliveryStatus deliveryStatus;

	private OrderRequest orderRequest;
	private ProductRequest productRequest;
	private PaymentRequest paymentRequest;

	public NotifyEvent(NotifyRequest notifyRequest, DeliveryStatus deliveryStatus, OrderRequest orderRequest) {
		this.notifyRequest = notifyRequest;
		this.deliveryStatus = deliveryStatus;
		this.orderRequest = orderRequest;
	}

	public NotifyEvent(NotifyRequest notifyRequest, DeliveryStatus deliveryStatus, OrderRequest orderRequest,
			ProductRequest productRequest, PaymentRequest paymentRequest) {
		this.notifyRequest = notifyRequest;
		this.deliveryStatus = deliveryStatus;
		this.orderRequest = orderRequest;
		this.productRequest = productRequest;
		this.paymentRequest = paymentRequest;
	}

}
