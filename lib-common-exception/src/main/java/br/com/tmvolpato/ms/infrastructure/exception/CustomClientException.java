package br.com.tmvolpato.ms.infrastructure.exception;

public class CustomClientException extends RuntimeException {

    public CustomClientException() {
        super();
    }

    public CustomClientException(final String message) {
        super(message);
    }

    public CustomClientException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
