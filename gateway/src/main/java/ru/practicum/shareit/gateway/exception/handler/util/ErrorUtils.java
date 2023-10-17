package ru.practicum.shareit.gateway.exception.handler.util;

import lombok.experimental.UtilityClass;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ErrorUtils {

    public List<String> buildErrorMessages(MethodArgumentNotValidException ex) {
        if (ex == null) {
            return null;
        }

        return ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> String.format("field '%s' %s, but it was '%s'",
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue()))
                .collect(Collectors.toList());
    }

    public List<String> buildErrorMessages(ConstraintViolationException ex) {
        if (ex == null) {
            return null;
        }

       return ex.getConstraintViolations()
                .stream()
                .map(pathError -> String.format("parameter '%s' %s, but it was '%s'",
                        ((PathImpl) pathError.getPropertyPath()).getLeafNode().getName(),
                        pathError.getMessage(),
                        pathError.getInvalidValue()))
                .collect(Collectors.toList());
    }
}
