package br.com.tmvolpato.ms.application.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@ToString(exclude = {"password"})
@Schema(name = "User form")
public class UserForm {

    @NotBlank
    @Size(min = 3)
    @Schema(description = "User name", minLength = 3, example = "Maria", required = true)
    private String name;

    @NotBlank
    @Size(min = 3)
    @Schema(description = "User last name", minLength = 3, example = "Silva e Silva", required = true)
    private String lastName;

    @Email
    @NotBlank
    @Schema(description = "User e-mail", example = "maria@email.com", required = true)
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    @Schema(description = "User password", minLength = 8, maxLength = 20, required = true)
    private String password;

    @Schema(description = "User status", example = "true or false", required = true)
    private boolean status;
}
