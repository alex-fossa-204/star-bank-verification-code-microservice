package dev.alexfossa204.starbank.verificationcodegen.config.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> adminProperties = Map.of(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress
        );
        return new KafkaAdmin(adminProperties);
    }

    @Bean
    public NewTopic generateVerificationCodeTopic() {
        return new NewTopic(KafkaConstant.GENERATE_VERIFICATION_CODE_TOPIC, KafkaConstant.DEFAULT_PARTITION_FACTOR, KafkaConstant.DEFAULT_REPLICATION_FACTOR);
    }

}
