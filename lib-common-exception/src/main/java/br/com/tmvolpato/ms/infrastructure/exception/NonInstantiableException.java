package br.com.tmvolpato.ms.infrastructure.exception;

public class NonInstantiableException extends RuntimeException {

    public NonInstantiableException(final String message) {
        super(message);
    }

}
