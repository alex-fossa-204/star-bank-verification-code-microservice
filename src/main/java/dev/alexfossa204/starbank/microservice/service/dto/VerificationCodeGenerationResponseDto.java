package dev.alexfossa204.starbank.microservice.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@Builder
@EqualsAndHashCode(exclude = {"timeStamp"})
public class VerificationCodeGenerationResponseDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Minsk")
    @JsonProperty("timeStamp")
    private Date timeStamp;

    @JsonProperty("httpStatus")
    private HttpStatus httpStatus;

    @JsonProperty("message")
    private String message;

    @JsonProperty("isClient")
    private boolean isClient;

}
