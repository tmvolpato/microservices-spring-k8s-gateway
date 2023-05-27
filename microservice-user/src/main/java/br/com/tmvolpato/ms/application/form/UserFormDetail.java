package br.com.tmvolpato.ms.application.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
@Getter
@ToString
@Schema(name = "User form detail")
public class UserFormDetail {

    @NotNull
    @Schema(description = "User identifier", example = "de2e6a2c-8ab5-4f5a-abd7-d033b8822dfc", required = true)
    private UUID userExternalId;

    @NotBlank
    @Schema(description = "User name", minLength = 3, example = "Maria", required = true)
    private String name;

    @NotBlank
    @Schema(description = "User last name", minLength = 3, example = "Silva e Silva", required = true)
    private String lastName;

    @Schema(description = "User status", example = "true or false", required = true)
    private boolean status;
}
