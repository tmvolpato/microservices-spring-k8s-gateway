package br.com.tmvolpato.ms.infrastructure.exception;

public class OperationNotSupportedException extends RuntimeException {

    public OperationNotSupportedException() {
        super();
    }

    public OperationNotSupportedException(final String message) {
        super(message);
    }
}
