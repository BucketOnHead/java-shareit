package ru.practicum.shareit.item.logger;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import ru.practicum.shareit.item.dto.request.ItemRequestDto;
import ru.practicum.shareit.item.dto.request.comment.CommentRequestDto;

@UtilityClass
public final class ItemControllerLoggerHelper {

    public static void addItem(
            Logger log,
            ItemRequestDto itemDto,
            Long ownerUserId
    ) {
        log.info("add ITEM["
                        + "name='{}', "
                        + "owner_id={}"
                        + "].",
                itemDto.getName(),
                ownerUserId);
    }

    public static void updateItem(
            Logger log,
            ItemRequestDto itemDto,
            Long itemId,
            Long currentUserId
    ) {
        log.info("update ITEM["
                        + "name='{}', "
                        + "id={}, "
                        + "user_id={}"
                        + "].",
                itemDto.getName(),
                itemId,
                currentUserId);
    }

    public static void getItemById(
            Logger log,
            Long itemId,
            Long currentUserId
    ) {
        log.info("get ITEM["
                        + "id={}, "
                        + "user_id={}"
                        + "].",
                itemId,
                currentUserId);
    }

    public static void getItemsByOwnerUserId(
            Logger log,
            Long ownerUserId,
            Integer from,
            Integer size
    ) {
        log.info("get ITEM_PAGE<DTO>["
                        + "owner_id={}, "
                        + "from={}, "
                        + "size={}"
                        + "] by owner id.",
                ownerUserId,
                from,
                size);
    }

    public static void searchItemsByTextIgnoreCase(
            Logger log,
            String text,
            Integer from,
            Integer size
    ) {
        log.info("get ITEM_PAGE<DTO>["
                        + "text='{}', "
                        + "from={}, "
                        + "size={}"
                        + "] by text.",
                text,
                from,
                size);
    }

    public static void addComment(
            Logger log,
            CommentRequestDto commentDto,
            Long itemId,
            Long authorUserId
    ) {
        log.info("add COMMENT["
                        + "text='{}', "
                        + "item_id={}, "
                        + "author_id={}"
                        + "].",
                commentDto.getText(),
                itemId,
                authorUserId);
    }
}
