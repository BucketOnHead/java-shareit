package ru.practicum.shareit.commondto.booking.validation.annotation;

import ru.practicum.shareit.commondto.booking.validation.annotation.validator.StartBeforeEndValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartBeforeEndValidator.class)
@Documented
public @interface StartBeforeEnd {
    String message() default "{ru.practicum.shareit.dto.booking.validation.annotation.StartBeforeEnd.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
