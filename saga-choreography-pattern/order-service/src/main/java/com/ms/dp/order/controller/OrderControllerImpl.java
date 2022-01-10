package com.ms.dp.order.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.ms.dp.common.dto.OrderRequest;
import com.ms.dp.order.dto.OrderDetails;
import com.ms.dp.order.service.OrderService;

@RestController
public class OrderControllerImpl implements OrderController {

	private OrderService orderService;

	public OrderControllerImpl(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	public OrderDetails createOrder(OrderRequest orderRequest) {
		return orderService.createOrder(orderRequest);
	}

	@Override
	public List<OrderDetails> getUserOrders(String userId) {
		return orderService.getUserOrders(userId);
	}

}
