package dev.alexfossa204.starbank.verificationcodegen.service;

import dev.alexfossa204.starbank.verificationcodegen.service.dto.VerificationCodeCheckRequestDto;
import dev.alexfossa204.starbank.verificationcodegen.service.dto.VerificationCodeCheckResponseDto;
import dev.alexfossa204.starbank.verificationcodegen.repository.model.VerificationCode;
import dev.alexfossa204.starbank.verificationcodegen.repository.VerificationCodeRepository;
import dev.alexfossa204.starbank.verificationcodegen.service.impl.KafkaProducerService;
import dev.alexfossa204.starbank.verificationcodegen.service.impl.VerificationCodeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Optional;

import static dev.alexfossa204.starbank.verificationcodegen.VerificationCodeTestConstant.*;
import static dev.alexfossa204.starbank.verificationcodegen.service.constant.VerificationCodeServiceConstant.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VerificationCodeServiceTest {

    @Mock
    private VerificationCodeRepository verificationCodeRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private VerificationCodeServiceImpl verificationCodeService;

    private VerificationCodeCheckRequestDto verificationCodeCheckRequestDto;

    private VerificationCodeCheckResponseDto verificationCodeCheckResponseDto;

    private VerificationCodeCheckResponseDto verificationCodeCheckResponseVerificationCodeNotFound;

    private VerificationCodeCheckResponseDto verificationCodeCheckResponseVerificationCodePresentNotExpired;

    private VerificationCodeCheckResponseDto verificationCodeCheckResponseVerificationCodePresentExpiredNotUsed;

    private VerificationCodeCheckResponseDto verificationCodeCheckResponseVerificationCodePresentExpiredUsed;

    private VerificationCode verificationCodeDummyEntityNotExpiredNotUsed;

    private VerificationCode verificationCodeDummyEntityExpiredNotUsed;

    private VerificationCode verificationCodeDummyEntityExpiredUsed;

    @BeforeEach
    void beforeEach() {
        Date dummyDate = new Date();
        verificationCodeCheckRequestDto = VerificationCodeCheckRequestDto.builder()
                .verificationCode(VERIFICATION_CODE_CORRECT_CHARS_DUMMY.getDummy())
                .phoneNumber(PHONE_CORRECT_CHARS_DUMMY.getDummy())
                .build();
//        verificationCodeCheckResponseDto = VerificationCodeCheckResponseDto.builder()
//                .timeStamp(dummyDate)
//                .httpStatus(HttpStatus.OK)
//                .message(STATUS_CODE_OK_VERIFICATION_CODE_VALID)
//                .isExpired(true)
//                .isUsed(false)
//                .build();
        verificationCodeCheckResponseVerificationCodeNotFound = VerificationCodeCheckResponseDto.builder()
                .timeStamp(new Date())
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .message(VERIFICATION_CODE_NOT_FOUND_MSG)
                .isExpired(false)
                .isUsed(false)
                .build();
        verificationCodeCheckResponseVerificationCodePresentNotExpired = VerificationCodeCheckResponseDto.builder()
                .timeStamp(new Date())
                .httpStatus(HttpStatus.OK)
                .message(STATUS_CODE_OK_VERIFICATION_CODE_VALID)
                .isExpired(false)
                .isUsed(false)
                .build();
        verificationCodeCheckResponseVerificationCodePresentExpiredNotUsed = VerificationCodeCheckResponseDto.builder()
                .timeStamp(new Date())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(VERIFICATION_CODE_EXPIRED_BUT_NOT_USED)
                .isExpired(true)
                .isUsed(false)
                .build();
        verificationCodeCheckResponseVerificationCodePresentExpiredUsed = VerificationCodeCheckResponseDto.builder()
                .timeStamp(new Date())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(VERIFICATION_CODE_ALREADY_USED_OR_UNAVAILABLE)
                .isExpired(true)
                .isUsed(true)
                .build();
        
        verificationCodeDummyEntityNotExpiredNotUsed = VerificationCode.builder()
                .codeValue(VERIFICATION_CODE_CORRECT_CHARS_DUMMY.getDummy())
                .creationDate(new Date())
                .isExpired(false)
                .isUsed(false)
                .isClient(true)
                .phoneNumber(PHONE_CORRECT_CHARS_DUMMY.getDummy())
                .build();
        verificationCodeDummyEntityExpiredNotUsed = VerificationCode.builder()
                .codeValue(VERIFICATION_CODE_CORRECT_CHARS_DUMMY.getDummy())
                .creationDate(new Date())
                .isExpired(true)
                .isUsed(false)
                .isClient(true)
                .phoneNumber(PHONE_CORRECT_CHARS_DUMMY.getDummy())
                .build();
        verificationCodeDummyEntityExpiredUsed = VerificationCode.builder()
                .codeValue(VERIFICATION_CODE_CORRECT_CHARS_DUMMY.getDummy())
                .creationDate(new Date())
                .isExpired(true)
                .isUsed(true)
                .isClient(true)
                .phoneNumber(PHONE_CORRECT_CHARS_DUMMY.getDummy())
                .build();


    }

    @Test
    void checkVerificationCodeCodeNotFoundNegativeTest() {
        when(verificationCodeRepository.findVerificationCodeByCodeValueAndPhoneNumber(VERIFICATION_CODE_CORRECT_CHARS_DUMMY.getDummy(), PHONE_CORRECT_CHARS_DUMMY.getDummy()))
                .thenReturn(Optional.empty());
        assertThat(verificationCodeService.checkVerificationCode(verificationCodeCheckRequestDto))
                .isEqualTo(Optional.of(verificationCodeCheckResponseVerificationCodeNotFound));
    }

    @Test
    void checkVerificationCodeCodePresentNotExpiredNotUsedPositiveTest() {
        when(verificationCodeRepository.findVerificationCodeByCodeValueAndPhoneNumber(VERIFICATION_CODE_CORRECT_CHARS_DUMMY.getDummy(), PHONE_CORRECT_CHARS_DUMMY.getDummy()))
                .thenReturn(Optional.of(verificationCodeDummyEntityNotExpiredNotUsed));
        assertThat(verificationCodeService.checkVerificationCode(verificationCodeCheckRequestDto))
                .isEqualTo(Optional.of(verificationCodeCheckResponseVerificationCodePresentNotExpired));
    }

    @Test
    void checkVerificationCodeCodePresentExpiredNotUsedPositiveTest() {
        when(verificationCodeRepository.findVerificationCodeByCodeValueAndPhoneNumber(VERIFICATION_CODE_CORRECT_CHARS_DUMMY.getDummy(), PHONE_CORRECT_CHARS_DUMMY.getDummy()))
                .thenReturn(Optional.of(verificationCodeDummyEntityExpiredNotUsed));
        assertThat(verificationCodeService.checkVerificationCode(verificationCodeCheckRequestDto))
                .isEqualTo(Optional.of(verificationCodeCheckResponseVerificationCodePresentExpiredNotUsed));
    }

    @Test
    void checkVerificationCodeCodePresentExpiredUsedPositiveTest() {
        when(verificationCodeRepository.findVerificationCodeByCodeValueAndPhoneNumber(VERIFICATION_CODE_CORRECT_CHARS_DUMMY.getDummy(), PHONE_CORRECT_CHARS_DUMMY.getDummy()))
                .thenReturn(Optional.of(verificationCodeDummyEntityExpiredUsed));
        assertThat(verificationCodeService.checkVerificationCode(verificationCodeCheckRequestDto))
                .isEqualTo(Optional.of(verificationCodeCheckResponseVerificationCodePresentExpiredUsed));
    }

}
