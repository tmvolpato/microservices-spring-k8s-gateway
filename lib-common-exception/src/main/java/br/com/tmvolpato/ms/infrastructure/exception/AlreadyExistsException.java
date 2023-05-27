package br.com.tmvolpato.ms.infrastructure.exception;

public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException() {
        super();
    }

    public AlreadyExistsException(final String message) {
        super(message);
    }

    public AlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
