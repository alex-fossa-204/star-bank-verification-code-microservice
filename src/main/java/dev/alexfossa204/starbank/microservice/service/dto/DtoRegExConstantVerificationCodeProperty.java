package dev.alexfossa204.starbank.microservice.service.dto;

interface DtoRegExConstantVerificationCodeProperty {

    String VERIFICATION_CODE_REGEX_VALIDATION_VALUE = "\\d{6}";

    String VERIFICATION_CODE_REGEX_VALIDATION_MESSAGE = "The verification code must contain 6 numerics in the range 0-9";

}
