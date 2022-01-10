package com.ms.dp.notify.config;

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

import com.ms.dp.common.event.NotifyEvent;
import com.ms.dp.common.event.OrderEvent;

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
	
	@Bean("order-completed-event-kafka-template")
	public KafkaTemplate<String, OrderEvent> createOrderCompletedKafkaTemplate() {
		return new KafkaTemplate<>(createOrderCompletedEventProducerFactory());
	}

	@Bean
	public ProducerFactory<String, OrderEvent> createOrderCompletedEventProducerFactory() {
		return new DefaultKafkaProducerFactory<>(buildProducerConfig(), new StringSerializer(), new JsonSerializer<OrderEvent>());
	}
	// Kafka Producer Configuration <--

	// Kafka Consumer Configuration -->
	@Bean("notify-event-kafka-listener")
	public ConcurrentKafkaListenerContainerFactory<String, NotifyEvent> notifyEventKafkaListener() {
		ConcurrentKafkaListenerContainerFactory<String, NotifyEvent> dataFactory = new ConcurrentKafkaListenerContainerFactory<>();
		dataFactory.setConsumerFactory(notifyEventConsumerFactory());
		return dataFactory;
	}

	@Bean
	public ConsumerFactory<String, NotifyEvent> notifyEventConsumerFactory() {

		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "notify-event-group");
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

		JsonDeserializer<NotifyEvent> deserializer = new JsonDeserializer<NotifyEvent>(NotifyEvent.class);
		deserializer.addTrustedPackages("*");
		return new DefaultKafkaConsumerFactory<String, NotifyEvent>(config, new StringDeserializer(), deserializer);
	}
	// Kafka Consumer Configuration <--

}
