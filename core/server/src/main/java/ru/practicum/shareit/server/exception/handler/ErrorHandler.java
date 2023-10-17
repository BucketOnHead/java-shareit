package ru.practicum.shareit.server.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.server.booking.exception.*;
import ru.practicum.shareit.server.dto.error.ApiError;
import ru.practicum.shareit.server.exception.handler.util.ErrorUtils;
import ru.practicum.shareit.server.item.exception.ItemAccessException;
import ru.practicum.shareit.server.item.exception.ItemNotFoundException;
import ru.practicum.shareit.server.item.exception.comment.CommentNotAllowedException;
import ru.practicum.shareit.server.itemrequest.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.server.user.exception.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    private static final Map<String, String> CONSTRAINS_MESSAGE = new HashMap<>();

    static {
        CONSTRAINS_MESSAGE.put("UQ_USER_EMAIL", "User email must be unique");
    }

    @ExceptionHandler({
            MissingRequestHeaderException.class,
            CommentNotAllowedException.class,
            BookingNotAwaitingApprovalException.class,
            ItemUnavailableException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .reason("Request cannot be understood by the server due to incorrect syntax")
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(ItemAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleForbiddenException(final RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return ApiError.builder()
                .status(HttpStatus.FORBIDDEN.name())
                .reason("Access denied")
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler({
            BookingNotFoundException.class,
            UserNotFoundException.class,
            ItemNotFoundException.class,
            ItemRequestNotFoundException.class,
            SelfBookingAttemptException.class,
            BookingAccessException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return ApiError.builder()
                .status(HttpStatus.NOT_FOUND.name())
                .reason("Requested resource does not exist")
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final DataIntegrityViolationException ex) {
        String message = ErrorUtils.getMessage(ex, CONSTRAINS_MESSAGE);
        if (message == null) {
            message = "Incorrect database request";
            log.warn("Custom message not found", ex);
        }

        log.error(ex.getMessage(), ex);
        return ApiError.builder()
                .status(HttpStatus.CONFLICT.name())
                .reason("Request conflicts with another request or with server configuration")
                .message(message)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleInternServerErrorException(final Throwable th) {
        log.error(th.getMessage(), th);
        return ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .reason("Server has encountered an unexpected condition that does not allow it to execute request")
                .message(th.getMessage())
                .build();
    }
}
