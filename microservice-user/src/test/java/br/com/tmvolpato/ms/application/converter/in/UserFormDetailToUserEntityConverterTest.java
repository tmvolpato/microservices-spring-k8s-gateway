package br.com.tmvolpato.ms.application.converter.in;

import br.com.tmvolpato.ms.AbstractUnitTest;
import br.com.tmvolpato.ms.infrastructure.exception.ConverterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.com.tmvolpato.ms.application.form.UserFormDetailHelper.userFormDetailOneValid;
import static br.com.tmvolpato.ms.infrastructure.constants.MessageErrorConstant.ERROR_CONVERTING_OBJECT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class UserFormDetailToUserEntityConverterTest extends AbstractUnitTest {

    @InjectMocks
    private UserFormDetailToUserEntityConverter userFormDetailToUserEntityConverter;

    @Test
    @DisplayName("Converter the object successfully")
    void givenUserFormDetail_whenCalledConverterToUserEntity_thenReturnConvertedObject() {

        final var userEntity = this.userFormDetailToUserEntityConverter.convert(userFormDetailOneValid());

        assertThat(userEntity, notNullValue());
        assertThat(userEntity.getName(), is("Maria"));
        assertThat(userEntity.getLastName(), is("Silva"));
    }

    @Test
    @DisplayName("Error called converter and passed null in parameter")
    void givenUserForm_whenCalledConverterNullObject_thenReturnException() {

        final ConverterException converterException = assertThrows(ConverterException.class,
                () -> this.userFormDetailToUserEntityConverter.convert(null));

        assertThat(converterException.getMessage(), is(ERROR_CONVERTING_OBJECT));
    }
}
