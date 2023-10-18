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

    @Schema(description = OpenApiConsts.User.ID, example = OpenApiConsts.User.ID_EG)
    Long id;

    @Schema(description = OpenApiConsts.User.NAME, example = OpenApiConsts.User.NAME_EG)
    String name;

    @Schema(description = OpenApiConsts.User.EMAIL, example = OpenApiConsts.User.EMAIL_EG)
    String email;
}
