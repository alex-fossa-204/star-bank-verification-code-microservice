package dev.alexfossa204.starbank.microservice.config.swagger;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("star-bank-public")
                .pathsToMatch("/star-bank/**")
                .packagesToScan("dev.alexfossa204.starbank.microservice.controller")
                .build();
    }

    @Bean
    public OpenAPI affinityBankOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Star Bank Verification Code Microservice")
                        .description("This Microservice is responsible for user 6-digit code generation, during password recovery and registration")
                        .version("v0.0.1"))
                .externalDocs(new ExternalDocumentation()
                        .description("A-Bank Wiki Documentation")
                        .url("https://wiki.andersenlab.com/display/VMN/Onboarding"));
    }
}