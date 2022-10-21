package dev.alexfossa204.starbank.microservice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VerificationCodeTestConstant {

    VERIFY_USER_CODE_URL("/a-banking/verification-management/validation/is-expired"),

    PHONE_CORRECT_CHARS_DUMMY("375331001010"),
    PHONE_INCORRECT_CHARS_DUMMY("375UP83475"),
    PHONE_EMPTY_DUMMY(""),

    VERIFICATION_CODE_CORRECT_CHARS_DUMMY("123456"),
    VERIFICATION_CODE_INCORRECT_CHARS_DUMMY("12345R"),
    VERIFICATION_CODE_EMPTY_DUMMY("");

    private String dummy;

}
