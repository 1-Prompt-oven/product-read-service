package org.example.productreadservice.global.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.productreadservice.category.event.CategoryCreatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableKafka
@Slf4j
public class KafkaConsumerConfig {

	@Bean
	public ConsumerFactory<String, CategoryCreatedEvent> consumerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
			"localhost:9092,localhost:9093,localhost:9094");
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "product-read-service");

		// Error Handling Deserializer 설정
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

		// Delegate Deserializer 설정
		configProps.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
		configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

		// JSON Deserializer 설정
		configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
			"org.example.productreadservice.category.event.CategoryCreatedEvent");
		configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

		return new DefaultKafkaConsumerFactory<>(configProps);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, CategoryCreatedEvent> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, CategoryCreatedEvent> factory =
			new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setMissingTopicsFatal(false);
		factory.setCommonErrorHandler(errorHandler());

		return factory;
	}

	@Bean
	public CommonErrorHandler errorHandler() {
		// FixedBackOff로 재시도 정책 설정 (1초 간격으로 2번 재시도)
		return new DefaultErrorHandler(
			(consumerRecord, e) -> {
				log.error("Error in consumer: ", e);
				log.error("Failed record: {}", consumerRecord);
			},
			new FixedBackOff(1000L, 2)
		);
	}
}