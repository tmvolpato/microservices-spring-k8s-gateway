package br.com.tmvolpato.ms.domain.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserEntityHelper {

    public static UserEntity userEntityOneValid() {
        return UserEntity.builder()
                .id(1L)
                .name("Maria")
                .lastName("Silva")
                .email("maria@email.com")
                .password("12345")
                .status(true)
                .build();
    }

    public static UserEntity userEntityTwoValid() {
        return UserEntity.builder()
                .id(1L)
                .name("Maria")
                .lastName("Silva e Silva")
                .email("test@email.com")
                .password("12345")
                .status(true)
                .build();
    }

    public static UserEntity userEntityThreeValid() {
        return UserEntity.builder()
                .id(2L)
                .name("Jose")
                .lastName("Silva e silva")
                .email("jose@email.com")
                .password("12345")
                .status(true)
                .build();
    }
}
