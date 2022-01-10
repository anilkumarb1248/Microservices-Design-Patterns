package com.ms.dp.notify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ms.dp.common.event.NotifyEvent;
import com.ms.dp.common.event.OrderEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaEventListnerService {

	@Autowired
	private KafkaEventProducerService kafkaEventProducerService;

	@KafkaListener(topics = "notify-event-topic", groupId = "notify-event-group", containerFactory = "notify-event-kafka-listener")
	public void notificationEventHandler(NotifyEvent notifyEvent) {
		log.info("Got notification event : {}", notifyEvent);

		// Sent mail notification to the User
		kafkaEventProducerService.createOrderCompletedEvent(new OrderEvent(notifyEvent.getOrderRequest(),notifyEvent.getOrderRequest().getOrderStatus()));
	}

}
