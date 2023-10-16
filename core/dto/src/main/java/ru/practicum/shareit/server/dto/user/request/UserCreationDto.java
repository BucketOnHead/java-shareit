package ru.practicum.shareit.server.dto.user.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.dto.validation.Groups;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserCreationDto {

    @NotNull(groups = Groups.OnCreate.class)
    String name;

    @NotNull(groups = Groups.OnCreate.class)
    @Email(groups = {Groups.OnCreate.class, Groups.OnUpdate.class})
    String email;
}
