package ru.practicum.shareit.commondto.validation.annotation;

import ru.practicum.shareit.commondto.validation.annotation.validator.NullableNotBlankValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import java.lang.annotation.*;

@Target({ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER,
        ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NullableNotBlankValidator.class)
@Repeatable(NullableNotBlank.List.class)
@Documented
public @interface NullableNotBlank {
    String message() default "{ru.practicum.shareit.commondto.validation.annotation.NullableNotBlank.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @NotBlank
    boolean nullable() default true;

    @Target({ElementType.METHOD,
            ElementType.FIELD,
            ElementType.ANNOTATION_TYPE,
            ElementType.CONSTRUCTOR,
            ElementType.PARAMETER,
            ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        NullableNotBlank[] value();
    }
}