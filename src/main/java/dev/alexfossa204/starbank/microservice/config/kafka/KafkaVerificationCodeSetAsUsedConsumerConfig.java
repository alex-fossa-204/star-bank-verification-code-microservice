package dev.alexfossa204.starbank.microservice.config.kafka;

import dev.alexfossa204.starbank.microservice.service.dto.VerificationCodeSetAsUsedTopicMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaVerificationCodeSetAsUsedConsumerConfig {

    public static final String CODE_SET_AS_USED_CONSUMER_GROUP_ID = "verificationCodeSetAsUsedConsumerGroup";

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Bean
    public ConsumerFactory<String, VerificationCodeSetAsUsedTopicMessage> verificationCodeSetAsUsedTopicMessageConsumerFactory() {

        JsonDeserializer<VerificationCodeSetAsUsedTopicMessage> deserializer = new JsonDeserializer<>(VerificationCodeSetAsUsedTopicMessage.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        ErrorHandlingDeserializer<VerificationCodeSetAsUsedTopicMessage> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(deserializer);
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, CODE_SET_AS_USED_CONSUMER_GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), errorHandlingDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, VerificationCodeSetAsUsedTopicMessage> verificationCodeSetAsUsedTopicMessageKafkaListenerContainerFactory(ConsumerFactory<String, VerificationCodeSetAsUsedTopicMessage> verificationCodeExpirationTopicMessageConsumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, VerificationCodeSetAsUsedTopicMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(verificationCodeExpirationTopicMessageConsumerFactory);
        return factory;
    }

}
