package br.com.tmvolpato.ms.application.converter.in;

import br.com.tmvolpato.ms.application.form.UserFormDetail;
import br.com.tmvolpato.ms.domain.model.UserEntity;
import br.com.tmvolpato.ms.infrastructure.exception.ConverterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static br.com.tmvolpato.ms.infrastructure.constants.MessageErrorConstant.ERROR_CONVERTING_OBJECT;

@Slf4j
@Lazy
@Component
public class UserFormDetailToUserEntityConverter implements Converter<UserFormDetail, UserEntity> {

    @Override
    @NonNull
    public UserEntity convert(@NonNull final UserFormDetail userFormDetail) {
        try {

            return UserEntity.builder()
                    .name(userFormDetail.getName())
                    .lastName(userFormDetail.getLastName())
                    .status(userFormDetail.isStatus())
                    .build();

        } catch (final Exception ex) {
            log.error("[ERROR] - UserFormDetailToUserEntityConverter={}", ex.getMessage());
            throw new ConverterException(ERROR_CONVERTING_OBJECT);
        }
    }
}