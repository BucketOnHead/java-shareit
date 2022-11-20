package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> itemRepository = new HashMap<>();
    private long uniqueId = 1;

    @Override
    public Item addItem(Item item) {
        assignIdToItem(item);
        itemRepository.put(item.getId(), item);
        return itemRepository.get(item.getId());
    }

    @Override
    public Item updateItem(Item item) {
        itemRepository.put(item.getId(), item);
        return itemRepository.get(item.getId());
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.get(itemId);
    }

    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(itemRepository.values());
    }

    @Override
    public boolean containsById(Long itemId) {
        return itemRepository.containsKey(itemId);
    }

    private void assignIdToItem(Item item) {
        Long id = generateUniqueId();
        item.setId(id);
    }

    private long generateUniqueId() {
        return uniqueId++;
    }
}
