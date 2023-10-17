package ru.practicum.shareit.gateway.booking.validation.annotation.validator;

import ru.practicum.shareit.gateway.booking.exception.IncorrectStateException;
import ru.practicum.shareit.gateway.booking.validation.annotation.BookingStateEnum;
import ru.practicum.shareit.server.constants.booking.BookingState;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BookingStateEnumValidator implements
        ConstraintValidator<BookingStateEnum, String> {

    @Override
    public boolean isValid(String state, ConstraintValidatorContext context) {
        try {
            BookingState.valueOf(state);
            return true;
        } catch (IllegalArgumentException ex) {
            throw new IncorrectStateException(state);
        }
    }
}
