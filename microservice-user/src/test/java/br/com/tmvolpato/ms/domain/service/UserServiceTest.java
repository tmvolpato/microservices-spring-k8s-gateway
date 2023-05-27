package br.com.tmvolpato.ms.domain.service;

import br.com.tmvolpato.ms.AbstractUnitTest;
import br.com.tmvolpato.ms.domain.model.UserEntity;
import br.com.tmvolpato.ms.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static br.com.tmvolpato.ms.domain.model.UserEntityHelper.userEntityOneValid;
import static br.com.tmvolpato.ms.infrastructure.constants.UserMessageConstant.USER_ALREADY_EXISTS;
import static br.com.tmvolpato.ms.infrastructure.constants.UserMessageConstant.USER_NOT_FOUND;
import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserServiceTest extends AbstractUnitTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<UserEntity> captor;

    @Test
    @DisplayName("Save user successfully")
    @SuppressWarnings("CPD-START")
    void givenUserEntity_whenCalledSave_thenReturnUserEntitySavedSuccessfully() {

        when(this.userRepository.existsUserByEmail(anyString())).thenReturn(false);
        when(this.userRepository.save(any(UserEntity.class))).thenReturn(userEntityOneValid());

        this.userService.save(userEntityOneValid());
        verify(this.userRepository, times(1)).existsUserByEmail(anyString());
        verify(this.userRepository, times(1)).save(this.captor.capture());
        verifyNoMoreInteractions(this.userRepository);

        assertThat(this.captor.getValue(), notNullValue());
        assertThat(this.captor.getValue().getId(), notNullValue());
        assertThat(this.captor.getValue().getExternalId(), notNullValue());
        assertThat(this.captor.getValue().getName(), is("Maria"));
        assertThat(this.captor.getValue().getLastName(), is("Silva"));
        assertThat(this.captor.getValue().getEmail(), is("maria@email.com"));
    }

    @Test
    @DisplayName("Update user successfully")
    @SuppressWarnings("CPD-END")
    void givenUserEntity_whenCalledUpdate_thenReturnUserEntityUpdatedSuccessfully() {

        when(this.userRepository.saveAndFlush(any(UserEntity.class))).thenReturn(userEntityOneValid());

        this.userService.update(userEntityOneValid());

        verify(this.userRepository, times(1)).saveAndFlush(this.captor.capture());
        verifyNoMoreInteractions(this.userRepository);

        assertThat(this.captor.getValue(), notNullValue());
        assertThat(this.captor.getValue().getId(), notNullValue());
        assertThat(this.captor.getValue().getExternalId(), notNullValue());
        assertThat(this.captor.getValue().getName(), is("Maria"));
        assertThat(this.captor.getValue().getLastName(), is("Silva"));
        assertThat(this.captor.getValue().getEmail(), is("maria@email.com"));
    }

    @Test
    @DisplayName("Delete user successfully")
    void givenUserExternalId_whenFindUser_thenRemoveUserSuccessfully() {

        when(this.userRepository.findOne(any(Specification.class))).thenReturn(of(userEntityOneValid()));

        doNothing().when(this.userRepository).delete(any(UserEntity.class));

        this.userService.delete(UUID.randomUUID());
        verify(this.userRepository, times(1)).findOne(any(Specification.class));
        verify(this.userRepository, times(1)).delete(any(UserEntity.class));
        verifyNoMoreInteractions(this.userRepository);
    }

    @Test
    @DisplayName("Find user successfully")
    void givenUserExternalId_whenFindUser_thenReturnUserEntitySuccessfully() {

        when(this.userRepository.findOne(any(Specification.class))).thenReturn(of(userEntityOneValid()));

        final var userEntity = this.userService.findUserByUserExternalId(UUID.randomUUID());
        verify(this.userRepository, times(1)).findOne(any(Specification.class));
        verifyNoMoreInteractions(this.userRepository);

        assertThat(userEntity, notNullValue());
        assertThat(userEntity.getId(), notNullValue());
        assertThat(userEntity.getExternalId(), notNullValue());
        assertThat(userEntity.getName(), is("Maria"));
        assertThat(userEntity.getLastName(), is("Silva"));
        assertThat(userEntity.getEmail(), is("maria@email.com"));
    }

    @Test
    @DisplayName("Save e-mail already exists")
    void givenUserEntity_whenEmailAlreadyExists_thenReturnAlreadyExistsException() {

        when(this.userRepository.existsUserByEmail(anyString())).thenReturn(true);

        final Exception exception = assertThrows(Exception.class,
                () -> this.userService.save(userEntityOneValid()));

        verify(this.userRepository, times(1)).existsUserByEmail(anyString());
        verify(this.userRepository, times(0)).save(any(UserEntity.class));
        verifyNoMoreInteractions(this.userRepository);

        assertThat(exception.getMessage(), is(USER_ALREADY_EXISTS));
    }

    @Test
    @DisplayName("Delete user not found")
    void givenUserExternalId_whenDeleteUser_thenReturnUserNotFoundException() {

        when(this.userRepository.findOne(any(Specification.class)))
                .thenReturn(Optional.empty());

        final Exception exception = assertThrows(Exception.class,
                () -> this.userService.delete(UUID.randomUUID()));

        verify(this.userRepository, times(1)).findOne(any(Specification.class));
        verify(this.userRepository, times(0)).delete(any(UserEntity.class));
        verifyNoMoreInteractions(this.userRepository);

        assertThat(exception.getMessage(), is(USER_NOT_FOUND));
    }

    @Test
    @DisplayName("User not found")
    void givenUserExternalId_whenFindUser_thenReturnUserNotFoundException() {

        when(this.userRepository.findOne(any(Specification.class)))
                .thenReturn(Optional.empty());

        final Exception exception = assertThrows(Exception.class,
                () -> this.userService.findUserByUserExternalId(UUID.randomUUID()));

        verify(this.userRepository, times(1)).findOne(any(Specification.class));
        verifyNoMoreInteractions(this.userRepository);

        assertThat(exception.getMessage(), is(USER_NOT_FOUND));
    }

}
