package dev.alexfossa204.starbank.microservice.service.dto;

interface DtoRegExConstantPhoneNumberProperty {

    String PHONE_NUMBER_REGEX_VALIDATION_VALUE = "^375\\d{9}";

    String PHONE_NUMBER_REGEX_VALIDATION_MESSAGE = "The phone number must start with 375 country code and contain only 12 digits in the range from 0-9";

}
