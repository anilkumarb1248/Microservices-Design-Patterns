package com.ms.dp.order.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ms.dp.common.dto.OrderRequest;
import com.ms.dp.order.dto.OrderDetails;

@RequestMapping("/order")
public interface OrderController {
	
	@PostMapping("/createOrder")
	public OrderDetails createOrder(@RequestBody OrderRequest orderRequest);
	
	@GetMapping("/getOrders/{userId}")
	public List<OrderDetails> getUserOrders(@PathVariable("userId") String userId);
	
	

}
