package ru.practicum.shareit.server.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.server.item.exception.ItemNotFoundException;
import ru.practicum.shareit.server.item.model.Item;

import java.util.Collection;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByItemRequestId(Long itemRequestId);

    List<Item> findAllByItemRequestIdIn(Collection<Long> itemRequestIds);

    Page<Item> findAllByOwnerId(Long ownerId, Pageable page);

    /**
     * Finds all available {@link Item} entities that match the given search text
     * (case-insensitive) in either the item name or description.
     */
    @Query("SELECT i FROM Item i " +
            "WHERE (i.isAvailable = TRUE) " +
            "AND (UPPER(i.name) LIKE CONCAT('%', UPPER(:text), '%')" +
            "     OR (UPPER(i.description) LIKE CONCAT('%', UPPER(:text), '%')))")
    Page<Item> findAllByText(String text, Pageable page);


    boolean existsByIdAndOwnerId(Long itemId, Long ownerId);

    default Item findByIdOrThrow(Long itemId) {
        var optionalItem = findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new ItemNotFoundException(itemId);
        }

        return optionalItem.get();
    }
}
