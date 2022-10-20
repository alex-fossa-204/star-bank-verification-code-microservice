package dev.alexfossa204.starbank.verificationcodegen.service;

import dev.alexfossa204.starbank.verificationcodegen.service.dto.VerificationCodeGenerationTopicMessage;
import dev.alexfossa204.starbank.verificationcodegen.service.dto.VerificationCodeSetAsUsedTopicMessage;

public interface VerificationCodeKafkaListener {

    void listenForVerificationCodeGenerationEvent(VerificationCodeGenerationTopicMessage brokerMessage);

    void listenForVerificationCodeSetAsUsedEvent(VerificationCodeSetAsUsedTopicMessage brokerMessage);

}
