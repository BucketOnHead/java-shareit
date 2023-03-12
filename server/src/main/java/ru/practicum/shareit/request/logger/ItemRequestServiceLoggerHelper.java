package ru.practicum.shareit.request.logger;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import ru.practicum.shareit.request.dto.response.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@UtilityClass
public final class ItemRequestServiceLoggerHelper {


    public static void itemRequestSaved(
            Logger log,
            ItemRequest itemRequest
    ) {
        log.debug("ITEM_REQUEST["
                        + "id={}"
                        + "] saved.",
                itemRequest.getId());
    }

    public static void itemRequestByRequesterIdReturned(
            Logger log,
            List<ItemRequestDto> itemRequestDtos,
            Long userId
    ) {
        log.debug("ITEM_REQUEST_LIST<DTO>["
                        + "size={}"
                        + "returned for USER["
                        + "id={}"
                        + "].",
                itemRequestDtos.size(),
                userId);
    }

    public static void itemRequestPageByRequesterIdReturned(
            Logger log,
            Integer from,
            Integer size,
            List<ItemRequestDto> itemRequestDtos,
            Long userId
    ) {
        log.debug("ITEM_REQUEST_PAGE<DTO>["
                        + "from={}, "
                        + "size={}, "
                        + "requests_count={}"
                        + "] returned for USER["
                        + "id={}"
                        + "].",
                from,
                size,
                itemRequestDtos.size(),
                userId);
    }

    public static void itemRequestReturned(
            Logger log,
            ItemRequestDto itemRequestDto,
            Long userId
    ) {
        log.debug("ITEM_REQUEST<DTO>["
                        + "id={}"
                        + "] returned for USER["
                        + "id={}"
                        + "].",
                itemRequestDto.getId(),
                userId);
    }
}
