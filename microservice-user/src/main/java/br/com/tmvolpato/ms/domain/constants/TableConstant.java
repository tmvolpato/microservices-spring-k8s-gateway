package br.com.tmvolpato.ms.domain.constants;

import br.com.tmvolpato.ms.infrastructure.constants.MessageErrorConstant;
import br.com.tmvolpato.ms.infrastructure.exception.NonInstantiableException;

public final class TableConstant {

    public static final String USERS = "users";

    private TableConstant() {
        throw new NonInstantiableException(MessageErrorConstant.ERROR_CANNOT_BE_INSTANTIATED);
    }
}
