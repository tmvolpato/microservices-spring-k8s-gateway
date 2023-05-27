package br.com.tmvolpato.ms.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.HypermediaMappingInformation;
import org.springframework.http.MediaType;

import static java.util.Arrays.asList;
import static org.springframework.http.MediaType.parseMediaType;

@Configuration
public class HateoasConfig {

    @Bean
    public HypermediaMappingInformation hypermediaMappingInformation() {
        return () -> asList(parseMediaType(MediaType.APPLICATION_JSON_VALUE));
    }
}
