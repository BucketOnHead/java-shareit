package ru.practicum.shareit.server.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.constants.OpenApiConsts;
import ru.practicum.shareit.server.dto.validation.Groups;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Schema(description = "Данные для добавления/обновления пользователя")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserCreationDto {

    @Schema(
            description = OpenApiConsts.USER_NAME_DESC,
            example = OpenApiConsts.USER_NAME_EG,
            maxLength = 50
    )
    @NotNull(groups = Groups.OnCreate.class)
    String name;

    @Schema(
            description = OpenApiConsts.USER_EMAIL_DESC,
            example = OpenApiConsts.USER_EMAIL_EG,
            maxLength = 100
    )
    @NotNull(groups = Groups.OnCreate.class)
    @Email(groups = {Groups.OnCreate.class, Groups.OnUpdate.class})
    String email;
}
