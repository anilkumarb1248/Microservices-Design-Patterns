package com.ms.dp.shipment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ms.dp.common.event.PaymentEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaEventListnerService {

	@Autowired
	private ShipmentService shipmentService;

	@Autowired
	private KafkaEventProducerService kafkaEventProducerService;

	@KafkaListener(topics = "payment-event-topic", groupId = "payment-completed-event-group", containerFactory = "payment-event-kafka-listener")
	public void paymentCompletedEventHandler(PaymentEvent paymentEvent) {
		log.info("Got payement completed event : {}", paymentEvent);

		kafkaEventProducerService.publishEvents(shipmentService.shipProduct(paymentEvent));
	}

}
