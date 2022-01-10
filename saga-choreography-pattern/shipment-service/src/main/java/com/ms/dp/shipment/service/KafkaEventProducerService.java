package com.ms.dp.shipment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ms.dp.common.enums.PaymentStatus;
import com.ms.dp.common.event.NotifyEvent;
import com.ms.dp.common.event.PaymentEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaEventProducerService {

	@Autowired
	@Qualifier("notify-event-kafka-template")
	KafkaTemplate<String, NotifyEvent> notifyEventKafkaTemplate;

	@Autowired
	@Qualifier("cancel-payment-event-kafka-template")
	KafkaTemplate<String, PaymentEvent> cancelPaymentEventKafkaTemplate;

	public void publishEvents(NotifyEvent notifyEvent) {

		switch (notifyEvent.getDeliveryStatus()) {

		case DELIVERY_INITIATED:
			createNotifyEvent(notifyEvent);
			break;
		case DELIVERY_COMPLETED:
			createNotifyEvent(notifyEvent);
			break;
		case DELIVERY_FAILIED:
			PaymentEvent paymentEvent = new PaymentEvent(notifyEvent.getPaymentRequest(),
					PaymentStatus.PAYMENT_ROLLBACKED, notifyEvent.getOrderRequest(), notifyEvent.getProductRequest());
			createCancelPaymentEvent(paymentEvent);
			break;
		}
	}

	public void createNotifyEvent(NotifyEvent notifyEvent) {
		log.info("sending the notify event to Kafka: {}", notifyEvent);
		notifyEventKafkaTemplate.send("notify-event-topic", notifyEvent);
	}

	public void createCancelPaymentEvent(PaymentEvent paymentEvent) {
		log.info("sending the cancel payment event to Kafka: {}", paymentEvent);
		cancelPaymentEventKafkaTemplate.send("cancel-payment-event-topic", paymentEvent);
	}

}
