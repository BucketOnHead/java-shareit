package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public static ErrorResponse getFromException(Throwable th) {
        return new ErrorResponse(th.getMessage());
    }
}
