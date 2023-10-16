package ru.practicum.shareit.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.List;

@Jacksonized
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class ApiError {
    String status;
    String reason;
    String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<String> errors;

    @Builder.Default
    LocalDateTime timestamp = LocalDateTime.now();
}
