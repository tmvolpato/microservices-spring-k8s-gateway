package br.com.tmvolpato.ms.domain.constants;

import br.com.tmvolpato.ms.infrastructure.exception.NonInstantiableException;

import static br.com.tmvolpato.ms.infrastructure.constants.MessageErrorConstant.ERROR_CANNOT_BE_INSTANTIATED;

public final class ColumnConstant {

    public static final String ID = "id";
    public static final String EXTERNAL_ID = "external_id";
    public static final String NAME = "name";
    public static final String LAST_NAME = "last_name";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String STATUS = "status";

    private ColumnConstant() {
        throw new NonInstantiableException(ERROR_CANNOT_BE_INSTANTIATED);
    }
}
