package dev.alexfossa204.starbank.verificationcodegen.service;

import dev.alexfossa204.starbank.verificationcodegen.service.dto.HttpResponseDto;
import dev.alexfossa204.starbank.verificationcodegen.service.dto.VerificationCodeGenerationTopicMessage;
import dev.alexfossa204.starbank.verificationcodegen.service.dto.VerificationCodeSetAsUsedTopicMessage;
import dev.alexfossa204.starbank.verificationcodegen.service.dto.VerificationCodeCheckRequestDto;
import dev.alexfossa204.starbank.verificationcodegen.service.dto.VerificationCodeCheckResponseDto;
import dev.alexfossa204.starbank.verificationcodegen.service.dto.VerificationCodeGenerationResponseDto;

import java.util.Optional;

public interface VerificationCodeService {

    Optional<VerificationCodeGenerationResponseDto> generateVerificationCode(VerificationCodeGenerationTopicMessage topicMessage);

    Optional<VerificationCodeCheckResponseDto> checkVerificationCode(VerificationCodeCheckRequestDto verificationCodeCheckRequestDto);

    Optional<HttpResponseDto> setAsUsedVerificationCode(VerificationCodeSetAsUsedTopicMessage brokerMessage);

}
