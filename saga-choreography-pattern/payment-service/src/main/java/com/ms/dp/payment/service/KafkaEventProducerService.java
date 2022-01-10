package com.ms.dp.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ms.dp.common.enums.PaymentStatus;
import com.ms.dp.common.enums.ProductStatus;
import com.ms.dp.common.event.PaymentEvent;
import com.ms.dp.common.event.ProductEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaEventProducerService {

	@Autowired
	@Qualifier("payment-event-kafka-template")
	KafkaTemplate<String, PaymentEvent> paymentEventKafkaTemplate;

	@Autowired
	@Qualifier("cancel-product-event-kafka-template")
	KafkaTemplate<String, ProductEvent> cancelProductEventKafkaTemplate;

	public void publishEvents(PaymentEvent paymentEvent) {
		if (PaymentStatus.PAYMENT_COMPLETED.equals(paymentEvent.getPaymentStatus())) {
			createPaymentEvent(paymentEvent);
		} else {
			paymentEvent.getOrderRequest().setPaymentStatus(paymentEvent.getPaymentStatus());
			ProductEvent productEvent = new ProductEvent(paymentEvent.getProductRequest(),
					ProductStatus.PRODUCT_ROLLBACKED, paymentEvent.getOrderRequest());
			createCancelProductEvent(productEvent);
		}
	}

	public void createPaymentEvent(PaymentEvent paymentEvent) {
		log.info("sending the payment event to Kafka: {}", paymentEvent);
		paymentEventKafkaTemplate.send("payment-event-topic", paymentEvent);
	}

	public void createCancelProductEvent(ProductEvent productEvent) {
		log.info("sending the cancel product event to Kafka: {}", productEvent);
		cancelProductEventKafkaTemplate.send("cancel-product-event-topic", productEvent);
	}

}
