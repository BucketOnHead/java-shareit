package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class BookingTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link Booking}
     *   <li>{@link Booking#setBooker(User)}
     *   <li>{@link Booking#setEndTime(LocalDateTime)}
     *   <li>{@link Booking#setId(Long)}
     *   <li>{@link Booking#setItem(Item)}
     *   <li>{@link Booking#setStartTime(LocalDateTime)}
     *   <li>{@link Booking#setStatus(Booking.Status)}
     *   <li>{@link Booking#getBooker()}
     *   <li>{@link Booking#getEndTime()}
     *   <li>{@link Booking#getId()}
     *   <li>{@link Booking#getItem()}
     *   <li>{@link Booking#getStartTime()}
     *   <li>{@link Booking#getStatus()}
     * </ul>
     */
    @Test
    void testConstructor() {
        Booking actualBooking = new Booking();
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");
        actualBooking.setBooker(user);
        LocalDateTime ofResult = LocalDateTime.of(1, 1, 1, 1, 1);
        actualBooking.setEndTime(ofResult);
        actualBooking.setId(123L);
        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("Name");
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user1);
        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");
        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("Name");
        item.setOwner(user2);
        actualBooking.setItem(item);
        LocalDateTime ofResult1 = LocalDateTime.of(1, 1, 1, 1, 1);
        actualBooking.setStartTime(ofResult1);
        actualBooking.setStatus(Booking.Status.WAITING);
        assertSame(user, actualBooking.getBooker());
        assertSame(ofResult, actualBooking.getEndTime());
        assertEquals(123L, actualBooking.getId().longValue());
        assertSame(item, actualBooking.getItem());
        assertSame(ofResult1, actualBooking.getStartTime());
        assertEquals(Booking.Status.WAITING, actualBooking.getStatus());
    }

    /**
     * Method under test: default or parameterless constructor of {@link Booking}
     */
    @Test
    void testConstructor2() {
        assertEquals(Booking.Status.WAITING, (new Booking()).getStatus());
    }
}
