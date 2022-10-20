package dev.alexfossa204.starbank.verificationcodegen.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class GeneratedVerificationCodeTopicMessage implements Serializable {

    @JsonProperty("verificationCode")
    private String verificationCode;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("client")
    private UserDto user;

}
