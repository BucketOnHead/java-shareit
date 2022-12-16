package ru.practicum.shareit.booking.validation.annotation;

import ru.practicum.shareit.booking.validation.validator.StartBeforeEndDatesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = StartBeforeEndDatesValidator.class)
@Target(ElementType.TYPE_USE)
@Retention(RUNTIME)
public @interface StartBeforeEndValid {
    String message() default "Start must be before end and start/end must not be null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
