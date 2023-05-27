package br.com.tmvolpato.ms.application.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDtoHelper {

    public static UserDto userDtoValid() {
        return UserDto.builder()
                .userExternalId(fromString("2c34f712-9286-4826-a4f6-5914bb74159f"))
                .name("Maria")
                .lastName("Silva")
                .email("maria@email.com")
                .status(true)
                .build();
    }

    public static UserDto userDtoTwoValid() {
        return UserDto.builder()
                .userExternalId(fromString("2c34f712-9286-4826-a4f6-5914bb741592"))
                .name("Jose")
                .lastName("Silva e silva")
                .email("jose@email.com")
                .status(true)
                .build();
    }
}
