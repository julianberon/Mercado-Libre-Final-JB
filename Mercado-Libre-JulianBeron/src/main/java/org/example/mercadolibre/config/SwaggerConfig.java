package org.example.mercadolibre.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Configuración de Swagger/OpenAPI para documentación automática de la API
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mutant Detection API")
                        .version("1.0")
                        .description("API REST para detectar mutantes mediante análisis de secuencias de ADN. " +
                                "Detecta si un humano es mutante basándose en su secuencia de ADN.")
                        .contact(new Contact()
                                .name("MercadoLibre Challenge")
                                .email("challenge@mercadolibre.com")));
    }
}

