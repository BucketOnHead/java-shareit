package ru.practicum.shareit.item.utils;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@UtilityClass
public class ItemUtils {

    public Set<Long> toIdsSet(Iterable<Item> items) {
        return StreamSupport.stream(items.spliterator(), true)
                .map(Item::getId)
                .collect(Collectors.toSet());
    }

    public boolean isOwner(Item item, Long userId) {
        if (userId == null || item == null) {
            return false;
        }

        var owner = item.getOwner();
        if (owner == null) {
            return false;
        }

        var ownerId = owner.getId();
        if (ownerId == null) {
            return false;
        }

        return ownerId.equals(userId);
    }

    public boolean isNotOwner(Item item, Long userId) {
        return !isOwner(item, userId);
    }

    public boolean isUnavailable(Item item) {
        if (item == null) {
            return false;
        }

        return item.getIsAvailable() == Boolean.FALSE;
    }
}
