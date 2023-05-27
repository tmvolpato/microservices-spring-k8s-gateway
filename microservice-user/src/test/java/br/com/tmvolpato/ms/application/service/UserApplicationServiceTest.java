package br.com.tmvolpato.ms.application.service;


import br.com.tmvolpato.ms.AbstractUnitTest;
import br.com.tmvolpato.ms.application.converter.in.UserFormDetailToUserEntityConverter;
import br.com.tmvolpato.ms.application.converter.in.UserFormToUserEntityConverter;
import br.com.tmvolpato.ms.application.converter.out.UserEntityToUserDtoConverter;
import br.com.tmvolpato.ms.application.form.UserForm;
import br.com.tmvolpato.ms.application.form.UserFormDetail;
import br.com.tmvolpato.ms.domain.model.UserEntity;
import br.com.tmvolpato.ms.domain.service.UserService;
import br.com.tmvolpato.ms.infrastructure.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static br.com.tmvolpato.ms.application.form.UserFormDetailHelper.userFormDetailTwoValid;
import static br.com.tmvolpato.ms.application.form.UserFormHelper.userFormOneValid;
import static br.com.tmvolpato.ms.application.dto.UserDtoHelper.userDtoValid;
import static br.com.tmvolpato.ms.domain.model.UserEntityHelper.userEntityOneValid;
import static br.com.tmvolpato.ms.domain.model.UserEntityHelper.userEntityTwoValid;
import static br.com.tmvolpato.ms.infrastructure.constants.UserMessageConstant.USER_NOT_FOUND;
import static java.util.List.of;
import static java.util.UUID.fromString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserApplicationServiceTest extends AbstractUnitTest {

    @InjectMocks
    private UserApplicationService userApplicationService;

    @Captor
    private ArgumentCaptor<UserEntity> captor;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserFormToUserEntityConverter userFormToUserEntityConverter;

    @Mock
    private UserFormDetailToUserEntityConverter userFormDetailToUserEntityConverter;

    @Mock
    private UserEntityToUserDtoConverter userEntityToUserDtoConverter;

    @Test
    @DisplayName("Save user successfully")
    void givenUserForm_whenCalledCreateUser_thenSavedSuccessfully() {

        when(this.userFormToUserEntityConverter.convert(any(UserForm.class)))
                .thenReturn(userEntityOneValid());

        when(this.passwordEncoder.encode(anyString())).thenReturn("$2a$10$Gait0kHgWW4EzFPHp6QyJe6v7b2aSKJCk7R2qQI1jLaZKCbtDe0U.");

        this.userApplicationService.createUser(userFormOneValid());

        verify(this.userFormToUserEntityConverter, times(1)).convert(any(UserForm.class));
        verify(this.passwordEncoder, times(1)).encode(anyString());
        verify(this.userService, times(1)).save(this.captor.capture());

        verifyNoMoreInteractions(this.userFormToUserEntityConverter);
        verifyNoMoreInteractions(this.passwordEncoder);
        verifyNoMoreInteractions(this.userService);

        assertThat(this.captor.getValue(), notNullValue());
        assertThat(this.captor.getValue().getName(), is("Maria"));
        assertThat(this.captor.getValue().getLastName(), is("Silva"));
        assertThat(this.captor.getValue().getEmail(), is("maria@email.com"));
        assertThat(this.captor.getValue().getPassword(), is("$2a$10$Gait0kHgWW4EzFPHp6QyJe6v7b2aSKJCk7R2qQI1jLaZKCbtDe0U."));
    }

    @Test
    @DisplayName("Update user successfully")
    void givenUserExternalIdAndUserFormDetail_whenCalledUpdateUser_thenUpdatedSuccessfully() {

        when(this.userFormDetailToUserEntityConverter.convert(any(UserFormDetail.class)))
                .thenReturn(userEntityTwoValid());

        when(this.userService.findUserByUserExternalId(any(UUID.class))).thenReturn(userEntityOneValid());

        this.userApplicationService.updateUser(fromString("2c34f712-9286-4826-a4f6-5914bb74159f"), userFormDetailTwoValid());

        verify(this.userService, times(1)).findUserByUserExternalId(any(UUID.class));
        verify(this.userFormDetailToUserEntityConverter, times(1)).convert(any(UserFormDetail.class));
        verify(this.userService, times(1)).update(this.captor.capture());

        verifyNoMoreInteractions(this.userFormDetailToUserEntityConverter);
        verifyNoMoreInteractions(this.userService);

        assertThat(this.captor.getValue(), notNullValue());
        assertThat(this.captor.getValue().getName(), is("Maria"));
        assertThat(this.captor.getValue().getLastName(), is("Silva e Silva"));
    }

    @Test
    @DisplayName("Delete user successfully")
    void givenUserExternalId_whenCalledDeleteUser_thenDeleteSuccessfully() {

        this.userApplicationService.deleteUser(fromString("2c34f712-9286-4826-a4f6-5914bb74159f"));
        verify(this.userService, times(1)).delete(any(UUID.class));
        verifyNoMoreInteractions(this.userService);
    }

    @Test
    @DisplayName("List paginated with search name or lastName or email")
    void givenSearchWithValue_whenCalledList_thenReturnListPaginated() {

        final var pageRequest = PageRequest.of(1, 15, Sort.by("name"));
        final var users = of(userEntityOneValid());
        final var page = new PageImpl<>(users, pageRequest, users.size());

        when(this.userService.list(anyString(), anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(page);

        when(this.userEntityToUserDtoConverter.convert(any(UserEntity.class)))
                .thenReturn(userDtoValid());

        final var pageDto = this.userApplicationService.list("maria", 1, 15, "name", "asc");

        verify(this.userEntityToUserDtoConverter, times(1)).convert(any(UserEntity.class));
        verify(this.userService, times(1)).list(anyString(), anyInt(), anyInt(), anyString(), anyString());

        verifyNoMoreInteractions(this.userEntityToUserDtoConverter);
        verifyNoMoreInteractions(this.userService);

        assertThat(pageDto.getContent(), notNullValue());

        final var userDto = pageDto.getContent().get(0);
        assertThat(userDto.getName(), is("Maria"));

    }

    @Test
    @DisplayName("Error update user not found")
    void givenUserExternalIdDifferentOfUserFormDetail_whenCalledUpdateUser_thenReturnNotFound() {

        final var notFoundException = assertThrows(NotFoundException.class,
                this::callMethodUpdate);

        verifyNoMoreInteractions(this.userFormDetailToUserEntityConverter);
        verifyNoMoreInteractions(this.userService);

        assertThat(notFoundException.getMessage(), is(USER_NOT_FOUND));
    }

    private void callMethodUpdate() {
        this.userApplicationService.updateUser(UUID.randomUUID(), userFormDetailTwoValid());
    }

}
