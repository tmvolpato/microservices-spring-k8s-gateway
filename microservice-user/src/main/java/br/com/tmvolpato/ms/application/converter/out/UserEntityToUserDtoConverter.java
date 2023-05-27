package br.com.tmvolpato.ms.application.converter.out;

import br.com.tmvolpato.ms.application.dto.UserDto;
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
public class UserEntityToUserDtoConverter implements Converter<UserEntity, UserDto> {

    @Override
    @NonNull
    public UserDto convert(@NonNull final UserEntity userEntity) {
        try {

            return UserDto.builder()
                    .userExternalId(userEntity.getExternalId())
                    .name(userEntity.getName())
                    .lastName(userEntity.getLastName())
                    .email(userEntity.getEmail())
                    .status(userEntity.isStatus())
                    .build();

        } catch (final Exception ex) {
            log.error("[ERROR] - UserEntityToUserDtoConverter={}", ex.getMessage());
            throw new ConverterException(ERROR_CONVERTING_OBJECT);
        }
    }
}
