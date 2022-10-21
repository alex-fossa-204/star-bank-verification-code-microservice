package dev.alexfossa204.starbank.microservice.service.impl;

import dev.alexfossa204.starbank.microservice.service.dto.GeneratedVerificationCodeTopicMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static dev.alexfossa204.starbank.microservice.config.kafka.KafkaConstant.PUBLISH_GENERATED_VERIFICATION_CODE;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, GeneratedVerificationCodeTopicMessage> generatedVerificationCodeKafkaTemplate;

    @Async
    public void publishVerificationCodeGenerationTopicEvent(GeneratedVerificationCodeTopicMessage topicMessage) {
        generatedVerificationCodeKafkaTemplate.send(PUBLISH_GENERATED_VERIFICATION_CODE, topicMessage);
    }

}
