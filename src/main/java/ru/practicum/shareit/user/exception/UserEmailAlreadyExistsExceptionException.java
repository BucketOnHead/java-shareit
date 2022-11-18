package ru.practicum.shareit.user.exception;

import ru.practicum.shareit.exception.EntityAlreadyExistsException;

public class UserEmailAlreadyExistsExceptionException extends EntityAlreadyExistsException {
    private static final String EMAIL_ALREADY_EXISTS_MESSAGE = "Email '%s' already exists";

    public UserEmailAlreadyExistsExceptionException(String email) {
        super(String.format(EMAIL_ALREADY_EXISTS_MESSAGE, email));
    }
}
