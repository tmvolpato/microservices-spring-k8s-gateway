package br.com.tmvolpato.ms.infrastructure.exception;

public class UnprocessableEntityException extends RuntimeException {

    public UnprocessableEntityException() {
        super();
    }

    public UnprocessableEntityException(final String message) {
        super(message);
    }
}
