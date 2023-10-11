package ru.practicum.shareit.itemrequest.logger;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import ru.practicum.shareit.itemrequest.dto.request.RequestItemRequestDto;

@UtilityClass
public final class ItemRequestControllerLoggerHelper {

    public static void addItemRequest(
            Logger log,
            RequestItemRequestDto requestDto,
            Long userId
    ) {
        log.info("add ITEM_REQUEST["
                        + "description='{}', "
                        + "user_id={}"
                        + "].",
                requestDto.getDescription(),
                userId);
    }

    public static void getItemRequestDtosByRequesterId(
            Logger log,
            Long userId
    ) {
        log.info("get ITEM_REQUEST<DTO>["
                        + "requester_id={}"
                        + "] by requester id.",
                userId);
    }

    public static void getItemRequestDtoById(
            Logger log,
            Long requestId,
            Long userId
    ) {
        log.info("get ITEM_REQUEST<DTO>["
                        + "id={}"
                        + "] by id for USER["
                        + "id={}"
                        + "].",
                requestId,
                userId);
    }

    public static void getItemRequestDtoPageByRequesterId(
            Logger log,
            Long requesterId,
            Integer from,
            Integer size
    ) {
        log.info("get ITEM_REQUEST_PAGE<DTO>["
                        + "from={}, "
                        + "size={}"
                        + "] by requester USER["
                        + "id={}"
                        + "].",
                from,
                size,
                requesterId);
    }
}
