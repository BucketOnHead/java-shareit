package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByItemRequestId(Long itemRequestId);

    Page<Item> findAllByOwnerId(Long ownerId, Pageable page);

    Page<Item> findAllByIsAvailableIsTrue(Pageable page);

    default void validateItemExistsById(Long itemId) {
        if (!existsById(itemId)) {
            throw ItemNotFoundException.fromItemId(itemId);
        }
    }
}
