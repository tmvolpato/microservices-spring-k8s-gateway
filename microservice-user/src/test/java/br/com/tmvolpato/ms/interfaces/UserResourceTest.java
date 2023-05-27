package br.com.tmvolpato.ms.interfaces;

import br.com.tmvolpato.ms.AbstractUnitTest;
import br.com.tmvolpato.ms.application.form.UserForm;
import br.com.tmvolpato.ms.application.form.UserFormDetail;
import br.com.tmvolpato.ms.application.hateoas.UserRepresentationAssemble;
import br.com.tmvolpato.ms.application.service.UserApplicationService;
import br.com.tmvolpato.ms.infrastructure.configuration.HateoasConfig;
import br.com.tmvolpato.ms.interfaces.web.UserResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static br.com.tmvolpato.ms.application.dto.UserDtoHelper.userDtoValid;
import static br.com.tmvolpato.ms.application.form.UserFormDetailHelper.userFormDetailOneValid;
import static br.com.tmvolpato.ms.application.form.UserFormHelper.userFormOneInvalid;
import static br.com.tmvolpato.ms.application.form.UserFormHelper.userFormOneValid;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserResource.class)
@Import({UserRepresentationAssemble.class, HateoasConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserResourceTest extends AbstractUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserApplicationService userApplicationService;

    private ObjectMapper mapper;

    @BeforeAll
    protected void init() {
        mapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Create user successfully")
    void givenUserForm_whenRequestDataValid_thenReturnStatusHttpCreated() throws Exception {

        when(this.userApplicationService.createUser(any(UserForm.class))).thenReturn(userDtoValid());

        this.mockMvc.perform(post("/protected/users")
                        .accept(APPLICATION_JSON_VALUE)
                        .content(this.mapper.writeValueAsString(userFormOneValid()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/protected/users/2c34f712-9286-4826-a4f6-5914bb74159f"));
    }

    @Test
    @DisplayName("Update user successfully")
    @SuppressWarnings("CPD-START")
    void givenUserFormDetail_whenRequestDataValid_thenReturnStatusHttpOk() throws Exception {

        when(this.userApplicationService.updateUser(any(UUID.class), any(UserFormDetail.class)))
                .thenReturn(userDtoValid());

        this.mockMvc.perform(put("/protected/users/2c34f712-9286-4826-a4f6-5914bb74159f")
                        .accept(APPLICATION_JSON_VALUE)
                        .content(this.mapper.writeValueAsString(userFormDetailOneValid()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userExternalId", is("2c34f712-9286-4826-a4f6-5914bb74159f")))
                .andExpect(jsonPath("$.name", is("Maria")))
                .andExpect(jsonPath("$.lastName", is("Silva")))
                .andExpect(jsonPath("$.email", is("maria@email.com")))
                .andExpect(jsonPath("$.status", is(true)))
                .andExpect(jsonPath("$.links[0].rel", is("Create user")))
                .andExpect(jsonPath("$.links[0].href", endsWith("/protected/users")))
                .andExpect(jsonPath("$.links[1].rel", is("Update user")))
                .andExpect(jsonPath("$.links[1].href", endsWith("/protected/users/2c34f712-9286-4826-a4f6-5914bb74159f")))
                .andExpect(jsonPath("$.links[2].rel", is("Find user")))
                .andExpect(jsonPath("$.links[2].href", endsWith("/protected/users/2c34f712-9286-4826-a4f6-5914bb74159f")))
                .andExpect(jsonPath("$.links[3].rel", is("Delete user")))
                .andExpect(jsonPath("$.links[3].href", endsWith("/protected/users/2c34f712-9286-4826-a4f6-5914bb74159f")));
    }

    @Test
    @DisplayName("Delete user successfully")
    void givenUserExternalId_whenPathVariableValid_thenReturnStatusHttpNoContent() throws Exception {

        doNothing().when(this.userApplicationService).deleteUser(any(UUID.class));

        this.mockMvc.perform(delete("/protected/users/2c34f712-9286-4826-a4f6-5914bb74159f")
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Find user successfully")
    void givenUserExternalId_whenPathVariableValid_thenReturnStatusHttpOk() throws Exception {

        when(this.userApplicationService.findUserByUserExternalId(any(UUID.class)))
                .thenReturn(userDtoValid());

        this.mockMvc.perform(get("/protected/users/2c34f712-9286-4826-a4f6-5914bb74159f")
                        .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userExternalId", is("2c34f712-9286-4826-a4f6-5914bb74159f")))
                .andExpect(jsonPath("$.name", is("Maria")))
                .andExpect(jsonPath("$.lastName", is("Silva")))
                .andExpect(jsonPath("$.email", is("maria@email.com")))
                .andExpect(jsonPath("$.status", is(true)))
                .andExpect(jsonPath("$.links[0].rel", is("Create user")))
                .andExpect(jsonPath("$.links[0].href", endsWith("/protected/users")))
                .andExpect(jsonPath("$.links[1].rel", is("Update user")))
                .andExpect(jsonPath("$.links[1].href", endsWith("/protected/users/2c34f712-9286-4826-a4f6-5914bb74159f")))
                .andExpect(jsonPath("$.links[2].rel", is("Find user")))
                .andExpect(jsonPath("$.links[2].href", endsWith("/protected/users/2c34f712-9286-4826-a4f6-5914bb74159f")))
                .andExpect(jsonPath("$.links[3].rel", is("Delete user")))
                .andExpect(jsonPath("$.links[3].href", endsWith("/protected/users/2c34f712-9286-4826-a4f6-5914bb74159f")));
    }

    @Test
    @DisplayName("Create user with content type empty return error")
    @SuppressWarnings("CPD-END")
    void givenCustomerForm_whenContentTypeIsEmpty_thenReturnUnsupportedMediaType() throws Exception {

        when(this.userApplicationService.createUser(any(UserForm.class))).thenReturn(userDtoValid());

        this.mockMvc.perform(post("/protected/users")
                        .accept(APPLICATION_JSON_VALUE)
                        .content(this.mapper.writeValueAsString(userFormOneValid()))
                        .contentType(""))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.dateTime", notNullValue()))
                .andExpect(jsonPath("$.httpStatus", is("UNSUPPORTED_MEDIA_TYPE")))
                .andExpect(jsonPath("$.message", is("null media type is not supported. Supported media types are application/json")))
                .andExpect(jsonPath("$.debugMessage", is("Content type '' not supported")))
                .andExpect(jsonPath("$.errors", empty()));
    }

    @Test
    @DisplayName("Create user without mandatory field return error")
    void givenUserForm_whenRequestDataInvalid_thenReturnStatusHttpBadRequest() throws Exception {

        when(this.userApplicationService.createUser(any(UserForm.class))).thenReturn(userDtoValid());

        this.mockMvc.perform(post("/protected/users")
                        .accept(APPLICATION_JSON_VALUE)
                        .content(this.mapper.writeValueAsString(userFormOneInvalid()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dateTime", notNullValue()))
                .andExpect(jsonPath("$.httpStatus", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", is("Validation failed for argument(s)")))
                .andExpect(jsonPath("$.debugMessage", is("No message")))
                .andExpect(jsonPath("$.errors.[0].object", is("userForm")))
                .andExpect(jsonPath("$.errors.[0].field", is("name")))
                .andExpect(jsonPath("$.errors.[0].rejectValue", is("empty")))
                .andExpect(jsonPath("$.errors.[0].message", is("must not be blank")));
    }

    @Test
    @DisplayName("Internal problem when tried to persist new user return error")
    void givenUserForm_whenRequestDataValid_thenReturnStatusHttpInternalServerError() throws Exception {

        doThrow(new RuntimeException()).when(this.userApplicationService).createUser(any());

        this.mockMvc.perform(post("/protected/users")
                        .accept(APPLICATION_JSON_VALUE)
                        .content(this.mapper.writeValueAsString(userFormOneValid()))
                        .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.dateTime", notNullValue()))
                .andExpect(jsonPath("$.httpStatus", is("INTERNAL_SERVER_ERROR")))
                .andExpect(jsonPath("$.message", is("No message")))
                .andExpect(jsonPath("$.debugMessage", is("No message")))
                .andExpect(jsonPath("$.errors", empty()));
    }
}
