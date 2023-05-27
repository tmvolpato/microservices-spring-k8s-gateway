package br.com.tmvolpato.ms.application.form;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserFormDetailHelper {

    public static UserFormDetail userFormDetailOneValid() {
        return UserFormDetail.builder()
                .userExternalId(fromString("2c34f712-9286-4826-a4f6-5914bb74159f"))
                .name("Maria")
                .lastName("Silva")
                .status(true)
                .build();
    }

    public static UserFormDetail userFormDetailTwoValid() {
        return UserFormDetail.builder()
                .userExternalId(fromString("2c34f712-9286-4826-a4f6-5914bb74159f"))
                .name("Maria")
                .lastName("Silva e Silva")
                .status(true)
                .build();
    }
}
