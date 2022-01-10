package com.ms.dp.order.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.ms.dp.common.event.OrderEvent;

@Configuration
public class KafkaConfig {

	private final static String BOOTSTRAP_SERVERS = "localhost:9092";

	// Kafka Producer Configuration -->
	@Bean("order-event-kafka-template")
	public KafkaTemplate<String, OrderEvent> orderEventKafkaTemplate() {
		return new KafkaTemplate<>(orderEventProducerFactory());
	}

	@Bean
	public ProducerFactory<String, OrderEvent> orderEventProducerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		return new DefaultKafkaProducerFactory<>(config, new StringSerializer(), new JsonSerializer<OrderEvent>());
	}
	// Kafka Producer Configuration <--

	// Kafka Consumer Configuration -->
//	private Map<String, Object> buildConsumerConfig(String groupId){
//		Map<String, Object> config = new HashMap<>();
//		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
//		config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
//		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//		return config;
//	}

	@Bean("order-completed-event-kafka-listener")
	public ConcurrentKafkaListenerContainerFactory<String, OrderEvent> orderCompletedEventKafkaListener() {
		ConcurrentKafkaListenerContainerFactory<String, OrderEvent> dataFactory = new ConcurrentKafkaListenerContainerFactory<>();
		dataFactory.setConsumerFactory(orderCompletedEventConsumerFactory());
		return dataFactory;
	}

	@Bean
	public ConsumerFactory<String, OrderEvent> orderCompletedEventConsumerFactory() {

		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "order-completed-event-group");
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

		JsonDeserializer<OrderEvent> deserializer = new JsonDeserializer<OrderEvent>(OrderEvent.class);
		deserializer.addTrustedPackages("*");

		return new DefaultKafkaConsumerFactory<String, OrderEvent>(config, new StringDeserializer(), deserializer);
	}

	@Bean("cancel-order-event-kafka-listener")
	public ConcurrentKafkaListenerContainerFactory<String, OrderEvent> cancelOrderEventKafkaListener() {
		ConcurrentKafkaListenerContainerFactory<String, OrderEvent> dataFactory = new ConcurrentKafkaListenerContainerFactory<>();
		dataFactory.setConsumerFactory(cancelOrderEventConsumerFactory());
		return dataFactory;
	}

	@Bean
	public ConsumerFactory<String, OrderEvent> cancelOrderEventConsumerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "cancel-order-event-group");
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

		JsonDeserializer<OrderEvent> deserializer = new JsonDeserializer<OrderEvent>(OrderEvent.class);
		deserializer.addTrustedPackages("*");

		return new DefaultKafkaConsumerFactory<String, OrderEvent>(config, new StringDeserializer(), deserializer);
	}
	// Kafka Consumer Configuration <--

}
