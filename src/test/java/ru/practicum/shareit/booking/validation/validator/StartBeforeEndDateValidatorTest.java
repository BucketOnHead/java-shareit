package ru.practicum.shareit.booking.validation.validator;

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.messageinterpolation.ExpressionLanguageFeatureLevel;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.in.RequestBookingDto;

import javax.validation.ClockProvider;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class StartBeforeEndDateValidatorTest {
    /**
     * Method under test: {@link StartBeforeEndDateValidator#isValid(RequestBookingDto, ConstraintValidatorContext)}
     */
    @Test
    void testIsValid_Same() {
        // test parameters
        final LocalDateTime time = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
        // test context
        final StartBeforeEndDateValidator validator = new StartBeforeEndDateValidator();

        RequestBookingDto requestBookingDto = new RequestBookingDto();
        requestBookingDto.setStart(time);
        requestBookingDto.setEnd(time);

        assertFalse(validator.isValid(requestBookingDto,
                new ConstraintValidatorContextImpl(
                        mock(ClockProvider.class),
                        PathImpl.createRootPath(),
                        null,
                        "Constraint Validator Payload",
                        ExpressionLanguageFeatureLevel.DEFAULT,
                        ExpressionLanguageFeatureLevel.DEFAULT)));
    }

    /**
     * Method under test: {@link StartBeforeEndDateValidator#isValid(RequestBookingDto, ConstraintValidatorContext)}
     */
    @Test
    void testIsValid_NullEnd() {
        // test parameters
        final LocalDateTime start = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
        // test context
        final StartBeforeEndDateValidator validator = new StartBeforeEndDateValidator();

        RequestBookingDto requestBookingDto = new RequestBookingDto();
        requestBookingDto.setStart(start);
        requestBookingDto.setEnd(null);

        assertFalse(validator.isValid(requestBookingDto,
                new ConstraintValidatorContextImpl(
                        mock(ClockProvider.class),
                        PathImpl.createRootPath(),
                        null,
                        "Constraint Validator Payload",
                        ExpressionLanguageFeatureLevel.DEFAULT,
                        ExpressionLanguageFeatureLevel.DEFAULT)));
    }

    /**
     * Method under test: {@link StartBeforeEndDateValidator#isValid(RequestBookingDto, ConstraintValidatorContext)}
     */
    @Test
    void testIsValid_NullStart() {
        // test parameters
        final LocalDateTime end = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
        // test context
        final StartBeforeEndDateValidator validator = new StartBeforeEndDateValidator();

        RequestBookingDto requestBookingDto = new RequestBookingDto();
        requestBookingDto.setStart(null);
        requestBookingDto.setEnd(end);

        assertFalse(validator.isValid(requestBookingDto,
                new ConstraintValidatorContextImpl(
                        mock(ClockProvider.class),
                        PathImpl.createRootPath(),
                        null,
                        "Constraint Validator Payload",
                        ExpressionLanguageFeatureLevel.DEFAULT,
                        ExpressionLanguageFeatureLevel.DEFAULT)));
    }

    /**
     * Method under test: {@link StartBeforeEndDateValidator#isValid(RequestBookingDto, ConstraintValidatorContext)}
     */
    @Test
    void testIsValid_NullStartAndNullEnd() {
        // test context
        final StartBeforeEndDateValidator validator = new StartBeforeEndDateValidator();

        RequestBookingDto requestBookingDto = new RequestBookingDto();
        requestBookingDto.setStart(null);
        requestBookingDto.setEnd(null);

        assertFalse(validator.isValid(requestBookingDto,
                new ConstraintValidatorContextImpl(
                        mock(ClockProvider.class),
                        PathImpl.createRootPath(),
                        null,
                        "Constraint Validator Payload",
                        ExpressionLanguageFeatureLevel.DEFAULT,
                        ExpressionLanguageFeatureLevel.DEFAULT)));
    }

    /**
     * Method under test: {@link StartBeforeEndDateValidator#isValid(RequestBookingDto, ConstraintValidatorContext)}
     */
    @Test
    void testIsValid_StartBeforeEnd() {
        // test parameters
        final LocalDateTime start = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
        final LocalDateTime end   = start.plusSeconds(1L);
        // test context
        final StartBeforeEndDateValidator validator = new StartBeforeEndDateValidator();

        RequestBookingDto requestBookingDto = new RequestBookingDto();
        requestBookingDto.setStart(start);
        requestBookingDto.setEnd(end);

        assertTrue(validator.isValid(requestBookingDto,
                new ConstraintValidatorContextImpl(
                        mock(ClockProvider.class),
                        PathImpl.createRootPath(),
                        null,
                        "Constraint Validator Payload",
                        ExpressionLanguageFeatureLevel.DEFAULT,
                        ExpressionLanguageFeatureLevel.DEFAULT)));
    }

    /**
     * Method under test: {@link StartBeforeEndDateValidator#isValid(RequestBookingDto, ConstraintValidatorContext)}
     */
    @Test
    void testIsValid_StartAfterEnd() {
        // test parameters
        final LocalDateTime start = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
        final LocalDateTime end   = start.minusSeconds(1L);
        // test context
        final StartBeforeEndDateValidator validator = new StartBeforeEndDateValidator();

        RequestBookingDto requestBookingDto = new RequestBookingDto();
        requestBookingDto.setStart(start);
        requestBookingDto.setEnd(end);

        assertFalse(validator.isValid(requestBookingDto,
                new ConstraintValidatorContextImpl(
                        mock(ClockProvider.class),
                        PathImpl.createRootPath(),
                        null,
                        "Constraint Validator Payload",
                        ExpressionLanguageFeatureLevel.DEFAULT,
                        ExpressionLanguageFeatureLevel.DEFAULT)));
    }
}
