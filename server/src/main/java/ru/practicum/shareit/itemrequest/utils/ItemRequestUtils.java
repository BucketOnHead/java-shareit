package ru.practicum.shareit.itemrequest.utils;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.itemrequest.model.ItemRequest;

import java.util.Collections;
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
}
