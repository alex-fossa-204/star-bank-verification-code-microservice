package dev.alexfossa204.starbank.microservice.repository.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Date;

@RedisHash("VerificationCode")
@Data
@Builder
public class VerificationCode {

    @Id
    @Indexed
    private String phoneNumber;

    private String codeValue;

    private Boolean isExpired;

    private Date creationDate;

    private Boolean isClient;

    private Boolean isUsed;

}
