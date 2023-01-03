package ru.practicum.shareit.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserRequestDto {
    private String name;
    private String email;
}
