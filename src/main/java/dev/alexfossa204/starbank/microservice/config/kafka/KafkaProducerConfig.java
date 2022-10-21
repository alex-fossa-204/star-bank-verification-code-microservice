package dev.alexfossa204.starbank.microservice.config.kafka;

import dev.alexfossa204.starbank.microservice.service.dto.GeneratedVerificationCodeTopicMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaProducerConfig {
    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    private Map<String, Object> jsonConfigProps() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return configProps;
    }

    @Bean
    public ProducerFactory<String, GeneratedVerificationCodeTopicMessage> generatedVerificationCodePublishTopicMessageProducerFactory() {
        return new DefaultKafkaProducerFactory<>(jsonConfigProps());
    }

    @Bean
    public KafkaTemplate<String, GeneratedVerificationCodeTopicMessage> generatedVerificationCodeKafkaTemplate() {
        KafkaTemplate<String, GeneratedVerificationCodeTopicMessage> kafkaTemplate = new KafkaTemplate<>(generatedVerificationCodePublishTopicMessageProducerFactory());
        kafkaTemplate.setMessageConverter(new StringJsonMessageConverter());
        return kafkaTemplate;
    }
}
