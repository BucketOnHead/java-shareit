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
}
