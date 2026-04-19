package com.vinculo.api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI vinculoOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Vínculo API")
                .description("Disaster Management & Donation System API")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Vínculo Team")
                )
            );
    }
}