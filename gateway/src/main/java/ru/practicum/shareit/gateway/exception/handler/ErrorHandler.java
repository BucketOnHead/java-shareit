package ru.practicum.shareit.gateway.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.practicum.shareit.gateway.booking.exception.IncorrectStateException;
import ru.practicum.shareit.gateway.exception.handler.util.ErrorUtils;
import ru.practicum.shareit.server.dto.error.ApiError;

import javax.validation.ConstraintViolationException;
import java.util.Map;

@Hidden
@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    private final ObjectMapper objectMapper;

    public ErrorHandler() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @ExceptionHandler({
            MissingRequestHeaderException.class,
            MethodArgumentTypeMismatchException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final RuntimeException ex) {
        log.info(ex.getMessage(), ex);
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError methodArgumentNotValidExceptionHandler(final MethodArgumentNotValidException ex) {
        log.info(ex.getMessage(), ex);
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Field(s) failed validation")
                .errors(ErrorUtils.buildErrorMessages(ex))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError constraintViolationExceptionHandle(final ConstraintViolationException ex) {
        log.info(ex.getMessage(), ex);
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Parameter(s) failed validation")
                .errors(ErrorUtils.buildErrorMessages(ex))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object constraintDeclarationExceptionHandler(final IncorrectStateException ex) {
        log.info(ex.getMessage(), ex);
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleWebClientResponseException(final WebClientResponseException ex) {
        log.info(ex.getMessage(), ex);
        try {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(objectMapper.readValue(ex.getResponseBodyAsString(), ApiError.class));
        } catch (JsonProcessingException jsonEx) {
            log.warn("Failed to parse server response: {}", jsonEx.getMessage(), jsonEx);
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body("Failed to parse server response");
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable th) {
        log.error(th.getMessage(), th);
        return ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .reason("Client has encountered an unexpected condition that does not allow it to execute request")
                .message(th.getMessage())
                .build();
    }
}
