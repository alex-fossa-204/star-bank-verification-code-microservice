package dev.alexfossa204.starbank.verificationcodegen.service.impl;

import dev.alexfossa204.starbank.verificationcodegen.service.dto.HttpResponseDto;
import dev.alexfossa204.starbank.verificationcodegen.service.dto.GeneratedVerificationCodeTopicMessage;
import dev.alexfossa204.starbank.verificationcodegen.service.dto.UserDto;
import dev.alexfossa204.starbank.verificationcodegen.service.dto.VerificationCodeGenerationTopicMessage;
import dev.alexfossa204.starbank.verificationcodegen.service.dto.VerificationCodeSetAsUsedTopicMessage;
import dev.alexfossa204.starbank.verificationcodegen.service.dto.VerificationCodeCheckRequestDto;
import dev.alexfossa204.starbank.verificationcodegen.service.dto.VerificationCodeCheckResponseDto;
import dev.alexfossa204.starbank.verificationcodegen.service.dto.VerificationCodeGenerationResponseDto;

import dev.alexfossa204.starbank.verificationcodegen.repository.model.VerificationCode;
import dev.alexfossa204.starbank.verificationcodegen.repository.VerificationCodeRepository;
import dev.alexfossa204.starbank.verificationcodegen.service.VerificationCodeService;

import dev.alexfossa204.starbank.verificationcodegen.utils.StringRandomNumericGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static dev.alexfossa204.starbank.verificationcodegen.service.constant.VerificationCodeServiceConstant.*;


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
                .id(UUID.randomUUID())
                .phoneNumber(userDto.getPhoneLogin())
                .codeValue(verificationCode)
                .isExpired(false)
                .creationDate(topicMessage.getPublicationTimeStamp())
                .isClient(userDto.getIsClient())
                .isUsed(false)
                .build());
        verificationCodeRepository.deleteAllByPhoneNumber(userDto.getPhoneLogin());
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
        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findVerificationCodeByCodeValueAndPhoneNumber(verificationCodeCheckRequestDto.getVerificationCode(), verificationCodeCheckRequestDto.getPhoneNumber());
        Optional<VerificationCodeCheckResponseDto> verificationCodeCheckResponseOptional = Optional.empty();
        boolean isEmpty = verificationCodeOptional.isEmpty();
        boolean isPresent = verificationCodeOptional.isPresent();
        if(isEmpty) {
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
        if (isPresent && !isExpired) {
            verificationCodeCheckResponseOptional = Optional.of(VerificationCodeCheckResponseDto.builder()
                    .timeStamp(new Date())
                    .httpStatus(HttpStatus.OK)
                    .message(STATUS_CODE_OK_VERIFICATION_CODE_VALID)
                    .isExpired(false)
                    .isUsed(false)
                    .build());
            expireVerificationCode(verificationCodeCheckRequestDto.getVerificationCode(), verificationCodeCheckRequestDto.getPhoneNumber());
        }
        if (isPresent && isExpired) {
            if (!isUsed) {
                verificationCodeCheckResponseOptional = Optional.of(VerificationCodeCheckResponseDto.builder()
                        .timeStamp(new Date())
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message(VERIFICATION_CODE_EXPIRED_BUT_NOT_USED)
                        .isExpired(true)
                        .isUsed(false)
                        .build());
            }
            if (isUsed) {
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
    public Optional<HttpResponseDto> setAsUsedVerificationCode(VerificationCodeSetAsUsedTopicMessage brokerMessage) {

        //IMPLEMENT !!!

        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findVerificationCodeByCodeValueAndPhoneNumber(brokerMessage.getVerificationCode(), brokerMessage.getPhoneNumber());
        Optional<HttpResponseDto> responseDto = Optional.empty();
        if (verificationCodeOptional.isPresent()) {
            VerificationCode verificationCode = verificationCodeOptional.get();
            verificationCode.setIsUsed(true);
            verificationCodeRepository.save(verificationCode);

            responseDto = Optional.of(HttpResponseDto.builder()

                    .build());
        }
        if(verificationCodeOptional.isEmpty()) {

            responseDto = Optional.of(HttpResponseDto.builder()

                    .build());
        }
        return Optional.empty();
    }

    private void expireVerificationCode(String codeValue, String phoneNumber) {
        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findVerificationCodeByCodeValueAndPhoneNumber(codeValue, phoneNumber);
        if(verificationCodeOptional.isPresent()) {
            VerificationCode code = verificationCodeOptional.get();
            code.setIsExpired(true);
            verificationCodeRepository.save(code);
        }
    }

}
