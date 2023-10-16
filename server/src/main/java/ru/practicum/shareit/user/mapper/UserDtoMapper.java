package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.commondto.user.request.UserCreationDto;
import ru.practicum.shareit.commondto.user.response.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", nullValueMappingStrategy = RETURN_DEFAULT)
public interface UserDtoMapper {

    @Mapping(target = "id", ignore = true)
    User mapToUser(UserCreationDto userCreationDto);

    UserDto mapToUserDto(User user);

    List<UserDto> mapToUserDto(Iterable<User> users);
}
