package com.ms.dp.product.config;

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
import com.ms.dp.common.event.ProductEvent;

@Configuration
public class KafkaConfig {

	private final static String BOOTSTRAP_SERVERS = "localhost:9092";

	// Kafka Producer Configuration -->
	private Map<String, Object> buildProducerConfig(){
		Map<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return config;
	}
	
	@Bean("product-event-kafka-template")
	public KafkaTemplate<String, ProductEvent> productEventKafkaTemplate() {
		return new KafkaTemplate<>(productEventProducerFactory());
	}

	@Bean
	public ProducerFactory<String, ProductEvent> productEventProducerFactory() {
		return new DefaultKafkaProducerFactory<>(buildProducerConfig(), new StringSerializer(), new JsonSerializer<ProductEvent>());
	}

	@Bean("cancel-order-event-kafka-template")
	public KafkaTemplate<String, OrderEvent> cancelOrderEventKafkaTemplate() {
		return new KafkaTemplate<>(cancelOrderEventProducerFactory());
	}

	@Bean
	public ProducerFactory<String, OrderEvent> cancelOrderEventProducerFactory() {
		return new DefaultKafkaProducerFactory<>(buildProducerConfig(), new StringSerializer(), new JsonSerializer<OrderEvent>());
	}
	
	// Kafka Producer Configuration <--
	
	// Kafka Consumer Configuration -->
	private Map<String, Object> buildConsumerConfig(String groupId){
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		return config;
	}
	
	@Bean("order-event-kafka-listener")
	public ConcurrentKafkaListenerContainerFactory<String, OrderEvent> orderCreatedEventKafkaListener() {
		ConcurrentKafkaListenerContainerFactory<String, OrderEvent> dataFactory = new ConcurrentKafkaListenerContainerFactory<>();
		dataFactory.setConsumerFactory(orderCreatedEventConsumerFactory());
		return dataFactory;
	}

	@Bean
	public ConsumerFactory<String, OrderEvent> orderCreatedEventConsumerFactory() {
		JsonDeserializer<OrderEvent> deserializer = new JsonDeserializer<OrderEvent>(OrderEvent.class);
		deserializer.addTrustedPackages("*");
		return new DefaultKafkaConsumerFactory<String, OrderEvent>(buildConsumerConfig("order-created-event-group"), new StringDeserializer(), deserializer);
	}
	
	@Bean("cancel-product-event-kafka-listener")
	public ConcurrentKafkaListenerContainerFactory<String, ProductEvent> canceledProductEventKafkaListener() {
		ConcurrentKafkaListenerContainerFactory<String, ProductEvent> dataFactory = new ConcurrentKafkaListenerContainerFactory<>();
		dataFactory.setConsumerFactory(canceledProductEventConsumerFactory());
		return dataFactory;
	}

	@Bean
	public ConsumerFactory<String, ProductEvent> canceledProductEventConsumerFactory() {
		JsonDeserializer<ProductEvent> deserializer = new JsonDeserializer<ProductEvent>(ProductEvent.class);
		deserializer.addTrustedPackages("*");
		return new DefaultKafkaConsumerFactory<String, ProductEvent>(buildConsumerConfig("cancel-product-event-group"), new StringDeserializer(), deserializer);
	}
	
	// Kafka Consumer Configuration <--

}
