package br.com.tmvolpato.ms.application.form;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserFormHelper {

    public static UserForm userFormOneValid() {
        return UserForm.builder()
                .name("Maria")
                .lastName("Silva")
                .email("test@email.com")
                .password("12345678")
                .status(true)
                .build();
    }

    public static UserForm userFormTwoValid() {
        return UserForm.builder()
                .name("Jose")
                .lastName("Silva")
                .email("jose@email.com")
                .password("12345678")
                .status(true)
                .build();
    }

    public static UserForm userFormThreeValid() {
        return UserForm.builder()
                .name("João")
                .lastName("Silva")
                .email("joao@email.com")
                .password("12345678")
                .status(true)
                .build();
    }

    public static UserForm userFormOneInvalid() {
        return UserForm.builder()
                .name(null)
                .lastName("Silva")
                .email("test@email.com")
                .password("12345678")
                .status(true)
                .build();
    }

}
