package ru.practicum.shareit.user.dto.in;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.validation.group.CreationGroup;
import ru.practicum.shareit.validation.group.UpdateGroup;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class RequestUserDto {
    @NotNull(groups = {CreationGroup.class})
    private String name;

    @NotNull(groups = {CreationGroup.class})
    @Email(groups = {CreationGroup.class, UpdateGroup.class})
    private String email;
}
