package com.ms.dp.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ms.dp.common.dto.OrderRequest;
import com.ms.dp.common.enums.OrderStatus;
import com.ms.dp.common.event.OrderEvent;
import com.ms.dp.order.dto.OrderDetails;
import com.ms.dp.order.entity.OrderEntity;
import com.ms.dp.order.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

	private OrderRepository orderRepository;
	private KafkaEventProducerService kafkaEventProducerService;

	@Autowired
	public OrderServiceImpl(OrderRepository orderRepository, KafkaEventProducerService kafkaEventProducerService) {
		this.orderRepository = orderRepository;
		this.kafkaEventProducerService = kafkaEventProducerService;
	}

	@Override
	@Transactional
	public OrderDetails createOrder(OrderRequest orderRequest) {

		OrderEntity orderEntity = new OrderEntity();
		BeanUtils.copyProperties(orderRequest, orderEntity);

		orderEntity.setOrderStatus(OrderStatus.ORDER_CREATED);
		OrderEntity savedEntity = orderRepository.save(orderEntity);
		
		orderRequest.setOrderId(savedEntity.getOrderId());
		kafkaEventProducerService.createOrderEvent(orderRequest);

		OrderDetails orderDetails = new OrderDetails();
		BeanUtils.copyProperties(savedEntity, orderDetails);
		return orderDetails;
	}

	@Override
	public List<OrderDetails> getUserOrders(String userId) {
		List<OrderDetails> orders = new ArrayList<>();
		List<OrderEntity> orderEntities = orderRepository.getOrdersByUserId(userId);

		orderEntities.forEach(entity -> {
			OrderDetails order = new OrderDetails();
			BeanUtils.copyProperties(entity, order);
			orders.add(order);
		});
		return orders;
	}

	@Override
	@Transactional
	public void updateStatus(OrderEvent orderEvent) {

		OrderRequest orderRequest = orderEvent.getOrderRequest();
		
		Optional<OrderEntity> optional = orderRepository.findById(orderEvent.getOrderRequest().getOrderId());
		if (optional.isPresent()) {
			
			OrderEntity order = optional.get();
			order.setAmount(orderEvent.getOrderRequest().getAmount());
			
			if(null != orderEvent.getOrderStatus())
				order.setOrderStatus(orderEvent.getOrderStatus());
			
			if(null != orderRequest.getProductStatus())
				order.setProductStatus(orderRequest.getProductStatus());
			
			if(null != orderRequest.getPaymentStatus())
				order.setPaymentStatus(orderRequest.getPaymentStatus());
			
			if(null != orderRequest.getDeliveryStatus())
				order.setDeliveryStatus(orderRequest.getDeliveryStatus());

			orderRepository.save(order);
		}
	}

}
