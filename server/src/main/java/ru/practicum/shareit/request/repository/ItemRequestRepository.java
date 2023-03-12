package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequesterId(Long requesterId);

    Page<ItemRequest> findAllByRequesterIdIsNot(Long requesterId, Pageable pageable);

    default void validateItemRequestExistsById(Long requestId) {
        if (!existsById(requestId)) {
            throw ItemRequestNotFoundException.getFromId(requestId);
        }
    }
}
