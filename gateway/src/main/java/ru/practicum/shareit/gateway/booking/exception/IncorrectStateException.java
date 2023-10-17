package ru.practicum.shareit.gateway.booking.exception;

import javax.validation.ConstraintDeclarationException;

public class IncorrectStateException extends ConstraintDeclarationException {
    private static final long serialVersionUID = -1597651445288260040L;

    public IncorrectStateException(String state) {
        super(String.format("Unknown state: %s", state));
    }
}
