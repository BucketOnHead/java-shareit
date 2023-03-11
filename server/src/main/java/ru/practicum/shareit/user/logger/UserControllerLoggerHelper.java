package ru.practicum.shareit.user.logger;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import ru.practicum.shareit.user.dto.request.UserRequestDto;

@UtilityClass
public final class UserControllerLoggerHelper {

    public static void addUser(
            Logger log,
            UserRequestDto userDto
    ) {
        log.info("add USER["
                        + "name='{}', "
                        + "email='{}'"
                        + "].",
                userDto.getName(),
                userDto.getEmail());
    }

    public static void updateUser(
            Logger log,
            UserRequestDto userDto,
            Long userId
    ) {
        log.info("update USER["
                        + "name='{}', "
                        + "email='{}', "
                        + "user_id={}"
                        + "].",
                userDto.getName(),
                userDto.getEmail(),
                userId);
    }

    public static void getUserDtoById(
            Logger log,
            Long userId
    ) {
        log.info("get USER["
                        + "id={}"
                        + "] by id.",
                userId);
    }

    public static void getUserDtoPage(
            Logger log,
            Integer from,
            Integer size
    ) {
        log.info("get USER_PAGE<DTO>["
                        + "from={}, "
                        + "size={}"
                        + "].",
                from,
                size);
    }

    public static void deleteUserById(
            Logger log,
            Long userId
    ) {
        log.info("delete USER["
                        + "id={}"
                        + "] by id.",
                userId);
    }
}
