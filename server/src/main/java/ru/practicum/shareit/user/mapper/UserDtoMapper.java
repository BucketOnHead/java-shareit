package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.user.dto.request.UserCreationDto;
import ru.practicum.shareit.user.dto.response.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    @Mapping(target = "id", ignore = true)
    User mapToUser(UserCreationDto userCreationDto);

    UserDto mapToUserDto(User user);

    List<UserDto> mapToUserDto(Iterable<User> users);
}
