package br.com.tmvolpato.ms.infrastructure.exeptionhandler;

import br.com.tmvolpato.ms.infrastructure.exception.AlreadyExistsException;
import br.com.tmvolpato.ms.infrastructure.exception.ConverterException;
import br.com.tmvolpato.ms.infrastructure.exception.CustomClientException;
import br.com.tmvolpato.ms.infrastructure.exception.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import static java.util.Objects.nonNull;

@RestControllerAdvice
public class BaseExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String NO_MESSAGE = "No message";

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException exception,
                                                                          final HttpHeaders headers, final HttpStatus httpStatus,
                                                                          final WebRequest request) {

        return this.buildResponseEntity(new MessageError(HttpStatus.BAD_REQUEST, exception.getParameterName(), exception));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException exception,
                                                                     final HttpHeaders headers,
                                                                     final HttpStatus httpStatus,
                                                                     final WebRequest request) {

        final var builder = new StringBuilder();
        builder.append(exception.getContentType());
        builder.append(" ");
        builder.append("media type is not supported. Supported media types are ");
        exception.getSupportedMediaTypes().forEach(type -> builder.append(type).append(", "));
        final var message = builder.substring(0, builder.length() - 2);

        return this.buildResponseEntity(new MessageError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, message, exception));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {

        final var messageError = new MessageError();
        messageError.setHttpStatus(HttpStatus.BAD_REQUEST);
        messageError.setMessage("Validation failed for argument(s)");
        messageError.setDebugMessage(NO_MESSAGE);
        messageError.addValidationFieldErrors(exception.getBindingResult().getFieldErrors());
        messageError.addValidationObjectErrors(exception.getBindingResult().getGlobalErrors());

        return this.buildResponseEntity(messageError);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(final HttpMessageNotWritableException exception,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {

        final var message = exception.getMessage();
        return this.buildResponseEntity(new MessageError(HttpStatus.INTERNAL_SERVER_ERROR, message, exception));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException exception,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {
        final var message = "Malformed JSON request";
        return this.buildResponseEntity(new MessageError(HttpStatus.BAD_REQUEST, message, exception));
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException exception) {

        final var messageError = new MessageError();
        messageError.setHttpStatus(HttpStatus.BAD_REQUEST);
        messageError.setDebugMessage(NO_MESSAGE);
        messageError.setMessage(exception.getLocalizedMessage());
        messageError.addVConstraintValidationErrors(exception.getConstraintViolations());

        return this.buildResponseEntity(messageError);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolation(final DataIntegrityViolationException exception) {

        var message = "Data integrity";
        var status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (nonNull(exception.getCause()) && (exception.getCause() instanceof ConstraintViolationException ||
                exception.getCause() instanceof org.hibernate.exception.ConstraintViolationException)) {

            message = exception.getMessage();
            status = HttpStatus.CONFLICT;
        }

        return this.buildResponseEntity(new MessageError(status, message, exception));
    }

    @ExceptionHandler(value = {EntityNotFoundException.class, NotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFound(final RuntimeException exception) {

        return this.buildResponseEntity(new MessageError(HttpStatus.NOT_FOUND, exception.getMessage(), exception));
    }

    @ExceptionHandler(value = {AlreadyExistsException.class})
    public ResponseEntity<Object> handleConflict(final RuntimeException exception) {

        final String message = exception.getMessage();
        return this.buildResponseEntity(new MessageError(HttpStatus.CONFLICT, message, exception));
    }

    @ExceptionHandler(value = {ConverterException.class})
    public ResponseEntity<Object> handleConverter(final RuntimeException exception) {

        final String message = exception.getMessage();
        return this.buildResponseEntity(new MessageError(HttpStatus.INTERNAL_SERVER_ERROR, message, exception));
    }

    @ExceptionHandler(value = {CustomClientException.class})
    public ResponseEntity<Object> handleCustomClientException(final RuntimeException exception) {

        return this.buildResponseEntity(new MessageError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception));
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(final Exception exception) {

        final String message = exception.getMessage();
        return this.buildResponseEntity(new MessageError(HttpStatus.INTERNAL_SERVER_ERROR, message, exception));
    }

    private ResponseEntity<Object> buildResponseEntity(final MessageError messageError) {
        return new ResponseEntity<>(messageError, messageError.getHttpStatus());
    }

}
