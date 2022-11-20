package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse missingRequestHeaderExceptionHandler(final MissingRequestHeaderException ex) {
        log.error("Request error: {}", ex.getMessage());
        return ErrorResponse.getFromException(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidExceptionHandler(
            final MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.error("Validation error: {}.", message);
        return new ErrorResponse(message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse entityNotFoundExceptionHandler(final EntityNotFoundException ex) {
        log.error("Search error: {}.", ex.getMessage());
        return ErrorResponse.getFromException(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse entityAlreadyExistsHandler(final EntityAlreadyExistsException ex) {
        log.error("Request error: {}.", ex.getMessage());
        return ErrorResponse.getFromException(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse incorrectParameterExceptionHandler(final IncorrectParameterException ex) {
        log.error("Parameter error: {}.", ex.getMessage());
        return ErrorResponse.getFromException(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse throwableHandler(final Throwable th) {
        log.error("Unexpected error: {}.", th.getMessage());
        return ErrorResponse.getFromException(th);
    }
}
