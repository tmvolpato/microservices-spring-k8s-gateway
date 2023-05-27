package br.com.tmvolpato.ms.application.converter.in;

import br.com.tmvolpato.ms.application.form.UserForm;
import br.com.tmvolpato.ms.domain.model.UserEntity;
import br.com.tmvolpato.ms.infrastructure.exception.ConverterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static br.com.tmvolpato.ms.infrastructure.constants.MessageErrorConstant.ERROR_CONVERTING_OBJECT;

@Slf4j
@Lazy
@Component
@RequiredArgsConstructor
public class UserFormToUserEntityConverter implements Converter<UserForm, UserEntity> {

    @Override
    @NonNull
    public UserEntity convert(@NonNull final UserForm userForm) {
        try {

            return UserEntity.builder()
                    .name(userForm.getName())
                    .lastName(userForm.getLastName())
                    .email(userForm.getEmail())
                    .status(userForm.isStatus())
                    .build();

        } catch (final Exception ex) {
            log.error("[ERROR] - UserFormToUserEntityConverter={}", ex.getMessage());
            throw new ConverterException(ERROR_CONVERTING_OBJECT);
        }
    }
}
