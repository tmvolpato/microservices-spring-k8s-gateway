package br.com.tmvolpato.ms.interfaces.web;

import br.com.tmvolpato.ms.application.hateoas.UserRepresentationAssemble;
import br.com.tmvolpato.ms.application.form.UserForm;
import br.com.tmvolpato.ms.application.form.UserFormDetail;
import br.com.tmvolpato.ms.application.dto.UserDto;
import br.com.tmvolpato.ms.application.service.UserApplicationService;
import br.com.tmvolpato.ms.interfaces.web.swagger.UserSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static br.com.tmvolpato.ms.interfaces.web.constants.MappingConstant.PROTECTED_USERS;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@Validated
@RestController
@RequestMapping(value = PROTECTED_USERS, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserResource implements UserSwagger {

    private final UserApplicationService userApplicationService;

    private final UserRepresentationAssemble userRepresentationAssemble;
    private final PagedResourcesAssembler<UserDto> pagedResourcesAssembler;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@NotNull @Valid @RequestBody final UserForm userForm) {
        final var userDto = this.userApplicationService.createUser(userForm);
        final var uriComponents = fromPath(PROTECTED_USERS.concat("/{userExternalId}"))
                .buildAndExpand(userDto.getUserExternalId());

        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @PutMapping(value = "/{userExternalId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> update(@NotNull @PathVariable(value = "userExternalId") final UUID userExternalId,
                                          @NotNull @Valid @RequestBody final UserFormDetail userFormDetail) {

        final var userDto = this.userApplicationService.updateUser(userExternalId, userFormDetail);
        this.userRepresentationAssemble.toModel(userDto);

        return ResponseEntity.ok(userDto);
    }

    @GetMapping(value = "/{userExternalId}")
    public ResponseEntity<UserDto> findUser(@NotNull @PathVariable(value = "userExternalId") final UUID userExternalId) {
        final var userDto = this.userApplicationService.findUserByUserExternalId(userExternalId);
        this.userRepresentationAssemble.toModel(userDto);

        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping(value = "/{userExternalId}")
    public ResponseEntity<Void> delete(@NotNull @PathVariable(value = "userExternalId") final UUID userExternalId) {
        this.userApplicationService.deleteUser(userExternalId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PagedModel<UserDto>> list(@RequestParam(value = "search", required = false) final String search,
                                                    @RequestParam(defaultValue = "0") final int page,
                                                    @RequestParam(defaultValue = "15") final int size,
                                                    @RequestParam(defaultValue = "asc") final String orderBy,
                                                    @RequestParam(defaultValue = "name") final String sortBy) {

        final var pageUserDto = this.userApplicationService.list(search, page, size, orderBy, sortBy);
        final var pagedModelUserDto = this.pagedResourcesAssembler
                .toModel(pageUserDto, this.userRepresentationAssemble);

        return ResponseEntity.ok(pagedModelUserDto);
    }

}
