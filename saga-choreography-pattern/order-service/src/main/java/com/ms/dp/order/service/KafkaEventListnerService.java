package com.ms.dp.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ms.dp.common.event.OrderEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaEventListnerService {
	
	@Autowired
	private OrderService orderService;
	
	@KafkaListener(topics = "order-completed-event-topic", groupId = "order-completed-event-group", containerFactory = "order-completed-event-kafka-listener")
	public void orderCompletedEventHandler(OrderEvent orderEvent) {
		log.info("Got order completed event : {}", orderEvent);
		orderService.updateStatus(orderEvent);
	}
	
	@KafkaListener(topics = "cancel-order-event-topic", groupId = "cancel-order-event-group", containerFactory = "cancel-order-event-kafka-listener")
	public void cancelOrderEventHandler(OrderEvent orderEvent) {
		log.info("Got order canceled event : {}", orderEvent);
		orderService.updateStatus(orderEvent);
	}

}
