package com.thales.atc.config;

import com.thales.atc.model.FlightEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    public static final String TOPIC = "atc-flight-events";

    @Value("${KAFKA_BOOTSTRAP_SERVERS:kafka:9092}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, FlightEvent> producerFactory() {

        Map<String, Object> config = new HashMap<>();

        // ✅ FIX: use docker network value, not localhost
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, FlightEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}