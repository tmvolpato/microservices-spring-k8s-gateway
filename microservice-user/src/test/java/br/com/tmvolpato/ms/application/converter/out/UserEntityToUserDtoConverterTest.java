package br.com.tmvolpato.ms.application.converter.out;

import br.com.tmvolpato.ms.AbstractUnitTest;
import br.com.tmvolpato.ms.infrastructure.exception.ConverterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.com.tmvolpato.ms.domain.model.UserEntityHelper.userEntityOneValid;
import static br.com.tmvolpato.ms.infrastructure.constants.MessageErrorConstant.ERROR_CONVERTING_OBJECT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class UserEntityToUserDtoConverterTest extends AbstractUnitTest {

    @InjectMocks
    private UserEntityToUserDtoConverter userEntityToUserDtoConverter;

    @Test
    @DisplayName("Converter the object successfully")
    void givenUserEntity_whenCalledConverterToUserDto_thenReturnConvertedObject() {

        final var userDto = this.userEntityToUserDtoConverter.convert(userEntityOneValid());

        assertThat(userDto, notNullValue());
        assertThat(userDto.getName(), is("Maria"));
        assertThat(userDto.getLastName(), is("Silva"));
        assertThat(userDto.getEmail(), is("maria@email.com"));
    }

    @Test
    @DisplayName("Error called converter and passed null in parameter")
    void givenUserForm_whenCalledConverterNullObject_thenReturnException() {

        final ConverterException converterException = assertThrows(ConverterException.class,
                () -> this.userEntityToUserDtoConverter.convert(null));

        assertThat(converterException.getMessage(), is(ERROR_CONVERTING_OBJECT));
    }
}
