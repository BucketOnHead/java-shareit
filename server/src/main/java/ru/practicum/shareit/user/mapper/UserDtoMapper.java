package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.user.dto.request.UserRequestDto;
import ru.practicum.shareit.user.dto.response.UserResponseDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    @Mapping(target = "id", ignore = true)
    User mapToUser(UserRequestDto userRequestDto);

    UserResponseDto mapToUserResponseDto(User user);

    default List<UserResponseDto> mapToUserResponseDto(Iterable<User> users) {
        return StreamSupport.stream(users.spliterator(), false)
                .map(this::mapToUserResponseDto)
                .collect(Collectors.toList());
    }
}
