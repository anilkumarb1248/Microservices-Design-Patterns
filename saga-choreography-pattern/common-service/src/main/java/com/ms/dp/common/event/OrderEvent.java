package com.ms.dp.common.event;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import com.ms.dp.common.dto.OrderRequest;
import com.ms.dp.common.enums.OrderStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderEvent implements Event, Serializable {

	private static final long serialVersionUID = -7445803498692639475L;
	
	private UUID eventId = UUID.randomUUID();
	private LocalDate eventDate = LocalDate.now();

	private OrderRequest orderRequest;
	private OrderStatus orderStatus;

	public OrderEvent(OrderRequest orderRequest, OrderStatus orderStatus) {
		this.orderRequest = orderRequest;
		this.orderStatus = orderStatus;
	}

}
