package ru.practicum.shareit.booking.dto.out;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.out.BookingDto.ItemDto;
import ru.practicum.shareit.booking.dto.out.BookingDto.UserDto;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class BookingDtoTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link BookingDto}
     *   <li>{@link BookingDto#setBooker(UserDto)}
     *   <li>{@link BookingDto#setEnd(LocalDateTime)}
     *   <li>{@link BookingDto#setId(Long)}
     *   <li>{@link BookingDto#setItem(ItemDto)}
     *   <li>{@link BookingDto#setStart(LocalDateTime)}
     *   <li>{@link BookingDto#setStatus(Booking.Status)}
     *   <li>{@link BookingDto#getBooker()}
     *   <li>{@link BookingDto#getEnd()}
     *   <li>{@link BookingDto#getId()}
     *   <li>{@link BookingDto#getItem()}
     *   <li>{@link BookingDto#getStart()}
     *   <li>{@link BookingDto#getStatus()}
     * </ul>
     */
    @Test
    void testConstructor() {
        BookingDto actualBookingDto = new BookingDto();
        UserDto userDto = new UserDto();
        userDto.setId(123L);
        actualBookingDto.setBooker(userDto);
        LocalDateTime ofResult = LocalDateTime.of(1, 1, 1, 1, 1);
        actualBookingDto.setEnd(ofResult);
        actualBookingDto.setId(123L);
        ItemDto itemDto = new ItemDto();
        itemDto.setId(123L);
        itemDto.setName("Name");
        actualBookingDto.setItem(itemDto);
        LocalDateTime ofResult1 = LocalDateTime.of(1, 1, 1, 1, 1);
        actualBookingDto.setStart(ofResult1);
        actualBookingDto.setStatus(Booking.Status.WAITING);
        assertSame(userDto, actualBookingDto.getBooker());
        assertSame(ofResult, actualBookingDto.getEnd());
        assertEquals(123L, actualBookingDto.getId().longValue());
        assertSame(itemDto, actualBookingDto.getItem());
        assertSame(ofResult1, actualBookingDto.getStart());
        assertEquals(Booking.Status.WAITING, actualBookingDto.getStatus());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link ItemDto}
     *   <li>{@link ItemDto#setId(Long)}
     *   <li>{@link ItemDto#setName(String)}
     *   <li>{@link ItemDto#getId()}
     *   <li>{@link ItemDto#getName()}
     * </ul>
     */
    @Test
    void testItemDto() {
        // test parameters
        final Long   id   = 1L;
        final String name = "ItemDtoName";
        // test context
        final ItemDto itemDto = new ItemDto();

        itemDto.setId(id);
        itemDto.setName(name);

        assertEquals(id, itemDto.getId());
        assertEquals(name, itemDto.getName());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link UserDto}
     *   <li>{@link UserDto#setId(Long)}
     *   <li>{@link UserDto#getId()}
     * </ul>
     */
    @Test
    void testUserDto() {
        // test parameters
        final Long id = 1L;
        // test context
        final UserDto userDto = new UserDto();

        userDto.setId(id);

        assertEquals(id, userDto.getId());
    }
}
