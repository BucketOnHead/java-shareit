package ru.practicum.shareit.item.repository;

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

    /**
     * Validates if an item with the given itemId exists.
     * If it does not exist, throws an {@link ItemNotFoundException}.
     *
     * @param itemId The ID of the item to validate.
     * @throws ItemNotFoundException If the item with the given ID does not exist.
     */
    default void validateItemExistsById(Long itemId) {
        if (!existsById(itemId)) {
            throw ItemNotFoundException.byId(itemId);
        }
    }

    /**
     * Validates if a user with the given userId
     * is the owner of the item with the given itemId.
     * If the user is not the owner of the item
     * or the item does not exist, throws an ItemNotFoundException.
     *
     * @param itemId The ID of the item to validate.
     * @param userId The ID of the user to validate.
     * @throws ItemNotFoundException If the item with the given ID
     *                               does not exist or the user
     *                               is not the owner of the item.
     */
    default void validateUserIdIsItemOwner(Long itemId, Long userId) {
        if (!existsByIdAndOwnerId(itemId, userId)) {
            throw ItemNotFoundException.fromItemIdAndUserId(itemId, userId);
        }
    }
}
