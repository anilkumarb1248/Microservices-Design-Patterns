package com.ms.dp.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ms.dp.common.enums.OrderStatus;
import com.ms.dp.common.enums.ProductStatus;
import com.ms.dp.common.event.OrderEvent;
import com.ms.dp.common.event.ProductEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaEventProducerService {

	@Autowired
	@Qualifier("product-event-kafka-template")
	KafkaTemplate<String, ProductEvent> productEventKafkaTemplate;

	@Autowired
	@Qualifier("cancel-order-event-kafka-template")
	KafkaTemplate<String, OrderEvent> cancelOrderEventKafkaTemplate;

	public void publishEvents(ProductEvent productEvent) {
		if (ProductStatus.PRODUCTS_AVAILABLE.equals(productEvent.getProductStatus())) {
			createProductEvent(productEvent);
		} else {
			OrderEvent orderEvent = new OrderEvent(productEvent.getOrderRequest(), OrderStatus.ORDER_CANCELLED);
			createCancelOrderEvent(orderEvent);
		}
	}

	public void createProductEvent(ProductEvent productEvent) {
		log.info("sending the product event to Kafka: {}", productEvent);
		productEventKafkaTemplate.send("product-event-topic", productEvent);
	}

	public void createCancelOrderEvent(OrderEvent orderEvent) {
		log.info("sending the cancel order event to Kafka: {}", orderEvent);
		cancelOrderEventKafkaTemplate.send("cancel-order-event-topic", orderEvent);
	}

}
