package com.ogooueTechnology.referentiel.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI referentielApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Référentiel")
                        .description("Documentation de l’API pour la gestion du référentiel")
                        .version("1.0.0"));
        ///swagger-ui/**", "/v3/api-docs"
    }
}
