package ru.practicum.shareit.user.dto.in;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User.CreationInfo;
import ru.practicum.shareit.user.model.User.UpdateInfo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Setter
@Getter
@NoArgsConstructor
public class RequestUserDto {
    @NotNull(groups = {CreationInfo.class})
    private String name;

    @NotNull(groups = {CreationInfo.class})
    @Email(groups = {CreationInfo.class, UpdateInfo.class})
    private String email;

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }
}
