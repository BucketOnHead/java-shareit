package ru.practicum.shareit.server.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.server.constants.OpenApiConsts;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Сведения об ошибке")
@Jacksonized
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class ApiError {

    @Schema(description = OpenApiConsts.ApiError.STATUS, example = OpenApiConsts.ApiError.STATUS_EG)
    String status;

    @Schema(description = OpenApiConsts.ApiError.REASON, example = OpenApiConsts.ApiError.REASON_EG)
    String reason;

    @Schema(description = OpenApiConsts.ApiError.MESSAGE, example = OpenApiConsts.ApiError.MESSAGE_EG)
    String message;

    @ArraySchema(arraySchema = @Schema(
            description = OpenApiConsts.ApiError.ERRORS,
            example = OpenApiConsts.ApiError.ERRORS_EG,
            nullable = true))
    @JsonInclude(Include.NON_NULL)
    List<String> errors;

    @Schema(description = OpenApiConsts.ApiError.TIMESTAMP)
    @Builder.Default
    LocalDateTime timestamp = LocalDateTime.now();
}
