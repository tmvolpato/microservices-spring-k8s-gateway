package br.com.tmvolpato.ms.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final var contact = new Contact()
                .email("email@email.com")
                .name("Thiago M. Volpato")
                .url("https://github.com/tmvolpato");

        final var localServer = new Server()
                .url("http://localhost:8001")
                .description("Server URL in Local development");

        final var mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        final var info = new Info()
                .title("User Manager API")
                .contact(contact)
                .version("1.0")
                .description("This API exposes endpoints to manage users.")
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}
