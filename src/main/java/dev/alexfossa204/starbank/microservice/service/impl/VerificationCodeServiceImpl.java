package dev.alexfossa204.starbank.microservice.service.impl;

import dev.alexfossa204.starbank.microservice.service.dto.GeneratedVerificationCodeTopicMessage;
import dev.alexfossa204.starbank.microservice.service.dto.UserDto;
import dev.alexfossa204.starbank.microservice.service.dto.VerificationCodeGenerationTopicMessage;
import dev.alexfossa204.starbank.microservice.service.dto.VerificationCodeSetAsUsedTopicMessage;
import dev.alexfossa204.starbank.microservice.service.dto.VerificationCodeCheckRequestDto;
import dev.alexfossa204.starbank.microservice.service.dto.VerificationCodeCheckResponseDto;
import dev.alexfossa204.starbank.microservice.service.dto.VerificationCodeGenerationResponseDto;

import dev.alexfossa204.starbank.microservice.repository.model.VerificationCode;
import dev.alexfossa204.starbank.microservice.repository.VerificationCodeRepository;
import dev.alexfossa204.starbank.microservice.service.VerificationCodeService;

import dev.alexfossa204.starbank.microservice.utils.StringRandomNumericGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import static dev.alexfossa204.starbank.microservice.service.constant.VerificationCodeServiceConstant.*;


@RequiredArgsConstructor
@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;

    private final KafkaProducerService kafkaProducerService;

    @Override
    public Optional<VerificationCodeGenerationResponseDto> generateVerificationCode(VerificationCodeGenerationTopicMessage topicMessage) {
        String verificationCode = StringRandomNumericGenerator.generateRandomVerificationCode();
        UserDto userDto = topicMessage.getUser();
        Optional<VerificationCode> verificationCodeOptional = Optional.of(VerificationCode.builder()
                .phoneNumber(userDto.getPhoneLogin())
                .codeValue(verificationCode)
                .isExpired(false)
                .creationDate(topicMessage.getPublicationTimeStamp())
                .isClient(userDto.getIsClient())
                .isUsed(false)
                .build());
        verificationCodeRepository.save(verificationCodeOptional.get());
        GeneratedVerificationCodeTopicMessage generatedVerificationCodeTopicMessage = GeneratedVerificationCodeTopicMessage.builder()
                .verificationCode(verificationCode)
                .reason(topicMessage.getPublicationReason())
                .user(userDto)
                .build();
        kafkaProducerService.publishVerificationCodeGenerationTopicEvent(generatedVerificationCodeTopicMessage);
        return Optional.of(
                VerificationCodeGenerationResponseDto.builder()
                        .timeStamp(topicMessage.getPublicationTimeStamp())
                        .httpStatus(HttpStatus.OK)
                        .message(STATUS_CODE_OK_VERIFICATION_CODE_SENT_AS_MESSAGE_FOR_CLIENT)
                        .isClient(userDto.getIsClient())
                        .build());
    }

    @Override
    public Optional<VerificationCodeCheckResponseDto> checkVerificationCode(VerificationCodeCheckRequestDto verificationCodeCheckRequestDto) {
        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findById(verificationCodeCheckRequestDto.getPhoneNumber());
        Optional<VerificationCodeCheckResponseDto> verificationCodeCheckResponseOptional = Optional.empty();
        boolean isEmpty = verificationCodeOptional.isEmpty();
        boolean isPresent = verificationCodeOptional.isPresent();
        if (isEmpty) {
            verificationCodeCheckResponseOptional = Optional.of(VerificationCodeCheckResponseDto.builder()
                    .timeStamp(new Date())
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .message(VERIFICATION_CODE_NOT_FOUND_MSG)
                    .isExpired(false)
                    .isUsed(false)
                    .build());
            return verificationCodeCheckResponseOptional;
        }
        boolean isExpired = verificationCodeOptional.get().getIsExpired();
        boolean isUsed = verificationCodeOptional.get().getIsUsed();
        if (isPresent && !isExpired && verificationCodeOptional.get().getCodeValue().equals(verificationCodeCheckRequestDto.getVerificationCode())) {
            verificationCodeCheckResponseOptional = Optional.of(VerificationCodeCheckResponseDto.builder()
                    .timeStamp(new Date())
                    .httpStatus(HttpStatus.OK)
                    .message(STATUS_CODE_OK_VERIFICATION_CODE_VALID)
                    .isExpired(false)
                    .isUsed(false)
                    .build());
            expireVerificationCode(verificationCodeCheckRequestDto.getPhoneNumber());
        }
        if (isPresent && isExpired && verificationCodeOptional.get().getCodeValue().equals(verificationCodeCheckRequestDto.getVerificationCode())) {
            if (!isUsed) {
                verificationCodeCheckResponseOptional = Optional.of(VerificationCodeCheckResponseDto.builder()
                        .timeStamp(new Date())
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message(VERIFICATION_CODE_EXPIRED_BUT_NOT_USED)
                        .isExpired(true)
                        .isUsed(false)
                        .build());
            }
            if (isUsed && verificationCodeOptional.get().getCodeValue().equals(verificationCodeCheckRequestDto.getVerificationCode())) {
                verificationCodeCheckResponseOptional = Optional.of(VerificationCodeCheckResponseDto.builder()
                        .timeStamp(new Date())
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message(VERIFICATION_CODE_ALREADY_USED_OR_UNAVAILABLE)
                        .isExpired(true)
                        .isUsed(true)
                        .build());
            }
        }
        return verificationCodeCheckResponseOptional;
    }

    @Override
    public void setAsUsedVerificationCode(VerificationCodeSetAsUsedTopicMessage brokerMessage) {
        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findById(brokerMessage.getPhoneNumber());
        VerificationCode verificationCode = verificationCodeOptional.get();
        verificationCode.setIsUsed(true);
        verificationCodeRepository.save(verificationCode);

    }

    @Override
    public void expireVerificationCode(String phoneNumber) {
        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findById(phoneNumber);
        if (verificationCodeOptional.isPresent()) {
            VerificationCode code = verificationCodeOptional.get();
            code.setIsExpired(true);
            verificationCodeRepository.save(code);
        }
    }

}
