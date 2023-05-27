package br.com.tmvolpato.ms.application.converter.in;

import br.com.tmvolpato.ms.AbstractUnitTest;
import br.com.tmvolpato.ms.infrastructure.exception.ConverterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.com.tmvolpato.ms.application.form.UserFormHelper.userFormOneValid;
import static br.com.tmvolpato.ms.infrastructure.constants.MessageErrorConstant.ERROR_CONVERTING_OBJECT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class UserFormToUserEntityConverterTest extends AbstractUnitTest {

    @InjectMocks
    private UserFormToUserEntityConverter userFormToUserEntityConverter;

    @Test
    @DisplayName("Converter the object successfully")
    void givenUserForm_whenCalledConverterToUserEntity_thenReturnConvertedObject() {

        final var userEntity = this.userFormToUserEntityConverter.convert(userFormOneValid());

        assertThat(userEntity, notNullValue());
        assertThat(userEntity.getName(), is("Maria"));
        assertThat(userEntity.getLastName(), is("Silva"));
        assertThat(userEntity.getEmail(), is("test@email.com"));
    }

    @Test
    @DisplayName("Error called converter and passed null in parameter")
    void givenUserForm_whenCalledConverterNullObject_thenReturnException() {

        final var converterException = assertThrows(ConverterException.class,
                () -> this.userFormToUserEntityConverter.convert(null));

        assertThat(converterException.getMessage(), is(ERROR_CONVERTING_OBJECT));
    }
}
