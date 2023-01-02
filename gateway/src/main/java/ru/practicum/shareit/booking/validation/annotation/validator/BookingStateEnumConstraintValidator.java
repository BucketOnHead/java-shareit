package ru.practicum.shareit.booking.validation.annotation.validator;

import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.validation.annotation.BookingStateEnum;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BookingStateEnumConstraintValidator
        implements ConstraintValidator<BookingStateEnum, String> {

    @Override
    public boolean isValid(String possibleState, ConstraintValidatorContext context) {
        try {
            State.valueOf(possibleState);
            return true;
        } catch (IllegalArgumentException ex) {
            throw IncorrectStateException.getFromString(possibleState);
        }
    }

    private static class IncorrectStateException extends ConstraintDeclarationException {
        private static final String UNKNOWN_STATE;

        static {
            UNKNOWN_STATE = "Unknown state: %s";
        }

        public IncorrectStateException(String message) {
            super(message);
        }

        public static IncorrectStateException getFromString(String state) {
            String message = String.format(UNKNOWN_STATE, state);
            return new IncorrectStateException(message);
        }
    }
}
