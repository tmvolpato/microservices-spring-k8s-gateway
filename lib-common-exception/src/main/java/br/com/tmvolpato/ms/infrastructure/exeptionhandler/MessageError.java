package br.com.tmvolpato.ms.infrastructure.exeptionhandler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasLength;

@Getter
@Setter
public class MessageError implements Serializable {

    private static final long serialVersionUID = -5758820731183930110L;

    private static final String NO_MESSAGE = "No message";

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;
    private HttpStatus httpStatus;
    private String message;
    private String debugMessage;
    private transient List<Error> errors = new ArrayList<>();

    MessageError() {
        this.dateTime = LocalDateTime.now();
    }

    MessageError(final HttpStatus status, final String message, final Throwable ex) {
        this();
        this.httpStatus = status;
        this.message = hasLength(message) ? message : NO_MESSAGE;
        this.debugMessage = hasLength(ex.getLocalizedMessage()) ? ex.getLocalizedMessage() : NO_MESSAGE;
    }

    public void addValidationFieldErrors(final List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }

    public void addValidationObjectErrors(final List<ObjectError> globalErrors) {
        globalErrors.forEach(this::addValidationError);
    }

    public void addVConstraintValidationErrors(final Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.forEach(this::addConstraintValidationError);
    }

    private void addConstraintValidationError(final ConstraintViolation<?> constraintViolation) {
        this.addValidationError(
                constraintViolation.getRootBeanClass().getSimpleName(),
                ((PathImpl) constraintViolation.getPropertyPath()).getLeafNode().asString(),
                constraintViolation.getInvalidValue(),
                constraintViolation.getMessage());
    }

    private void addValidationError(final ObjectError objectError) {
        this.addValidationError(
                objectError.getObjectName(),
                objectError.getDefaultMessage());
    }

    private void addValidationError(final FieldError fieldError) {
        this.addValidationError(
                fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage());
    }

    private void addValidationError(final String object, final String field, final Object rejectedValue, final String message) {
        this.addError(new MessageValidationError(object, field, rejectedValue, message));
    }

    private void addValidationError(final String object, final String message) {
        this.addError(new MessageValidationError(object, message));
    }

    private void addError(final Error error) {
        this.errors.add(error);
    }

}

interface Error {
}

@Getter
@EqualsAndHashCode(callSuper = false)
class MessageValidationError implements Error {

    private static final String EMPTY = "empty";

    private final String object;
    private String field;
    private Object rejectValue;
    private final String message;

    MessageValidationError(final String object, final String message) {
        this.object = object;
        this.message = message;
    }

    MessageValidationError(final String object, final String field, final Object rejectValue, final String message) {
        this.object = object;
        this.field = field;
        if (nonNull(rejectValue)) this.rejectValue = rejectValue;
        else this.rejectValue = EMPTY;
        this.message = message;
    }

}
