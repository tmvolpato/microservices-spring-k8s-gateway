package br.com.tmvolpato.ms.infrastructure.exception;

public class ConverterException extends RuntimeException {

    public ConverterException() {
        super();
    }

    public ConverterException(final String message) {
        super(message);
    }

    public ConverterException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
