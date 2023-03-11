package ru.practicum.shareit.item.logger;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import ru.practicum.shareit.item.dto.response.ItemDetailsResponseDto;
import ru.practicum.shareit.item.dto.response.SimpleItemResponseDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@UtilityClass
public final class ItemServiceLoggerHelper {

    public static void itemSaved(
            Logger log,
            Item item
    ) {
        log.debug("ITEM["
                        + "id={}"
                        + "] saved.",
                item.getId());
    }

    public static void itemDtoReturned(
            Logger log,
            ItemDetailsResponseDto itemDto
    ) {
        log.debug("ITEM<DTO>["
                        + "id={}"
                        + "] returned.",
                itemDto.getId());
    }

    public static void itemDtosReturned(
            Logger log,
            List<ItemDetailsResponseDto> itemDtos
    ) {
        log.debug("ITEMS<DTO>["
                        + "size={}"
                        + "] returned.",
                itemDtos.size());
    }

    public static void itemDtosByTextReturned(
            Logger log,
            String text,
            List<SimpleItemResponseDto> itemDtos
    ) {
        log.debug("ITEMS<DTO>["
                        + "text={}, "
                        + "size={}"
                        + "] returned.",
                text,
                itemDtos.size());
    }

    public static void itemUpdated(
            Logger log,
            Item item
    ) {
        log.debug("ITEM["
                        + "id={}"
                        + "] updated.",
                item.getId());
    }
}
