package dev.alexfossa204.starbank.verificationcodegen.repository.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;
import java.util.UUID;

@RedisHash("VerificationCode")
@Data
@Builder
public class VerificationCode {

    @Id
    private UUID id;

    private String phoneNumber;

    private String codeValue;

    private Boolean isExpired;

    private Date creationDate;

    private Boolean isClient;

    private Boolean isUsed;

}
