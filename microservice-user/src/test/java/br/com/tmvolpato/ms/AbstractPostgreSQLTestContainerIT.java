package br.com.tmvolpato.ms;


import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("integration-test")
@Testcontainers
public abstract class AbstractPostgreSQLTestContainerIT {

    private static final PostgreSQLContainer POSTGRES_SQL_CONTAINER;
    private static final String POSTGRESQL_DOCKER_IMAGE_NAME = "postgres:13-alpine";

    static {
        POSTGRES_SQL_CONTAINER = new PostgreSQLContainer<>(DockerImageName.parse(POSTGRESQL_DOCKER_IMAGE_NAME));
        POSTGRES_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void testProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_SQL_CONTAINER::getPassword);
    }

}
