package ru.practicum.shareit.item.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByItemRequestId(Long itemRequestId);

    Page<Item> findAllByOwnerId(Long ownerId, Pageable page);

    /**
     * Finds all available {@link Item} entities that match the given search text
     * (case-insensitive) in either the item name or description.
     *
     * @param text The search text to match.
     * @param page The page of results to retrieve.
     * @return A {@link Page} of available {@link Item} entities that match the given search text.
     */
    @Query("SELECT i FROM Item i " +
            "WHERE (i.isAvailable = TRUE) " +
            "AND (UPPER(i.name) LIKE CONCAT('%', UPPER(:text) , '%')" +
            "     OR (UPPER(i.description) LIKE CONCAT('%', UPPER(:text) , '%')))")
    Page<Item> findAllByText(@Param("text") String text, Pageable page);

    boolean existsByIdAndOwnerId(Long itemId, Long ownerId);

    default void existsByIdOrThrow(@NonNull Long itemId) {
        if (!existsById(itemId)) {
            throw ItemNotFoundException.byId(itemId);
        }
    }

    default void validateUserIdIsItemOwner(Long itemId, Long userId) {
        if (!existsByIdAndOwnerId(itemId, userId)) {
            throw ItemNotFoundException.fromItemIdAndUserId(itemId, userId);
        }
    }
}
