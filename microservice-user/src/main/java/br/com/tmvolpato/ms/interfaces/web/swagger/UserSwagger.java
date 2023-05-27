package br.com.tmvolpato.ms.interfaces.web.swagger;

import br.com.tmvolpato.ms.application.form.UserForm;
import br.com.tmvolpato.ms.application.form.UserFormDetail;
import br.com.tmvolpato.ms.application.dto.UserDto;
import br.com.tmvolpato.ms.infrastructure.exeptionhandler.MessageError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Tag(name = "UserResource", description = "Manages operations on user")
public interface UserSwagger {

    @Operation(summary = "Create new user", description = "Create new user and return the code status http 201")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "saved successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "problem with request data", content = @Content(schema = @Schema(implementation = MessageError.class))),
            @ApiResponse(responseCode = "409", description = "conflict with the data", content = @Content(schema = @Schema(implementation = MessageError.class))),
            @ApiResponse(responseCode = "415", description = "media type not supported", content = @Content(schema = @Schema(implementation = MessageError.class))),
            @ApiResponse(responseCode = "500", description = "internal server error", content = @Content(schema = @Schema(implementation = MessageError.class)))
    })
    ResponseEntity<Void> create(@NotNull @Parameter(description = "User data", required = true, content = @Content(schema = @Schema(implementation = UserForm.class)))
                                @Valid @RequestBody final UserForm userForm);

    @SuppressWarnings("CPD-START")
    @Operation(summary = "Update user", description = "Update user and return the object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "updated successfully", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "problem with request data", content = @Content(schema = @Schema(implementation = MessageError.class))),
            @ApiResponse(responseCode = "409", description = "conflict with the data", content = @Content(schema = @Schema(implementation = MessageError.class))),
            @ApiResponse(responseCode = "404", description = "data not found", content = @Content(schema = @Schema(implementation = MessageError.class))),
            @ApiResponse(responseCode = "415", description = "media type not supported", content = @Content(schema = @Schema(implementation = MessageError.class))),
            @ApiResponse(responseCode = "500", description = "internal server error", content = @Content(schema = @Schema(implementation = MessageError.class)))
    })
    ResponseEntity<UserDto> update(@NotNull @Parameter(description = "User identifier", required = true) @PathVariable(value = "userExternalId") final UUID userExternalId,
                                   @NotNull @Parameter(description = "User data", required = true, content = @Content(schema = @Schema(implementation = UserFormDetail.class)))
                                   @Valid @RequestBody final UserFormDetail userFormDetail);

    @Operation(summary = "Find user", description = "Find user by user external id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "find user successfully", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "problem with request data", content = @Content(schema = @Schema(implementation = MessageError.class))),
            @ApiResponse(responseCode = "404", description = "data not found", content = @Content(schema = @Schema(implementation = MessageError.class))),
            @ApiResponse(responseCode = "415", description = "media type not supported", content = @Content(schema = @Schema(implementation = MessageError.class))),
            @ApiResponse(responseCode = "500", description = "internal server error", content = @Content(schema = @Schema(implementation = MessageError.class)))
    })
    ResponseEntity<UserDto> findUser(@NotNull @Parameter(description = "User identifier", required = true) @PathVariable(value = "userExternalId") final UUID userExternalId);

    @Operation(summary = "Delete user", description = "Delete user by user external id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "deleted successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "problem with request data", content = @Content(schema = @Schema(implementation = MessageError.class))),
            @ApiResponse(responseCode = "409", description = "conflict with the data", content = @Content(schema = @Schema(implementation = MessageError.class))),
            @ApiResponse(responseCode = "404", description = "data not found", content = @Content(schema = @Schema(implementation = MessageError.class))),
            @ApiResponse(responseCode = "415", description = "media type not supported", content = @Content(schema = @Schema(implementation = MessageError.class))),
            @ApiResponse(responseCode = "500", description = "internal server error", content = @Content(schema = @Schema(implementation = MessageError.class)))
    })
    ResponseEntity<Void> delete(@NotNull @Parameter(description = "User identifier", required = true) @PathVariable(value = "userExternalId") final UUID userExternalId);

    @Operation(summary = "Pagination list", description = "Find user with search field")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "search successfully", content = @Content(schema = @Schema(implementation = PagedModel.class))),
            @ApiResponse(responseCode = "400", description = "problem with request data", content = @Content(schema = @Schema(implementation = MessageError.class))),
            @ApiResponse(responseCode = "409", description = "conflict with the data", content = @Content(schema = @Schema(implementation = MessageError.class))),
            @ApiResponse(responseCode = "404", description = "data not found", content = @Content(schema = @Schema(implementation = MessageError.class))),
            @ApiResponse(responseCode = "415", description = "media type not supported", content = @Content(schema = @Schema(implementation = MessageError.class))),
            @ApiResponse(responseCode = "500", description = "internal server error", content = @Content(schema = @Schema(implementation = MessageError.class)))
    })
    @SuppressWarnings("CPD-END")
    ResponseEntity<PagedModel<UserDto>> list(@Parameter(description = "Search by name or lastName or email")
                                             @RequestParam(value = "search", required = false) final String search,
                                             @Parameter(description = "Initial page 0") @RequestParam(defaultValue = "0") final int page,
                                             @Parameter(description = "Total of information in page default 15") @RequestParam(defaultValue = "15") final int size,
                                             @Parameter(description = "Order by default asc") @RequestParam(defaultValue = "asc") final String orderBy,
                                             @Parameter(description = "Sort by default name") @RequestParam(defaultValue = "name") final String sortBy);

}
