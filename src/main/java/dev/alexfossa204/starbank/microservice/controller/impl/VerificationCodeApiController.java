package dev.alexfossa204.starbank.microservice.controller.impl;

import dev.alexfossa204.starbank.microservice.controller.VerificationCodeApi;
import dev.alexfossa204.starbank.microservice.service.dto.VerificationCodeCheckRequestDto;
import dev.alexfossa204.starbank.microservice.service.dto.VerificationCodeCheckResponseDto;
import dev.alexfossa204.starbank.microservice.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class VerificationCodeApiController implements VerificationCodeApi {

    private final VerificationCodeService verificationCodeService;

    @Override
    public ResponseEntity<VerificationCodeCheckResponseDto> checkVerificationCode(VerificationCodeCheckRequestDto verificationCodeCheckRequestDto) {
        Optional<VerificationCodeCheckResponseDto> verificationCodeCheckResponseDto = verificationCodeService.checkVerificationCode(verificationCodeCheckRequestDto);
        return new ResponseEntity<>(verificationCodeCheckResponseDto.get(), verificationCodeCheckResponseDto.get().getHttpStatus());
    }
}
