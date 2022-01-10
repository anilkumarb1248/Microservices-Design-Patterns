package com.ms.dp.order.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicsConfig {
	
	// Create the Kafka topics manually
	
	@Bean
	public KafkaAdmin admin() {
	    Map<String, Object> configs = new HashMap<>();
	    configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
	    return new KafkaAdmin(configs);
	}
	
	@Bean
	public NewTopic orderEventTopic() {
		return TopicBuilder
				.name("order-event-topic")
				.partitions(2)
				.replicas(1)
				.compact()
				.build();
	}
	
	@Bean
	public NewTopic productEventTopic() {
		return TopicBuilder
				.name("product-event-topic")
				.partitions(2)
				.replicas(1)
				.compact()
				.build();
	}
	
	@Bean
	public NewTopic paymentEventTopic() {
		return TopicBuilder
				.name("payment-event-topic")
				.partitions(2)
				.replicas(1)
				.compact()
				.build();
	}

	@Bean
	public NewTopic notifyEventTopic() {
		return TopicBuilder
				.name("notify-event-topic")
				.partitions(2)
				.replicas(1)
				.compact()
				.build();
	}
	
	@Bean
	public NewTopic orderCompletedEventTopic() {
		return TopicBuilder
				.name("order-completed-event-topic")
				.partitions(2)
				.replicas(1)
				.compact()
				.build();
	}
	
	@Bean
	public NewTopic cancelOrderEventTopic() {
		return TopicBuilder
				.name("cancel-order-event-topic")
				.partitions(2)
				.replicas(1)
				.compact()
				.build();
	}
	
	@Bean
	public NewTopic cancelProductEventTopic() {
		return TopicBuilder
				.name("cancel-product-event-topic")
				.partitions(2)
				.replicas(1)
				.compact()
				.build();
	}
	
	@Bean
	public NewTopic cancelPaymentEventTopic() {
		return TopicBuilder
				.name("cancel-payment-event-topic")
				.partitions(2)
				.replicas(1)
				.compact()
				.build();
	}

}
