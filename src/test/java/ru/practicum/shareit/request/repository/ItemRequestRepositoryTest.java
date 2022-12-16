package ru.practicum.shareit.request.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ItemRequestRepositoryTest {
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    /**
     * Method under test: {@link ItemRequestRepository#findAllByRequesterId(Long)}
     */
    @Test
    void testFindAllByRequesterIdWithoutItemRequest() {
        final Long requesterId = 1L;

        List<ItemRequest> allByRequesterId = itemRequestRepository.findAllByRequesterId(requesterId);

        assertNotNull(allByRequesterId);
        assertTrue(allByRequesterId.isEmpty());
    }

    /**
     * Method under test: {@link ItemRequestRepository#findAllByRequesterId(Long)}
     */
    @Test
    void testFindAllByRequesterIdWithOneItemRequest() {
        final User        requester   = addRequester(userRepository, 1L);
        final ItemRequest itemRequest = addItemRequest(itemRequestRepository, requester, 1L);

        List<ItemRequest> allByRequesterId = itemRequestRepository.findAllByRequesterId(requester.getId());

        assertNotNull(allByRequesterId);
        assertEquals(1, allByRequesterId.size());
        assertItemRequestEquals(itemRequest, allByRequesterId.get(0));
    }

    /**
     * Method under test: {@link ItemRequestRepository#findAllByRequesterId(Long)}
     */
    @Test
    void testFindAllByRequesterIdWithOneItemRequestAndOtherItemRequest() {
        final User        requester    = addRequester(userRepository, 1L);
        final User        requester2   = addRequester(userRepository, 2L);
        final ItemRequest itemRequest  = addItemRequest(itemRequestRepository, requester, 1L);
        final ItemRequest itemRequest2 = addItemRequest(itemRequestRepository, requester2, 2L);

        List<ItemRequest> allByRequesterId  = itemRequestRepository.findAllByRequesterId(requester.getId());
        List<ItemRequest> allByRequesterId2 = itemRequestRepository.findAllByRequesterId(requester2.getId());

        assertNotNull(allByRequesterId);
        assertEquals(1, allByRequesterId.size());
        assertItemRequestEquals(itemRequest, allByRequesterId.get(0));

        assertNotNull(allByRequesterId2);
        assertEquals(1, allByRequesterId2.size());
        assertItemRequestEquals(itemRequest2, allByRequesterId2.get(0));
    }

    /**
     * Method under test: {@link ItemRequestRepository#findAllByRequesterIdIsNotLike(Long, Pageable)}
     */
    @Test
    void testFindAllByRequesterIdIsNotLike() {
        final User        requester    = addRequester(userRepository, 1L);
        final User        requester2   = addRequester(userRepository, 2L);
        final ItemRequest itemRequest  = addItemRequest(itemRequestRepository, requester, 1L);
        final ItemRequest itemRequest2 = addItemRequest(itemRequestRepository, requester2, 2L);
        final ItemRequest itemRequest3 = addItemRequest(itemRequestRepository, requester2, 3L);

        List<ItemRequest> itemRequestsWithoutRequester = itemRequestRepository
                .findAllByRequesterIdIsNotLike(requester.getId(), PageRequest.of(0, 10))
                .toList();

        List<ItemRequest> itemRequestsWithoutRequester2 = itemRequestRepository
                .findAllByRequesterIdIsNotLike(requester2.getId(), PageRequest.of(0, 10))
                .toList();

        assertNotNull(itemRequestsWithoutRequester);
        assertEquals(2, itemRequestsWithoutRequester.size());
        assertItemRequestEquals(itemRequest2, itemRequestsWithoutRequester.get(0));
        assertItemRequestEquals(itemRequest3, itemRequestsWithoutRequester.get(1));

        assertNotNull(itemRequestsWithoutRequester2);
        assertEquals(1, itemRequestsWithoutRequester2.size());
        assertItemRequestEquals(itemRequest, itemRequestsWithoutRequester2.get(0));
    }

    private static void assertItemRequestEquals(ItemRequest itemRequest1, ItemRequest itemRequest2) {
        assertEquals(itemRequest1.getId(),            itemRequest2.getId());
        assertEquals(itemRequest1.getDescription(),   itemRequest2.getDescription());
        assertEquals(itemRequest1.getCreationTime(),  itemRequest2.getCreationTime());
        assertUserEquals(itemRequest1.getRequester(), itemRequest2.getRequester());
    }

    private static void assertUserEquals(User requester1, User requester2) {
        assertEquals(requester1.getId(),    requester2.getId());
        assertEquals(requester1.getName(),  requester2.getName());
        assertEquals(requester1.getEmail(), requester2.getEmail());
    }

    private static ItemRequest addItemRequest(ItemRequestRepository itemRequestRepository, User requester, long n) {
        final String        itemRequestDescription = String.format("Item request description %d", n);
        final LocalDateTime time                   = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);

        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setDescription(itemRequestDescription);
        itemRequest.setCreationTime(time);
        itemRequest.setRequester(requester);

        return itemRequestRepository.save(itemRequest);
    }

    private static User addRequester(UserRepository userRepository, long n) {
        final String requesterName  = String.format("RequesterName%d", n);
        final String requesterEmail = String.format("requester%d@example.org", n);

        User user = new User();

        user.setName(requesterName);
        user.setEmail(requesterEmail);

        return userRepository.save(user);
    }
}