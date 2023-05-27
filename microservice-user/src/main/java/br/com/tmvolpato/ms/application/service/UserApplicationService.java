package br.com.tmvolpato.ms.application.service;

import br.com.tmvolpato.ms.application.converter.in.UserFormDetailToUserEntityConverter;
import br.com.tmvolpato.ms.application.converter.in.UserFormToUserEntityConverter;
import br.com.tmvolpato.ms.application.converter.out.UserEntityToUserDtoConverter;
import br.com.tmvolpato.ms.application.form.UserForm;
import br.com.tmvolpato.ms.application.form.UserFormDetail;
import br.com.tmvolpato.ms.application.dto.UserDto;
import br.com.tmvolpato.ms.domain.service.UserService;
import br.com.tmvolpato.ms.infrastructure.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static br.com.tmvolpato.ms.infrastructure.constants.UserMessageConstant.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private final UserFormToUserEntityConverter userFormToUserEntityConverter;
    private final UserFormDetailToUserEntityConverter userFormDetailToUserEntityConverter;
    private final UserEntityToUserDtoConverter userEntityToUserDtoConverter;

    public UserDto createUser(final UserForm userForm) {
        var userEntity = this.userFormToUserEntityConverter.convert(userForm);

        userEntity.setPassword(this.passwordEncoder.encode(userForm.getPassword()));
        final var userEntitySaved = this.userService.save(userEntity);

        return this.userEntityToUserDtoConverter.convert(userEntitySaved);
    }

    public UserDto updateUser(final UUID userExternalId, final UserFormDetail userFormDetail) {
        if (!userFormDetail.getUserExternalId().equals(userExternalId)) throw new NotFoundException(USER_NOT_FOUND);

        var userEntityFound = this.userService.findUserByUserExternalId(userExternalId);
        final var userEntityChanged = this.userFormDetailToUserEntityConverter.convert(userFormDetail);

        userEntityFound.allowedChanges(userEntityChanged.getName(), userEntityChanged.getLastName(), userEntityChanged.isStatus());
        final var userEntityUpdated = this.userService.update(userEntityFound);

        return this.userEntityToUserDtoConverter.convert(userEntityUpdated);
    }

    public void deleteUser(final UUID userExternalId) {
        this.userService.delete(userExternalId);
    }

    public UserDto findUserByUserExternalId(final UUID userExternalId) {
        final var userEntity = this.userService.findUserByUserExternalId(userExternalId);
        return this.userEntityToUserDtoConverter.convert(userEntity);
    }

    public Page<UserDto> list(final String search, final int page, final int size, final String orderBy, final String sortBy) {
        final var pageUserEntities = this.userService.list(search, page, size, orderBy, sortBy);
        return pageUserEntities.map(this.userEntityToUserDtoConverter::convert);
    }
}
