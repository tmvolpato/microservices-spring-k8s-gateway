package br.com.tmvolpato.ms.infrastructure.constants;


import br.com.tmvolpato.ms.infrastructure.exception.NonInstantiableException;

public final class MessageErrorConstant {

    public static final String ERROR_CONVERTING_OBJECT = "Error converting object";
    public static final String ERROR_UNPROCESSABLE_ENTITY = "Error unprocessable entity";
    public static final String ERROR_CANNOT_BE_INSTANTIATED = "Error cannot be instantiated";

    private MessageErrorConstant() {
        throw new NonInstantiableException(MessageErrorConstant.ERROR_CANNOT_BE_INSTANTIATED);
    }
}
