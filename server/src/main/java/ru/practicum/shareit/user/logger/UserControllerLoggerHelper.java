package ru.practicum.shareit.user.logger;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import ru.practicum.shareit.user.dto.request.UserCreationDto;

@UtilityClass
public class UserControllerLoggerHelper {

    public void addUser(
            Logger log,
            UserCreationDto userDto
    ) {
        log.info("add USER["
                        + "name='{}', "
                        + "email='{}'"
                        + "].",
                userDto.getName(),
                userDto.getEmail());
    }

    public void updateUser(
            Logger log,
            UserCreationDto userDto,
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

    public void getUserDtoById(
            Logger log,
            Long userId
    ) {
        log.info("get USER["
                        + "id={}"
                        + "] by id.",
                userId);
    }

    public void getUserDtoPage(
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

    public void deleteUserById(
            Logger log,
            Long userId
    ) {
        log.info("delete USER["
                        + "id={}"
                        + "] by id.",
                userId);
    }
}
