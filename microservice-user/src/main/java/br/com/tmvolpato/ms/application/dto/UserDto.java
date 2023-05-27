package br.com.tmvolpato.ms.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.UUID;

@Builder
@Getter
@ToString
@Relation(itemRelation = "user", collectionRelation = "users")
@Schema(name = "User DTO", description = "Response information of user")
public class UserDto extends RepresentationModel<UserDto> {

    @Schema(description = "Identifier", example = "de2e6a2c-8ab5-4f5a-abd7-d033b8822dfc")
    private UUID userExternalId;

    @Schema(description = "Name", example = "Maria")
    private String name;

    @Schema(description = "Last name", example = "Silva e Silva")
    private String lastName;

    @Schema(description = "E-mail", example = "maria@email.com")
    private String email;

    @Schema(description = "Status", example = "true or false")
    private boolean status;
}
