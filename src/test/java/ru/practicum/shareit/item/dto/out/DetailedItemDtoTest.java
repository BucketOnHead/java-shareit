package ru.practicum.shareit.item.dto.out;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.out.DetailedItemDto.BookingDto;
import ru.practicum.shareit.item.dto.out.DetailedItemDto.CommentDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class DetailedItemDtoTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link DetailedItemDto}
     *   <li>{@link DetailedItemDto#setAvailable(Boolean)}
     *   <li>{@link DetailedItemDto#setComments(List)}
     *   <li>{@link DetailedItemDto#setDescription(String)}
     *   <li>{@link DetailedItemDto#setId(Long)}
     *   <li>{@link DetailedItemDto#setLastBooking(BookingDto)}
     *   <li>{@link DetailedItemDto#setName(String)}
     *   <li>{@link DetailedItemDto#setNextBooking(BookingDto)}
     *   <li>{@link DetailedItemDto#getAvailable()}
     *   <li>{@link DetailedItemDto#getComments()}
     *   <li>{@link DetailedItemDto#getDescription()}
     *   <li>{@link DetailedItemDto#getId()}
     *   <li>{@link DetailedItemDto#getLastBooking()}
     *   <li>{@link DetailedItemDto#getName()}
     *   <li>{@link DetailedItemDto#getNextBooking()}
     * </ul>
     */
    @Test
    void testConstructor() {
        // test parameters
        final Long id            = 1L;
        final Long bookingId     = 1L;
        final Long bookingId2    = 2L;
        final Long bookerId      = 1L;
        final Long bookerId2     = 2L;
        final String name        = "DetailedItemDtoName";
        final String description = "Detailed item dto description";
        final Boolean available  = Boolean.TRUE;
        // test context
        final BookingDto       lastBooking     = getBookingDto(bookingId, bookerId);
        final BookingDto       nextBooking     = getBookingDto(bookingId2, bookerId2);
        final List<CommentDto> comments        = new ArrayList<>();
        final DetailedItemDto  detailedItemDto = new DetailedItemDto();

        detailedItemDto.setId(id);
        detailedItemDto.setName(name);
        detailedItemDto.setDescription(description);
        detailedItemDto.setAvailable(available);
        detailedItemDto.setLastBooking(lastBooking);
        detailedItemDto.setNextBooking(nextBooking);
        detailedItemDto.setComments(comments);

        assertEquals(id,          detailedItemDto.getId());
        assertEquals(name,        detailedItemDto.getName());
        assertEquals(description, detailedItemDto.getDescription());
        assertEquals(available,   detailedItemDto.getAvailable());
        assertSame(lastBooking,   detailedItemDto.getLastBooking());
        assertSame(nextBooking,   detailedItemDto.getNextBooking());
        assertSame(comments,      detailedItemDto.getComments());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link BookingDto}
     *   <li>{@link BookingDto#setId(Long)}
     *   <li>{@link BookingDto#setBookerId(Long)}
     *   <li>{@link BookingDto#getId()}
     *   <li>{@link BookingDto#getBookerId()}
     * </ul>
     */
    @Test
    void testBookingDto() {
        // test parameters
        final Long id       = 1L;
        final Long bookerId = 10L;
        // test context
        final BookingDto bookingDto = new BookingDto();

        bookingDto.setId(id);
        bookingDto.setBookerId(bookerId);

        assertEquals(id,       bookingDto.getId());
        assertEquals(bookerId, bookingDto.getBookerId());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link CommentDto}
     *   <li>{@link CommentDto#setId(Long)}
     *   <li>{@link CommentDto#setAuthorName(String)}
     *   <li>{@link CommentDto#setText(String)}
     *   <li>{@link CommentDto#setCreated(LocalDateTime)}
     *   <li>{@link CommentDto#getId()}
     *   <li>{@link CommentDto#getAuthorName()}
     *   <li>{@link CommentDto#getText()}
     *   <li>{@link CommentDto#getCreated()}
     * </ul>
     */
    @Test
    void testCommentDto() {
        // test parameters
        final Long          id         = 1L;
        final String        authorName = "CommentDtoAuthorName";
        final String        text       = "Comment dto test";
        final LocalDateTime time       = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
        // test context
        final CommentDto commentDto = new CommentDto();

        commentDto.setId(id);
        commentDto.setAuthorName(authorName);
        commentDto.setText(text);
        commentDto.setCreated(time);

        assertEquals(id,         commentDto.getId());
        assertEquals(authorName, commentDto.getAuthorName());
        assertEquals(text,       commentDto.getText());
        assertEquals(time,       commentDto.getCreated());
    }

    private BookingDto getBookingDto(Long id, Long bookerId) {
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(id);
        bookingDto.setBookerId(bookerId);

        return bookingDto;
    }
}
