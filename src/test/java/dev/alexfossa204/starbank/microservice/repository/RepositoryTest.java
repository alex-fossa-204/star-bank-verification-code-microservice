package dev.alexfossa204.starbank.microservice.repository;

import dev.alexfossa204.starbank.microservice.repository.model.VerificationCode;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RunWith(SpringRunner.class)
@DataRedisTest
public class RepositoryTest {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Test
    public void testSaveMethod() {
        VerificationCode verificationCode = VerificationCode.builder()
                //.id(UUID.randomUUID())
                .phoneNumber("375331001010")
                .codeValue("010101")
                .isUsed(false)
                .isExpired(false)
                .creationDate(new Date())
                .isClient(true)
                .isUsed(false)
                .build();
        VerificationCode resultCode = verificationCodeRepository.save(verificationCode);
        System.out.println(resultCode);
        Assertions.assertNotNull(resultCode);
    }

    @Test
    public void getAllCodes() {
        List<VerificationCode> codes = StreamSupport.stream(Spliterators.spliteratorUnknownSize(verificationCodeRepository.findAll().iterator(), Spliterator.ORDERED), false)
                .collect(Collectors.toList());
        codes.forEach(System.out::println);
        Assertions.assertNotNull(codes);
    }

    @Test
    public void findVerificationCodeByPhoneNumberTest() {
        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findById("375115881007");
        System.out.println(verificationCodeOptional.get());
    }

    @Test
    public void deleteAllMethodTest() {
        verificationCodeRepository.deleteById("375331001010");
        //verificationCodeRepository.deleteAllByPhoneNumber("375331001010")
//        verificationCodeRepository.deleteAll();
    }

}
