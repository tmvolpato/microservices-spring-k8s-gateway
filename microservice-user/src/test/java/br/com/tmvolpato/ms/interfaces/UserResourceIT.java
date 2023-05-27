package br.com.tmvolpato.ms.interfaces;

import br.com.tmvolpato.ms.AbstractPostgreSQLTestContainerIT;
import br.com.tmvolpato.ms.application.form.UserForm;
import br.com.tmvolpato.ms.application.form.UserFormDetail;
import br.com.tmvolpato.ms.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static br.com.tmvolpato.ms.application.form.UserFormHelper.userFormOneValid;
import static br.com.tmvolpato.ms.application.form.UserFormHelper.userFormThreeValid;
import static br.com.tmvolpato.ms.application.form.UserFormHelper.userFormTwoValid;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static reactor.core.publisher.Mono.just;


@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserResourceIT extends AbstractPostgreSQLTestContainerIT {

    private WebTestClient webTestClient;

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    @BeforeAll
    protected void init() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .build();

        this.webTestClient = MockMvcWebTestClient.bindTo(this.mockMvc).build();
    }

    @BeforeEach
    protected void initEach() {
        this.userRepository.deleteAll();
    }

    @Test
    @DisplayName("Create user successfully")
    void givenUserForm_whenRequestDataValid_thenReturnStatusHttpCreated() {

        final var createUserResponse = this.createUser(userFormOneValid());

        final var locationUrlNewUser = requireNonNull(createUserResponse.getResponseHeaders()
                .get(HttpHeaders.LOCATION)).get(0);
        final var userExternalId = this.getUserExternalIdOfLocationUrl(locationUrlNewUser);

        assertThat(userExternalId).isNotBlank();
    }

    @Test
    @DisplayName("Update user successfully")
    void givenUserFormDetail_whenRequestDataValid_thenReturnStatusHttpOk() {

        final var createUserResponse = this.createUser(userFormTwoValid());

        final var locationUrlNewUser = requireNonNull(createUserResponse.getResponseHeaders()
                .get(HttpHeaders.LOCATION)).get(0);
        final var userExternalId = this.getUserExternalIdOfLocationUrl(locationUrlNewUser);

        final var userFormDetail = UserFormDetail.builder()
                .userExternalId(UUID.fromString(userExternalId))
                .name("Jose")
                .lastName("Silva")
                .status(true)
                .build();

        this.webTestClient.put()
                .uri(locationUrlNewUser)
                .body(just(userFormDetail), UserFormDetail.class)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.userExternalId").value(is(userExternalId))
                .jsonPath("$.name").value(is("Jose"))
                .jsonPath("$.lastName").value(is("Silva"))
                .jsonPath("$.email").value(is("jose@email.com"))
                .jsonPath("$.status").value(is(true))
                .jsonPath("$.links[0].rel").value(is("Create user"))
                .jsonPath("$.links[0].href").value(containsString("/protected/users"))
                .jsonPath("$.links[1].rel").value(is("Update user"))
                .jsonPath("$.links[1].href").value(endsWith("/protected/users/".concat(userExternalId)))
                .jsonPath("$.links[2].rel").value(is("Find user"))
                .jsonPath("$.links[2].href").value(endsWith("/protected/users/".concat(userExternalId)))
                .jsonPath("$.links[3].rel").value(is("Delete user"))
                .jsonPath("$.links[3].href").value(endsWith("/protected/users/".concat(userExternalId)));
    }

    @Test
    @DisplayName("Delete user successfully")
    void givenUserExternalId_whenPathVariableValid_thenReturnStatusHttpNoContent() {

        final var createUserResponse = this.createUser(userFormThreeValid());

        final var locationUrlNewUser = requireNonNull(createUserResponse.getResponseHeaders()
                .get(HttpHeaders.LOCATION)).get(0);

        this.webTestClient.delete()
                .uri(locationUrlNewUser)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isNoContent()
                .returnResult(Void.class);
    }

    @Test
    @DisplayName("Search all users with paginated list successfully")
    @SuppressWarnings("CPD-START")
    void givenSearch_whenSearchAllInDatabaseWithPaginated_thenReturnStatusHttpOk() {

        final var createUserOneResponse = this.createUser(userFormOneValid());

        final var createUserTwoResponse = this.createUser(userFormTwoValid());

        final var createUserThreeResponse = this.createUser(userFormThreeValid());

        final var locationUrlNewUserOne = requireNonNull(createUserOneResponse.getResponseHeaders()
                .get(HttpHeaders.LOCATION)).get(0);

        final var userOneExternalId = this.getUserExternalIdOfLocationUrl(locationUrlNewUserOne);

        final var locationUrlNewUserTwo = requireNonNull(createUserTwoResponse.getResponseHeaders()
                .get(HttpHeaders.LOCATION)).get(0);

        final var userTwoExternalId = this.getUserExternalIdOfLocationUrl(locationUrlNewUserTwo);

        final var locationUrlNewUserThree = requireNonNull(createUserThreeResponse.getResponseHeaders()
                .get(HttpHeaders.LOCATION)).get(0);

        final var userThreeExternalId = this.getUserExternalIdOfLocationUrl(locationUrlNewUserThree);

        this.webTestClient.get()
                .uri("/protected/users")
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.links[0].rel").value(is("self"))
                .jsonPath("$.links[0].href").value(endsWith("/protected/users?page=0&size=15&sort=name,asc"))

                .jsonPath("$.content[0].userExternalId").value(is(userTwoExternalId))
                .jsonPath("$.content[0].name").value(is("Jose"))
                .jsonPath("$.content[0].lastName").value(is("Silva"))
                .jsonPath("$.content[0].email").value(is("jose@email.com"))
                .jsonPath("$.content[0].status").value(is(true))
                .jsonPath("$.content[0].links[0].rel").value(is("Create user"))
                .jsonPath("$.content[0].links[0].href").value(endsWith("/protected/users"))
                .jsonPath("$.content[0].links[1].rel").value(is("Update user"))
                .jsonPath("$.content[0].links[1].href").value(endsWith("/protected/users/".concat(userTwoExternalId)))
                .jsonPath("$.content[0].links[2].rel").value(is("Find user"))
                .jsonPath("$.content[0].links[2].href").value(endsWith("/protected/users/".concat(userTwoExternalId)))
                .jsonPath("$.content[0].links[3].rel").value(is("Delete user"))
                .jsonPath("$.content[0].links[3].href").value(endsWith("/protected/users/".concat(userTwoExternalId)))

                .jsonPath("$.content[1].userExternalId").value(is(userThreeExternalId))
                .jsonPath("$.content[1].name").value(is("João"))
                .jsonPath("$.content[1].lastName").value(is("Silva"))
                .jsonPath("$.content[1].email").value(is("joao@email.com"))
                .jsonPath("$.content[1].links[0].rel").value(is("Create user"))
                .jsonPath("$.content[1].links[0].href").value(endsWith("/protected/users"))
                .jsonPath("$.content[1].links[1].rel").value(is("Update user"))
                .jsonPath("$.content[1].links[1].href").value(endsWith("/protected/users/".concat(userThreeExternalId)))
                .jsonPath("$.content[1].links[2].rel").value(is("Find user"))
                .jsonPath("$.content[1].links[2].href").value(endsWith("/protected/users/".concat(userThreeExternalId)))
                .jsonPath("$.content[1].links[3].rel").value(is("Delete user"))
                .jsonPath("$.content[1].links[3].href").value(endsWith("/protected/users/".concat(userThreeExternalId)))

                .jsonPath("$.content[2].userExternalId").value(is(userOneExternalId))
                .jsonPath("$.content[2].name").value(is("Maria"))
                .jsonPath("$.content[2].lastName").value(is("Silva"))
                .jsonPath("$.content[2].email").value(is("test@email.com"))
                .jsonPath("$.content[2].links[0].rel").value(is("Create user"))
                .jsonPath("$.content[2].links[0].href").value(endsWith("/protected/users"))
                .jsonPath("$.content[2].links[1].rel").value(is("Update user"))
                .jsonPath("$.content[2].links[1].href").value(endsWith("/protected/users/".concat(userOneExternalId)))
                .jsonPath("$.content[2].links[2].rel").value(is("Find user"))
                .jsonPath("$.content[2].links[2].href").value(endsWith("/protected/users/".concat(userOneExternalId)))
                .jsonPath("$.content[2].links[3].rel").value(is("Delete user"))
                .jsonPath("$.content[2].links[3].href").value(endsWith("/protected/users/".concat(userOneExternalId)))

                .jsonPath("$.page.size").value(is(15))
                .jsonPath("$.page.totalElements").value(is(3))
                .jsonPath("$.page.totalPages").value(is(1))
                .jsonPath("$.page.number").value(is(0));
    }

    @Test
    @DisplayName("Search users by name with list pagination successfully")
    void givenSearchByName_whenSearchUserByNameSavedInDatabase_thenReturnStatusHttpOk() {

        final var createUserOneResponse = this.createUser(userFormOneValid());

        this.createUser(userFormTwoValid());

        this.createUser(userFormThreeValid());

        final var locationUrlNewUserOne = requireNonNull(createUserOneResponse.getResponseHeaders()
                .get(HttpHeaders.LOCATION)).get(0);

        final var userOneExternalId = this.getUserExternalIdOfLocationUrl(locationUrlNewUserOne);

        this.webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/protected/users")
                        .queryParam("search", "maria").build())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.links[0].rel").value(is("self"))
                .jsonPath("$.links[0].href").value(endsWith("/protected/users?search=maria&page=0&size=15&sort=name,asc"))

                .jsonPath("$.content[0].userExternalId").value(is(userOneExternalId))
                .jsonPath("$.content[0].name").value(is("Maria"))
                .jsonPath("$.content[0].lastName").value(is("Silva"))
                .jsonPath("$.content[0].email").value(is("test@email.com"))
                .jsonPath("$.content[0].status").value(is(true))
                .jsonPath("$.content[0].links[0].rel").value(is("Create user"))
                .jsonPath("$.content[0].links[0].href").value(endsWith("/protected/users"))
                .jsonPath("$.content[0].links[1].rel").value(is("Update user"))
                .jsonPath("$.content[0].links[1].href").value(endsWith("/protected/users/".concat(userOneExternalId)))
                .jsonPath("$.content[0].links[2].rel").value(is("Find user"))
                .jsonPath("$.content[0].links[2].href").value(endsWith("/protected/users/".concat(userOneExternalId)))
                .jsonPath("$.content[0].links[3].rel").value(is("Delete user"))
                .jsonPath("$.content[0].links[3].href").value(endsWith("/protected/users/".concat(userOneExternalId)))

                .jsonPath("$.page.size").value(is(15))
                .jsonPath("$.page.totalElements").value(is(1))
                .jsonPath("$.page.totalPages").value(is(1))
                .jsonPath("$.page.number").value(is(0));
    }

    @Test
    @DisplayName("Search users by email with list pagination successfully")
    void givenSearchByEmail_whenSearchUserByEmailSavedInDatabase_thenReturnStatusHttpOk() {

        this.createUser(userFormOneValid());

        final var createUserTwoResponse = this.createUser(userFormTwoValid());

        this.createUser(userFormThreeValid());

        final var locationUrlNewUserTwo = requireNonNull(createUserTwoResponse.getResponseHeaders()
                .get(HttpHeaders.LOCATION)).get(0);

        final var userTwoExternalId = this.getUserExternalIdOfLocationUrl(locationUrlNewUserTwo);

        this.webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/protected/users")
                        .queryParam("search", "jose@email.com").build())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.links[0].rel").value(is("self"))
                .jsonPath("$.links[0].href").value(endsWith("/protected/users?search=jose@email.com&page=0&size=15&sort=name,asc"))

                .jsonPath("$.content[0].userExternalId").value(is(userTwoExternalId))
                .jsonPath("$.content[0].name").value(is("Jose"))
                .jsonPath("$.content[0].lastName").value(is("Silva"))
                .jsonPath("$.content[0].email").value(is("jose@email.com"))
                .jsonPath("$.content[0].status").value(is(true))
                .jsonPath("$.content[0].links[0].rel").value(is("Create user"))
                .jsonPath("$.content[0].links[0].href").value(endsWith("/protected/users"))
                .jsonPath("$.content[0].links[1].rel").value(is("Update user"))
                .jsonPath("$.content[0].links[1].href").value(endsWith("/protected/users/".concat(userTwoExternalId)))
                .jsonPath("$.content[0].links[2].rel").value(is("Find user"))
                .jsonPath("$.content[0].links[2].href").value(endsWith("/protected/users/".concat(userTwoExternalId)))
                .jsonPath("$.content[0].links[3].rel").value(is("Delete user"))
                .jsonPath("$.content[0].links[3].href").value(endsWith("/protected/users/".concat(userTwoExternalId)))

                .jsonPath("$.page.size").value(is(15))
                .jsonPath("$.page.totalElements").value(is(1))
                .jsonPath("$.page.totalPages").value(is(1))
                .jsonPath("$.page.number").value(is(0));
    }

    @Test
    @DisplayName("Search users by name with list pagination successfully")
    @SuppressWarnings("CPD-END")
    void givenSearchByName_whenSearchUserByNameNotFound_thenReturnStatusHttpOk() {

        this.createUser(userFormOneValid());

        this.createUser(userFormTwoValid());

        this.webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/protected/users")
                        .queryParam("search", "maa").build())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.links[0].rel").value(is("self"))
                .jsonPath("$.links[0].href").value(endsWith("/protected/users?search=maa&page=0&size=15&sort=name,asc"))

                .jsonPath("$.content").value(empty())

                .jsonPath("$.page.size").value(is(15))
                .jsonPath("$.page.totalElements").value(is(0))
                .jsonPath("$.page.totalPages").value(is(0))
                .jsonPath("$.page.number").value(is(0));
    }

    private String getUserExternalIdOfLocationUrl(final String locationUrl) {
        return locationUrl.substring(locationUrl.length() - 36);
    }

    private FluxExchangeResult<Void> createUser(final UserForm userForm) {

        return webTestClient.post()
                .uri("/protected/users")
                .body(just(userForm), UserForm.class)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isCreated()
                .returnResult(Void.class);
    }

}
