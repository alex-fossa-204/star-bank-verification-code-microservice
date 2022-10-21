package dev.alexfossa204.starbank.microservice.repository;

import dev.alexfossa204.starbank.microservice.repository.model.VerificationCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationCodeRepository extends CrudRepository<VerificationCode, String> {

    Optional<VerificationCode> findVerificationCodeByCodeValueAndPhoneNumber(String codeValue, String phoneNumber);

    Optional<VerificationCode> findVerificationCodeByPhoneNumber(String phoneNumber);

    void deleteAllByPhoneNumber(String phoneNumber);

}
