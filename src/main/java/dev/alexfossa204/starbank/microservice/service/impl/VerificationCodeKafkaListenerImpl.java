package dev.alexfossa204.starbank.microservice.service.impl;

import dev.alexfossa204.starbank.microservice.service.dto.VerificationCodeGenerationTopicMessage;
import dev.alexfossa204.starbank.microservice.service.dto.VerificationCodeSetAsUsedTopicMessage;
import dev.alexfossa204.starbank.microservice.service.VerificationCodeKafkaListener;
import dev.alexfossa204.starbank.microservice.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static dev.alexfossa204.starbank.microservice.config.kafka.KafkaConstant.*;
import static dev.alexfossa204.starbank.microservice.config.kafka.KafkaVerificationCodeGenerationConsumerConfig.*;
import static dev.alexfossa204.starbank.microservice.config.kafka.KafkaVerificationCodeSetAsUsedConsumerConfig.*;

@Component
@RequiredArgsConstructor
public class VerificationCodeKafkaListenerImpl implements VerificationCodeKafkaListener {

    private final VerificationCodeService verificationCodeService;

    @KafkaListener(id = "verificationCodeGenerationEventListener", topics = GENERATE_VERIFICATION_CODE_TOPIC, groupId = CODE_GENERATION_CONSUMER_GROUP_ID, containerFactory = "verificationCodeGenerationTopicMessageKafkaListenerContainerFactory")
    @Override
    public void listenForVerificationCodeGenerationEvent(VerificationCodeGenerationTopicMessage brokerMessage) {
        verificationCodeService.generateVerificationCode(brokerMessage);
    }


    @KafkaListener(id = "verificationCodeSetAsUsedEventListener", topics = SET_VERIFICATION_CODE_AS_USED_TOPIC, groupId = CODE_SET_AS_USED_CONSUMER_GROUP_ID, containerFactory = "verificationCodeSetAsUsedTopicMessageKafkaListenerContainerFactory")
    @Override
    public void listenForVerificationCodeSetAsUsedEvent(VerificationCodeSetAsUsedTopicMessage brokerMessage) {
        verificationCodeService.setAsUsedVerificationCode(brokerMessage);
    }

}
