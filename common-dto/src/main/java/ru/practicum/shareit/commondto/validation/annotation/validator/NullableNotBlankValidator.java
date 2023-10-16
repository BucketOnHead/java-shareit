package ru.practicum.shareit.commondto.validation.annotation.validator;

import ru.practicum.shareit.commondto.validation.annotation.NullableNotBlank;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NullableNotBlankValidator implements
        ConstraintValidator<NullableNotBlank, String> {

    @Override
    public boolean isValid(String str, ConstraintValidatorContext context) {
        if (str == null) {
            return true;
        }

        return !str.isBlank();
    }
}
