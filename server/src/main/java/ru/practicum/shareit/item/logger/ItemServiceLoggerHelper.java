package ru.practicum.shareit.item.logger;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import ru.practicum.shareit.item.model.Item;

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
}
