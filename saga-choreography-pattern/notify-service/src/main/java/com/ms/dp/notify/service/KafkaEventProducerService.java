package com.ms.dp.notify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ms.dp.common.event.OrderEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaEventProducerService {

	@Autowired
	@Qualifier("order-completed-event-kafka-template")
	KafkaTemplate<String, OrderEvent> orderCompletedEventKafkaTemplate;

	public void createOrderCompletedEvent(OrderEvent orderEvent) {
		log.info("sending the order completed event to Kafka: {}", orderEvent);
		orderCompletedEventKafkaTemplate.send("order-completed-event-topic", orderEvent);
	}

}
