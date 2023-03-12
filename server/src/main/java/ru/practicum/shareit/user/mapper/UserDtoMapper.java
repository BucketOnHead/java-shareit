package ru.practicum.shareit.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.request.UserRequestDto;
import ru.practicum.shareit.user.dto.response.UserResponseDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

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

    public static List<UserResponseDto> toUserResponseDto(Iterable<User> users) {
        var userDtos = new ArrayList<UserResponseDto>();

        for (User user : users) {
            var userDto = toUserResponseDto(user);
            userDtos.add(userDto);
        }

        return userDtos;
    }
}
