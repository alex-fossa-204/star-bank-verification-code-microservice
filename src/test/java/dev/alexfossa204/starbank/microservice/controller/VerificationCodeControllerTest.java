package dev.alexfossa204.starbank.microservice.controller;

import dev.alexfossa204.starbank.microservice.controller.impl.VerificationCodeApiController;
import dev.alexfossa204.starbank.microservice.service.dto.VerificationCodeCheckRequestDto;
import dev.alexfossa204.starbank.microservice.service.dto.VerificationCodeCheckResponseDto;
import dev.alexfossa204.starbank.microservice.service.VerificationCodeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Date;
import java.util.Optional;

import static dev.alexfossa204.starbank.microservice.service.constant.VerificationCodeServiceConstant.STATUS_CODE_OK_VERIFICATION_CODE_VALID;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static dev.alexfossa204.starbank.microservice.VerificationCodeTestConstant.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = VerificationCodeApiController.class)
@ExtendWith(MockitoExtension.class)
public class VerificationCodeControllerTest {

    @MockBean
    private VerificationCodeService verificationCodeService;

    @Autowired
    private MockMvc mockMvc;

    private VerificationCodeCheckRequestDto verificationCodeCheckRequestDto;

    private VerificationCodeCheckResponseDto verificationCodeCheckResponseDto;

    @BeforeEach
    void beforeEach() {
        Date dummyDate = new Date();
        verificationCodeCheckRequestDto = VerificationCodeCheckRequestDto.builder()
                .verificationCode(VERIFICATION_CODE_CORRECT_CHARS_DUMMY.getDummy())
                .phoneNumber(PHONE_CORRECT_CHARS_DUMMY.getDummy())
                .build();
        verificationCodeCheckResponseDto = VerificationCodeCheckResponseDto.builder()
                .timeStamp(dummyDate)
                .httpStatus(HttpStatus.OK)
                .message(STATUS_CODE_OK_VERIFICATION_CODE_VALID)
                .isExpired(true)
                .isUsed(false)
                .build();
    }

    @Test
    void checkVerificationCodeRequestPositiveTest() throws Exception {
        when(verificationCodeService.checkVerificationCode(verificationCodeCheckRequestDto))
                .thenReturn(Optional.of(verificationCodeCheckResponseDto));
        mockMvc.perform(post(VERIFY_USER_CODE_URL.getDummy())
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(verificationCodeCheckRequestDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

}
