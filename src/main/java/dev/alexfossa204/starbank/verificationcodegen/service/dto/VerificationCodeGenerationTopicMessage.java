package dev.alexfossa204.starbank.verificationcodegen.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class VerificationCodeGenerationTopicMessage implements Serializable {

    @JsonProperty("reason")
    private String publicationReason;

    @JsonProperty("messagePublicationDate")
    private Date publicationTimeStamp;

    @JsonProperty("user")
    private UserDto user;

}
