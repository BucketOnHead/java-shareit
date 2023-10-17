package ru.practicum.shareit.gateway.booking.validation.annotation;

import ru.practicum.shareit.gateway.booking.validation.annotation.validator.BookingStateEnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = BookingStateEnumValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BookingStateEnum {
    String message() default "should be booking state enum";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
