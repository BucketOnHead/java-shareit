package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.user.dto.request.UserCreationDto;
import ru.practicum.shareit.user.dto.response.UserResponseDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    @Mapping(target = "id", ignore = true)
    User mapToUser(UserCreationDto userCreationDto);

    UserResponseDto mapToUserResponseDto(User user);

    List<UserResponseDto> mapToUserResponseDto(Iterable<User> users);
}
