package dev.alexfossa204.starbank.microservice.service;

import dev.alexfossa204.starbank.microservice.service.dto.VerificationCodeGenerationTopicMessage;
import dev.alexfossa204.starbank.microservice.service.dto.VerificationCodeSetAsUsedTopicMessage;

public interface VerificationCodeKafkaListener {

    void listenForVerificationCodeGenerationEvent(VerificationCodeGenerationTopicMessage brokerMessage);

    void listenForVerificationCodeSetAsUsedEvent(VerificationCodeSetAsUsedTopicMessage brokerMessage);

}
