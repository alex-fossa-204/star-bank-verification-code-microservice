package dev.alexfossa204.starbank.microservice.service;

import dev.alexfossa204.starbank.microservice.service.dto.VerificationCodeGenerationTopicMessage;
import dev.alexfossa204.starbank.microservice.service.dto.VerificationCodeSetAsUsedTopicMessage;
import dev.alexfossa204.starbank.microservice.service.dto.VerificationCodeCheckRequestDto;
import dev.alexfossa204.starbank.microservice.service.dto.VerificationCodeCheckResponseDto;
import dev.alexfossa204.starbank.microservice.service.dto.VerificationCodeGenerationResponseDto;

import java.util.Optional;

public interface VerificationCodeService {

    Optional<VerificationCodeGenerationResponseDto> generateVerificationCode(VerificationCodeGenerationTopicMessage topicMessage);

    Optional<VerificationCodeCheckResponseDto> checkVerificationCode(VerificationCodeCheckRequestDto verificationCodeCheckRequestDto);

    void setAsUsedVerificationCode(VerificationCodeSetAsUsedTopicMessage brokerMessage);

    void expireVerificationCode(String phoneNumber);

}
