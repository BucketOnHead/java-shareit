package ru.practicum.shareit.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.commondto.error.ApiError;
import ru.practicum.shareit.item.exception.ItemAccessException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.comment.CommentNotAllowedException;
import ru.practicum.shareit.itemrequest.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

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
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
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
        log.error(ex.getMessage(), ex);
        return ApiError.builder()
                .status(HttpStatus.CONFLICT.name())
                .reason("Request conflicts with another request or with server configuration")
                .message(ex.getMessage())
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
