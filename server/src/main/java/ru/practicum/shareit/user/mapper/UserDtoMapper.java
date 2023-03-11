package ru.practicum.shareit.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.request.UserRequestDto;
import ru.practicum.shareit.user.dto.response.UserResponseDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class UserDtoMapper {

    public static User toUser(UserRequestDto userDto) {
        User user = new User();

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        return user;
    }

    public static UserResponseDto toUserResponseDto(User user) {
        var userDto = new UserResponseDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    public static List<UserResponseDto> toUserResponseDto(Collection<User> users) {
        return users.stream()
                .map(UserDtoMapper::toUserResponseDto)
                .collect(Collectors.toList());
    }
}
