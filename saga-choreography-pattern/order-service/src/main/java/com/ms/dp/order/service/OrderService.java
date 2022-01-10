package com.ms.dp.order.service;

import java.util.List;

import com.ms.dp.common.dto.OrderRequest;
import com.ms.dp.common.event.OrderEvent;
import com.ms.dp.order.dto.OrderDetails;

public interface OrderService {
	
	public OrderDetails createOrder(OrderRequest orderRequest);

	public List<OrderDetails> getUserOrders(String userId);
	
	public void updateStatus(OrderEvent orderEvent);

}
