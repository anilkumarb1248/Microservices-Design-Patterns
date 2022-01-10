package com.ms.dp.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ms.dp.common.enums.OrderStatus;
import com.ms.dp.common.event.OrderEvent;
import com.ms.dp.common.event.ProductEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaEventListnerService {

	@Autowired
	private ProductService productService;

	@Autowired
	private KafkaEventProducerService kafkaEventProducerService;

	@KafkaListener(topics = "order-event-topic", groupId = "order-created-event-group", containerFactory = "order-event-kafka-listener")
	public void orderCreatedEventHandler(OrderEvent orderEvent) {
		log.info("Got create order event : {}", orderEvent);

		ProductEvent productEvent = productService.checkProductsAvailability(orderEvent);
		kafkaEventProducerService.publishEvents(productEvent);
	}

	@KafkaListener(topics = "cancel-product-event-topic", groupId = "cancel-product-event-group", containerFactory = "cancel-product-event-kafka-listener")
	public void canceledProductEventHandler(ProductEvent productEvent) {
		log.info("Got cancelled product event : {}", productEvent);

		productService.handleProductCancellation(productEvent);

		OrderEvent orderEvent = new OrderEvent(productEvent.getOrderRequest(), OrderStatus.ORDER_CANCELLED);
		kafkaEventProducerService.createCancelOrderEvent(orderEvent);
	}

}
