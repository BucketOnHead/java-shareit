package ru.practicum.shareit.server.itemrequest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.server.itemrequest.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.server.itemrequest.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequesterId(Long requesterId);

    Page<ItemRequest> findAllByRequesterIdIsNot(Long requesterId, Pageable pageable);

    default ItemRequest findByIdOrThrow(Long requestId) {
        var optionalRequest = findById(requestId);
        if (optionalRequest.isEmpty()) {
            throw new ItemRequestNotFoundException(requestId);
        }

        return optionalRequest.get();
    }
}
