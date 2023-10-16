package ru.practicum.shareit.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.practicum.shareit.booking.exception.IncorrectStateException;
import ru.practicum.shareit.server.dto.error.ApiError;

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
    public ErrorResponse constraintDeclarationExceptionHandler(final IncorrectStateException ex) {
        log.error("[DATA ERROR]: {}.", ex.getMessage());
        return ErrorResponse.getFromException(ex);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleWebClientException(final WebClientResponseException ex) {
        var responseBody = ex.getResponseBodyAsString();

        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            ApiError apiError = objectMapper.readValue(responseBody, ApiError.class);
            return ResponseEntity.status(ex.getStatusCode()).body(apiError);
        } catch (Exception jsonEx) {
            return ResponseEntity.status(ex.getStatusCode()).body("Failed to receive message");
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse throwableHandler(final Throwable th) {
        log.error("[UNEXPECTED ERROR]: {}.", th.getMessage());
        return ErrorResponse.getFromException(th);
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class ErrorResponse {
        private static final ObjectMapper objectMapper;

        static {
            objectMapper = new ObjectMapper();
        }

        private String error;
        private String exception;

        public static ErrorResponse getFromException(Throwable th) {
            return new ErrorResponse(th.getMessage(), th.getClass().getSimpleName());
        }

        public static ErrorResponse getFromExceptionAndMessage(Throwable th, String message) {
            return new ErrorResponse(message, th.getClass().getSimpleName());
        }
    }
}
