package dev.alexfossa204.starbank.microservice.service.exception;

public class VerificationCodeNotFoundException extends ServiceException {

    public VerificationCodeNotFoundException(String message) {
        super(message);
    }

    public VerificationCodeNotFoundException(Throwable cause) {
        super(cause);
    }
}
