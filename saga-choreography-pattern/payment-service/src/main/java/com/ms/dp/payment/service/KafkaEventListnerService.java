package com.ms.dp.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ms.dp.common.enums.PaymentStatus;
import com.ms.dp.common.enums.ProductStatus;
import com.ms.dp.common.event.PaymentEvent;
import com.ms.dp.common.event.ProductEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaEventListnerService {

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private KafkaEventProducerService kafkaEventProducerService;

	@KafkaListener(topics = "product-event-topic", groupId = "products-available-event-group", containerFactory = "product-event-kafka-listener")
	public void productsAvailableEventHandler(ProductEvent productEvent) {
		log.info("Got products available event : {}", productEvent);

		kafkaEventProducerService.publishEvents(paymentService.makePayment(productEvent));
	}

	@KafkaListener(topics = "cancel-payment-event-topic", groupId = "cancel-payment-event-group", containerFactory = "cancel-payment-event-kafka-listener")
	public void canceledPaymentEventHandler(PaymentEvent paymentEvent) {
		log.info("Got cancelled payment event : {}", paymentEvent);

		paymentService.handlePaymentCancellation(paymentEvent);

		paymentEvent.getOrderRequest().setPaymentStatus(PaymentStatus.PAYMENT_ROLLBACKED);
		ProductEvent productEvent = new ProductEvent(paymentEvent.getProductRequest(), ProductStatus.PRODUCT_ROLLBACKED, paymentEvent.getOrderRequest());
		kafkaEventProducerService.createCancelProductEvent(productEvent);
	}

}
