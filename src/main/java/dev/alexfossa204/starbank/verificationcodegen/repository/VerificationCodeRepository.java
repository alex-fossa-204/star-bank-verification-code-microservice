package dev.alexfossa204.starbank.verificationcodegen.repository;

import dev.alexfossa204.starbank.verificationcodegen.repository.model.VerificationCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationCodeRepository extends CrudRepository<VerificationCode, UUID> {

    Optional<VerificationCode> findVerificationCodeByCodeValueAndPhoneNumber(String codeValue, String phoneNumber);

    void deleteAllByPhoneNumber(String phoneNumber);

}
