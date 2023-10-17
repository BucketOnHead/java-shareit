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

    @Schema(description = OpenApiConsts.API_ERROR_STATUS_DESC, example = OpenApiConsts.API_ERROR_STATUS_EG)
    String status;

    @Schema(description = OpenApiConsts.API_ERROR_REASON_DESC, example = OpenApiConsts.API_ERROR_REASON_EG)
    String reason;

    @Schema(description = OpenApiConsts.API_ERROR_MESSAGE_DESC, example = OpenApiConsts.API_ERROR_MESSAGE_EG)
    String message;

    @ArraySchema(arraySchema = @Schema(
            description = OpenApiConsts.API_ERROR_ERRORS_DESC,
            example = OpenApiConsts.API_ERROR_ERRORS_EG,
            nullable = true
    ))
    @JsonInclude(Include.NON_NULL)
    List<String> errors;

    @Schema(description = OpenApiConsts.API_ERROR_TIMESTAMP_DESC)
    @Builder.Default
    LocalDateTime timestamp = LocalDateTime.now();
}
