package dev.alexfossa204.starbank.verificationcodegen.controller;

import dev.alexfossa204.starbank.verificationcodegen.config.swagger.annotation.SwaggerOperationCheckVerificationCode;
import dev.alexfossa204.starbank.verificationcodegen.service.dto.VerificationCodeCheckRequestDto;
import dev.alexfossa204.starbank.verificationcodegen.service.dto.VerificationCodeCheckResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Tag(name = "Verification Code management API", description = "This Component is responsible for Verification code checks and validation.")
@RequestMapping(value = {"/star-bank/verification-management", "/"})
public interface VerificationCodeApi {

    @SwaggerOperationCheckVerificationCode
    @RequestMapping(value = "/validation/is-expired", method = RequestMethod.POST)
    ResponseEntity<VerificationCodeCheckResponseDto> checkVerificationCode(@RequestBody @Valid VerificationCodeCheckRequestDto verificationCodeCheckRequestDto);

}
