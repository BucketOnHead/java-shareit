package ru.practicum.shareit.server.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.constants.OpenApiConsts;

@Schema(description = "Пользователь")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto {

    @Schema(description = OpenApiConsts.USER_ID, example = OpenApiConsts.USER_ID_EG)
    Long id;

    @Schema(description = OpenApiConsts.USER_NAME_DESC, example = OpenApiConsts.USER_NAME_EG)
    String name;

    @Schema(description = OpenApiConsts.USER_EMAIL_DESC, example = OpenApiConsts.USER_EMAIL_EG)
    String email;
}
