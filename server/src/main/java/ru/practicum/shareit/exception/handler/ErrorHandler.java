package ru.practicum.shareit.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.IncorrectDataException;
import ru.practicum.shareit.exception.LogicException;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse missingRequestHeaderExceptionHandler(final MissingRequestHeaderException ex) {
        log.error("[REQUEST HEADER ERROR]: {}", ex.getMessage());
        return ErrorResponse.getFromException(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidExceptionHandler(final MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(fieldError -> String.format(
                        "field '%s' %s, but it was '%s'",
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue()))
                .collect(Collectors.joining(", "));
        log.error("[VALIDATION ERROR]: {}.", message);
        return ErrorResponse.getFromExceptionAndMessage(ex, message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse constraintViolationExceptionHandle(final ConstraintViolationException ex) {
        String message = ex.getConstraintViolations()
                .stream()
                .map(pathError -> String.format(
                        "parameter '%s' %s, but it was '%s'",
                        ((PathImpl) pathError.getPropertyPath()).getLeafNode().getName(),
                        pathError.getMessage(),
                        pathError.getInvalidValue()))
                .collect(Collectors.joining(", "));
        log.error("[VALIDATION ERROR]: {}.", message);
        return ErrorResponse.getFromExceptionAndMessage(ex, message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse incorrectDataHandler(final IncorrectDataException ex) {
        log.error("[DATA ERROR]: {}.", ex.getMessage());
        return ErrorResponse.getFromException(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse entityNotFoundExceptionHandler(final EntityNotFoundException ex) {
        log.error("[SEARCH ERROR]: {}.", ex.getMessage());
        return ErrorResponse.getFromException(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse logicExceptionHandler(final LogicException ex) {
        log.error("[LOGIC ERROR]: {}.", ex.getMessage());
        return ErrorResponse.getFromException(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse dataIntegrityViolationExceptionHandler(final DataIntegrityViolationException ex) {
        log.error("[DATABASE ERROR]: {}.", ex.getMostSpecificCause().getMessage());
        return ErrorResponse.getFromException(ex.getMostSpecificCause());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse throwableHandler(final Throwable th) {
        log.error("[UNEXPECTED ERROR]: {}.", th.getMessage());
        return ErrorResponse.getFromException(th);
    }
}
