package dev.alexfossa204.starbank.microservice.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(exclude = {"timeStamp"})
public class VerificationCodeCheckResponseDto implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Minsk")
    @JsonProperty("timeStamp")
    private Date timeStamp;

    @JsonProperty("httpStatus")
    private HttpStatus httpStatus;

    @JsonProperty("message")
    private String message;

    @JsonProperty("isExpired")
    private boolean isExpired;

    @JsonProperty("isUsed")
    private boolean isUsed;

}
