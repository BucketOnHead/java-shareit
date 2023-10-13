package ru.practicum.shareit.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class ApiError {
    HttpStatus status;
    String reason;
    String message;

    @JsonInclude(Include.NON_NULL)
    List<String> errors;

    @Builder.Default
    LocalDateTime timestamp = LocalDateTime.now();
}
