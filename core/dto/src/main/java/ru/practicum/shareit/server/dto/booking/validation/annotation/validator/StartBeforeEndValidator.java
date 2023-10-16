package ru.practicum.shareit.server.dto.booking.validation.annotation.validator;

import ru.practicum.shareit.server.dto.booking.request.BookingCreationDto;
import ru.practicum.shareit.server.dto.booking.validation.annotation.StartBeforeEnd;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartBeforeEndValidator implements
        ConstraintValidator<StartBeforeEnd, BookingCreationDto> {

    @Override
    public boolean isValid(BookingCreationDto bookingDto, ConstraintValidatorContext context) {
        if (bookingDto == null || bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            return false;
        }

        var start = bookingDto.getStart();
        var end = bookingDto.getEnd();

        return start.isBefore(end);
    }
}
