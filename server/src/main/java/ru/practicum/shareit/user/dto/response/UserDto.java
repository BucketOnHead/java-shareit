package ru.practicum.shareit.user.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto {
    Long id;
    String name;
    String email;
}
