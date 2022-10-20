package dev.alexfossa204.starbank.verificationcodegen.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCodeCheckRequestDto implements DtoRegExConstantPhoneNumberProperty, DtoRegExConstantVerificationCodeProperty {

    @Pattern(regexp = VERIFICATION_CODE_REGEX_VALIDATION_VALUE, message = VERIFICATION_CODE_REGEX_VALIDATION_MESSAGE)
    @JsonProperty("verificationCode")
    private String verificationCode;

    @Pattern(regexp = PHONE_NUMBER_REGEX_VALIDATION_VALUE, message = PHONE_NUMBER_REGEX_VALIDATION_MESSAGE)
    @JsonProperty("phoneNumber")
    private String phoneNumber;

}