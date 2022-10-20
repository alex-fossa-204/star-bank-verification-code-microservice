package dev.alexfossa204.starbank.verificationcodegen.config.swagger.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "[Registration 3-d step] [Password recovery 3-d step] Validate entered verification code",
        method = "sendRequestForVerificationCodeGeneration",
        requestBody = @RequestBody(
                description = "__[Login request body]:__\n\n" +
                        "__(phoneNumber)__ - represents user phone number value into the database.\n\n" +
                        "__(verificationCode)__ - represents generated verification code, which is required for password recovery and registration.\n\n"),
        description = "This endpoint is responsible for verification code validation. The one code can be used once. After this endpoint usage, code will be expired, bot not used. Used - means, that code took part in registration, password recovery and etc")
public @interface SwaggerOperationCheckVerificationCode {
}
