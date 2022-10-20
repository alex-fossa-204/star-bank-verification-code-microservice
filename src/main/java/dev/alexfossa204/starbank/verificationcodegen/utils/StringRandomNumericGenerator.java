package dev.alexfossa204.starbank.verificationcodegen.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class StringRandomNumericGenerator {

    private static final int VERIFICATION_CODE_NUMERIC_QUANTITY = 6;


    public static String generateRandomVerificationCode() {
        String code = numericsRandomStringGenerator(VERIFICATION_CODE_NUMERIC_QUANTITY);
        return randomCharactersCombiner(code);
    }

    private static String randomCharactersCombiner(String password) {
        List<Character> characters = password.chars()
                .mapToObj(character -> (char) character)
                .collect(Collectors.toList());
        Collections.shuffle(characters);
        return characters.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }


    private static String numericsRandomStringGenerator(int count) {
        return RandomStringUtils.randomNumeric(count);
    }

}
