package ru.practicum.shareit.user.logger;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import ru.practicum.shareit.user.dto.request.UserCreationDto;

@UtilityClass
public class UserControllerLoggerHelper {

    public void addUser(Logger log, UserCreationDto userDto) {
        log.info("Adding user: {}", userDto);
    }

    public void updateUser(Logger log, UserCreationDto userDto, Long userId) {
        log.info("Updating user with id: {}, {}", userId, userDto);
    }

    public void getUserDtoById(Logger log, Long userId) {
        log.info("Getting user by id: {}", userId);
    }

    public void getUserDtoPage(Logger log, Integer from, Integer size) {
        log.info("Getting users with pagination: (from: {}, size: {})", from, size);
    }

    public void deleteUserById(Logger log, Long userId) {
        log.info("Deleting user by id: {}", userId);
    }
}
