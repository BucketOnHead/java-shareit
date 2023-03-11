package ru.practicum.shareit.user.logger;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import ru.practicum.shareit.user.dto.response.UserResponseDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@UtilityClass
public final class UserServiceLoggerHelper {

    public static void userSaved(
            Logger log,
            User user
    ) {
        log.debug("USER["
                        + "id={}"
                        + "] saved.",
                user.getId());
    }

    public static void userUpdated(
            Logger log,
            User user
    ) {
        log.debug("USER["
                        + "id={}"
                        + "] updated.",
                user.getId());
    }

    public static void userDtoByIdReturned(
            Logger log,
            UserResponseDto userDto
    ) {
        log.debug("USER<DTO>["
                        + "id={}"
                        + "] returned.",
                userDto.getId());
    }

    public static void userDtoPageReturned(
            Logger log,
            Integer from,
            Integer size,
            List<UserResponseDto> usersDto
    ) {
        log.debug("USER_PAGE<DTO>["
                        + "from={}, "
                        + "size={}, "
                        + "users_count={}"
                        + "] returned.",
                from,
                size,
                usersDto.size());
    }

    public static void userByIdDeleted(
            Logger log,
            Long userId
    ) {
        log.debug("USER["
                        + "id={}"
                        + "] deleted.",
                userId);
    }
}
