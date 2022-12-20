package ru.practicum.shareit.validation.annotation.validator;

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.messageinterpolation.ExpressionLanguageFeatureLevel;
import org.junit.jupiter.api.Test;

import javax.validation.ClockProvider;
import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class NotEmptyIfNotNullConstraintValidatorTest {
    /**
     * Method under test: {@link NotEmptyIfNotNullConstraintValidator#isValid(String, ConstraintValidatorContext)}
     */
    @Test
    void testIsValid() {
        // test context
        var validator = new NotEmptyIfNotNullConstraintValidator();

        assertTrue(validator.isValid("Str", new ConstraintValidatorContextImpl(
                mock(ClockProvider.class),
                PathImpl.createRootPath(),
                null,
                "Constraint Validator Payload",
                ExpressionLanguageFeatureLevel.DEFAULT,
                ExpressionLanguageFeatureLevel.DEFAULT)));
    }

    /**
     * Method under test: {@link NotEmptyIfNotNullConstraintValidator#isValid(String, ConstraintValidatorContext)}
     */
    @Test
    void testIsValidNull() {
        // test context
        var validator = new NotEmptyIfNotNullConstraintValidator();

        assertTrue(validator.isValid(null, new ConstraintValidatorContextImpl(
                mock(ClockProvider.class),
                PathImpl.createRootPath(),
                null,
                "Constraint Validator Payload",
                ExpressionLanguageFeatureLevel.DEFAULT,
                ExpressionLanguageFeatureLevel.DEFAULT)));
    }

    /**
     * Method under test: {@link NotEmptyIfNotNullConstraintValidator#isValid(String, ConstraintValidatorContext)}
     */
    @Test
    void testIsValidEmpty() {
        // test context
        var validator = new NotEmptyIfNotNullConstraintValidator();

        assertFalse(validator.isValid("", new ConstraintValidatorContextImpl(
                mock(ClockProvider.class),
                PathImpl.createRootPath(),
                null,
                "Constraint Validator Payload",
                ExpressionLanguageFeatureLevel.DEFAULT,
                ExpressionLanguageFeatureLevel.DEFAULT)));
    }
}
