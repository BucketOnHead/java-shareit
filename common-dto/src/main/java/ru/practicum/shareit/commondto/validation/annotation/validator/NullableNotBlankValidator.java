package ru.practicum.shareit.commondto.validation.annotation.validator;

import ru.practicum.shareit.commondto.validation.annotation.NullableNotBlank;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class NullableNotBlankValidator implements
        ConstraintValidator<NullableNotBlank, String> {
    private boolean nullable;

    @Override
    public void initialize(NullableNotBlank annotation) {
        Objects.requireNonNull(annotation);
        nullable = annotation.nullable();
    }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext context) {
        if (str == null) {
            return nullable;
        }

        return !str.isBlank();
    }
}
