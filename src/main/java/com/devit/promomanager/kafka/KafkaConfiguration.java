package com.devit.promomanager.kafka;

import com.devit.promomanager.persistense.document.PromoDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaConfiguration {

	@Autowired
	private KafkaProperties kafkaProperties;
	@Autowired
	private ObjectMapper objectMapper;

	@Bean
	public ProducerFactory<String, PromoDocument> producerFactory() {
		return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties(),
				new StringSerializer(), new JsonSerializer<>(objectMapper));
	}

	@Bean
	public KafkaTemplate<String, PromoDocument> kafkaTemplate(ProducerFactory producerFactory) {
		return new KafkaTemplate<String, PromoDocument>(producerFactory);
	}

}
