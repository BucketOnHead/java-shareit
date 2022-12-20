package ru.practicum.shareit.request.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
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
    void testFindAllByRequesterId_Empty() {
        // test parameters
        final Long requesterId = 1L;

        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterId(requesterId);

        assertNotNull(itemRequests);
        assertTrue(itemRequests.isEmpty());
    }

    /**
     * Method under test: {@link ItemRequestRepository#findAllByRequesterId(Long)}
     */
    @Test
    void testFindAllByRequesterId_WithOne() {
        // test context
        final long        requesterNum     = 1L;
        final long        itemRequestNum   = 1L;
        final User        addedRequester   = addRequester(requesterNum);
        final ItemRequest addedItemRequest = addItemRequest(itemRequestNum, addedRequester);

        List<ItemRequest> itemRequests =
                itemRequestRepository.findAllByRequesterId(addedRequester.getId());

        assertNotNull(itemRequests);
        assertEquals(1, itemRequests.size());
        assertEquals(addedItemRequest.getId(),           itemRequests.get(0).getId());
        assertEquals(addedItemRequest.getDescription(),  itemRequests.get(0).getDescription());
        assertEquals(addedItemRequest.getCreationTime(), itemRequests.get(0).getCreationTime());
        assertEquals(addedRequester.getId(),    itemRequests.get(0).getRequester().getId());
        assertEquals(addedRequester.getName(),  itemRequests.get(0).getRequester().getName());
        assertEquals(addedRequester.getEmail(), itemRequests.get(0).getRequester().getEmail());
    }

    /**
     * Method under test: {@link ItemRequestRepository#findAllByRequesterId(Long)}
     */
    @Test
    void testFindAllByRequesterId_WithOneAndOther() {
        // test context
        final long        requesterNum      = 1L;
        final long        requesterNum2     = 2L;
        final long        itemRequestNum    = 1L;
        final long        itemRequestNum2   = 2L;
        final User        addedRequester    = addRequester(requesterNum);
        final User        addedRequester2   = addRequester(requesterNum2);
        final ItemRequest addedItemRequest  = addItemRequest(itemRequestNum, addedRequester);
        final ItemRequest addedItemRequest2 = addItemRequest(itemRequestNum2, addedRequester2);

        List<ItemRequest> itemRequests =
                itemRequestRepository.findAllByRequesterId(addedRequester.getId());

        assertNotNull(itemRequests);
        assertEquals(1, itemRequests.size());
        assertEquals(addedItemRequest.getId(),           itemRequests.get(0).getId());
        assertEquals(addedItemRequest.getDescription(),  itemRequests.get(0).getDescription());
        assertEquals(addedItemRequest.getCreationTime(), itemRequests.get(0).getCreationTime());
        assertEquals(addedRequester.getId(),    itemRequests.get(0).getRequester().getId());
        assertEquals(addedRequester.getName(),  itemRequests.get(0).getRequester().getName());
        assertEquals(addedRequester.getEmail(), itemRequests.get(0).getRequester().getEmail());
    }

    /**
     * Method under test: {@link ItemRequestRepository#findAllByRequesterIdIsNotLike(Long, Pageable)}
     */
    @Test
    void testFindAllByRequesterIdIsNotLike_Empty() {
        // test parameters
        final Pageable page        = PageRequest.of(0, 100);
        final Long     requesterId = 1L;

        Page<ItemRequest> itemRequestsPage =
                itemRequestRepository.findAllByRequesterIdIsNotLike(requesterId, page);

        assertNotNull(itemRequestsPage);

        List<ItemRequest> itemRequests = itemRequestsPage.toList();
        assertTrue(itemRequests.isEmpty());
    }

    /**
     * Method under test: {@link ItemRequestRepository#findAllByRequesterIdIsNotLike(Long, Pageable)}
     */
    @Test
    void testFindAllByRequesterIdIsNotLike_WithOneAndOther() {
        // test context
        final long        requesterNum      = 1L;
        final long        requesterNum2     = 2L;
        final long        itemRequestNum    = 1L;
        final long        itemRequestNum2   = 2L;
        final User        addedRequester    = addRequester(requesterNum);
        final User        addedRequester2   = addRequester(requesterNum2);
        final ItemRequest addedItemRequest  = addItemRequest(itemRequestNum, addedRequester);
        final ItemRequest addedItemRequest2 = addItemRequest(itemRequestNum2, addedRequester2);
        final Pageable    page              = PageRequest.of(0, 100);

        Page<ItemRequest> itemRequestsPage =
                itemRequestRepository.findAllByRequesterIdIsNotLike(addedRequester.getId(), page);

        assertNotNull(itemRequestsPage);

        List<ItemRequest> itemRequests = itemRequestsPage.toList();
        assertEquals(1, itemRequests.size());
        assertEquals(addedItemRequest2.getId(),           itemRequests.get(0).getId());
        assertEquals(addedItemRequest2.getDescription(),  itemRequests.get(0).getDescription());
        assertEquals(addedItemRequest2.getCreationTime(), itemRequests.get(0).getCreationTime());
        assertEquals(addedRequester2.getId(),    itemRequests.get(0).getRequester().getId());
        assertEquals(addedRequester2.getName(),  itemRequests.get(0).getRequester().getName());
        assertEquals(addedRequester2.getEmail(), itemRequests.get(0).getRequester().getEmail());
    }

    private static User getRequesterWithoutId(Long n) {
        final String name  = String.format("Requester%dName", n);
        final String email = String.format("user.requester%d", n);

        User user = new User();
        user.setName(name);
        user.setEmail(email);

        return user;
    }

    private static ItemRequest getItemRequestWithoutId(long n, User requester) {
        final String        description = String.format("Item request %d description", n);
        final LocalDateTime time        = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(description);
        itemRequest.setCreationTime(time);
        itemRequest.setRequester(requester);

        return itemRequest;
    }

    private ItemRequest addItemRequest(long itemRequestNum, User requester) {
        ItemRequest itemRequest = getItemRequestWithoutId(itemRequestNum, requester);
        return itemRequestRepository.save(itemRequest);
    }

    private User addRequester(Long requesterId) {
        User requester = getRequesterWithoutId(requesterId);
        return userRepository.save(requester);
    }
}
