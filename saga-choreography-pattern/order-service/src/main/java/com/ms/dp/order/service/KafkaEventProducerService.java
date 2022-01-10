package com.ms.dp.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ms.dp.common.dto.OrderRequest;
import com.ms.dp.common.enums.OrderStatus;
import com.ms.dp.common.event.OrderEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaEventProducerService {
	
	@Autowired
	@Qualifier("order-event-kafka-template")
	KafkaTemplate<String, OrderEvent> orderEventKafkaTemplate;
	
	public void createOrderEvent(OrderRequest orderRequest) {
		OrderEvent orderEvent = new OrderEvent(orderRequest, OrderStatus.ORDER_CREATED);
		
		log.info("sending the order event to Kafka: {}", orderEvent);
		
		orderEventKafkaTemplate.send("order-event-topic", orderEvent);
	}

}
