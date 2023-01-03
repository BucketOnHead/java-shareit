package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.request.UserRequestDto;
import ru.practicum.shareit.user.dto.response.UserResponseDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class UserDtoMapper {
    private UserDtoMapper() {
        throw new AssertionError("This is a utility class and cannot be instantiated");
    }

    // ╔══╗───╔═══╗───╔══╗───╔╗──╔╗──────╔══╗────╔════╗───╔══╗
    // ║╔═╝───║╔═╗║───║╔╗║───║║──║║──────║╔╗╚╗───╚═╗╔═╝───║╔╗║
    // ║╚═╗───║╚═╝║───║║║║───║╚╗╔╝║──────║║╚╗║─────║║─────║║║║
    // ║╔═╝───║╔╗╔╝───║║║║───║╔╗╔╗║──────║║─║║─────║║─────║║║║
    // ║║─────║║║║────║╚╝║───║║╚╝║║──────║╚═╝║─────║║─────║╚╝║
    // ╚╝─────╚╝╚╝────╚══╝───╚╝──╚╝──────╚═══╝─────╚╝─────╚══╝

    public static User toUser(UserRequestDto userDto) {
        User user = new User();

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        return user;
    }

    // ╔════╗───╔══╗──────╔══╗────╔════╗───╔══╗
    // ╚═╗╔═╝───║╔╗║──────║╔╗╚╗───╚═╗╔═╝───║╔╗║
    // ──║║─────║║║║──────║║╚╗║─────║║─────║║║║
    // ──║║─────║║║║──────║║─║║─────║║─────║║║║
    // ──║║─────║╚╝║──────║╚═╝║─────║║─────║╚╝║
    // ──╚╝─────╚══╝──────╚═══╝─────╚╝─────╚══╝

    public static UserResponseDto toUserDto(User user) {
        UserResponseDto userDto = new UserResponseDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    // ╔════╗───╔══╗──────╔╗─────╔══╗───╔══╗───╔════╗──────╔══╗────╔════╗───╔══╗
    // ╚═╗╔═╝───║╔╗║──────║║─────╚╗╔╝───║╔═╝───╚═╗╔═╝──────║╔╗╚╗───╚═╗╔═╝───║╔╗║
    // ──║║─────║║║║──────║║──────║║────║╚═╗─────║║────────║║╚╗║─────║║─────║║║║
    // ──║║─────║║║║──────║║──────║║────╚═╗║─────║║────────║║─║║─────║║─────║║║║
    // ──║║─────║╚╝║──────║╚═╗───╔╝╚╗───╔═╝║─────║║────────║╚═╝║─────║║─────║╚╝║
    // ──╚╝─────╚══╝──────╚══╝───╚══╝───╚══╝─────╚╝────────╚═══╝─────╚╝─────╚══╝

    public static List<UserResponseDto> toUserDto(Collection<User> users) {
        return users.stream()
                .map(UserDtoMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
