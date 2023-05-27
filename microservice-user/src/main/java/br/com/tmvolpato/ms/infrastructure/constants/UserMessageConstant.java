package br.com.tmvolpato.ms.infrastructure.constants;

import br.com.tmvolpato.ms.infrastructure.exception.NonInstantiableException;

import static br.com.tmvolpato.ms.infrastructure.constants.MessageErrorConstant.ERROR_CANNOT_BE_INSTANTIATED;

public final class UserMessageConstant {

    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String USER_NOT_FOUND = "User not found";

    private UserMessageConstant() {
        throw new NonInstantiableException(ERROR_CANNOT_BE_INSTANTIATED);
    }
}
