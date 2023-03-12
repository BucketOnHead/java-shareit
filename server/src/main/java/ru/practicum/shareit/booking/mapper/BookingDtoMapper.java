package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.response.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides methods for mapping booking
 * related DTOs to corresponding entities and vice versa.
 */
@UtilityClass
public final class BookingDtoMapper {

    /**
     * Maps a {@link BookItemRequestDto} and related entities to a {@link Booking} entity.
     *
     * @param bookingDto the DTO containing booking information.
     * @param booker     the user who made the booking.
     * @param item       the item being booked.
     * @return the mapped {@link Booking} entity.
     */
    public static Booking toBooking(BookItemRequestDto bookingDto, User booker, Item item) {
        Booking booking = new Booking();

        booking.setStartTime(bookingDto.getStart());
        booking.setEndTime(bookingDto.getEnd());
        booking.setBooker(booker);
        booking.setItem(item);

        return booking;
    }

    /**
     * Maps a {@link Booking} entity to a {@link BookingResponseDto} DTO.
     *
     * @param booking the {@link Booking} entity to be mapped.
     * @return the mapped {@link BookingResponseDto} DTO.
     */
    public static BookingResponseDto toBookingDto(Booking booking) {
        BookingResponseDto bookingDto = new BookingResponseDto();

        bookingDto.setStart(booking.getStartTime());
        bookingDto.setEnd(booking.getEndTime());
        bookingDto.setId(booking.getId());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setBooker(BookingResponseDto.UserDto.fromUser(booking.getBooker()));
        bookingDto.setItem(BookingResponseDto.ItemDto.fromItem(booking.getItem()));

        return bookingDto;
    }

    /**
     * Maps an iterable collection of {@link Booking}
     * entities to a list of {@link BookingResponseDto} DTOs.
     *
     * @param bookings the iterable collection of {@link Booking}
     *                 entities to be mapped.
     * @return the list of mapped {@link BookingResponseDto} DTOs.
     */
    public static List<BookingResponseDto> toBookingDto(Iterable<Booking> bookings) {
        var bookingDtos = new ArrayList<BookingResponseDto>();

        for (Booking booking : bookings) {
            var bookingDto = toBookingDto(booking);
            bookingDtos.add(bookingDto);
        }

        return bookingDtos;
    }
}
