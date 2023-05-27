package br.com.tmvolpato.ms.interfaces.web.constants;

import br.com.tmvolpato.ms.infrastructure.exception.NonInstantiableException;

import static br.com.tmvolpato.ms.infrastructure.constants.MessageErrorConstant.ERROR_CANNOT_BE_INSTANTIATED;

public final class MappingConstant {

    public static final String PROTECTED_USERS = "/protected/users";

    private MappingConstant() {
        throw new NonInstantiableException(ERROR_CANNOT_BE_INSTANTIATED);
    }
}
