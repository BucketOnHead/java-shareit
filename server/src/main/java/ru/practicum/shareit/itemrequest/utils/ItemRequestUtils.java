package ru.practicum.shareit.itemrequest.utils;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.itemrequest.model.ItemRequest;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@UtilityClass
public class ItemRequestUtils {

    public Set<Long> toIdsSet(Iterable<ItemRequest> requests) {
        if (requests == null) {
            return Collections.emptySet();
        }

        return StreamSupport.stream(requests.spliterator(), false)
                .map(ItemRequest::getId)
                .collect(Collectors.toSet());
    }

    public Map<Long, List<Item>> toItemsByRequestId(Iterable<Item> items) {
        if (items == null) {
            return Collections.emptyMap();
        }

        return StreamSupport.stream(items.spliterator(), false)
                .filter(i -> i.getItemRequest() != null && i.getItemRequest().getId() != null)
                .collect(Collectors.groupingBy(i -> i.getItemRequest().getId()));
    }
}
