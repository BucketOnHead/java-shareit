package ru.practicum.shareit.booking.mapper;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.response.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class BookingDtoMapper {
    private BookingDtoMapper() {
        throw new AssertionError("This is a utility class and cannot be instantiated");
    }

    // ╔══╗───╔═══╗───╔══╗───╔╗──╔╗──────╔══╗────╔════╗───╔══╗
    // ║╔═╝───║╔═╗║───║╔╗║───║║──║║──────║╔╗╚╗───╚═╗╔═╝───║╔╗║
    // ║╚═╗───║╚═╝║───║║║║───║╚╗╔╝║──────║║╚╗║─────║║─────║║║║
    // ║╔═╝───║╔╗╔╝───║║║║───║╔╗╔╗║──────║║─║║─────║║─────║║║║
    // ║║─────║║║║────║╚╝║───║║╚╝║║──────║╚═╝║─────║║─────║╚╝║
    // ╚╝─────╚╝╚╝────╚══╝───╚╝──╚╝──────╚═══╝─────╚╝─────╚══╝

    public static Booking toBooking(BookItemRequestDto bookingDto, User booker, Item item) {
        Booking booking = new Booking();

        booking.setStartTime(bookingDto.getStart());
        booking.setEndTime(bookingDto.getEnd());
        booking.setBooker(booker);
        booking.setItem(item);

        return booking;
    }

    // ╔════╗───╔══╗──────╔══╗────╔════╗───╔══╗
    // ╚═╗╔═╝───║╔╗║──────║╔╗╚╗───╚═╗╔═╝───║╔╗║
    // ──║║─────║║║║──────║║╚╗║─────║║─────║║║║
    // ──║║─────║║║║──────║║─║║─────║║─────║║║║
    // ──║║─────║╚╝║──────║╚═╝║─────║║─────║╚╝║
    // ──╚╝─────╚══╝──────╚═══╝─────╚╝─────╚══╝

    public static BookingResponseDto toBookingDto(Booking booking) {
        BookingResponseDto bookingDto = new BookingResponseDto();

        bookingDto.setStart(booking.getStartTime());
        bookingDto.setEnd(booking.getEndTime());
        bookingDto.setId(booking.getId());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setBooker(toUserDtoForBookingDto(booking.getBooker()));
        bookingDto.setItem(toItemDtoForBookingDto(booking.getItem()));

        return bookingDto;
    }

    // ╔════╗───╔══╗──────╔══╗────╔════╗───╔══╗──────╔╗─────╔══╗───╔══╗───╔════╗
    // ╚═╗╔═╝───║╔╗║──────║╔╗╚╗───╚═╗╔═╝───║╔╗║──────║║─────╚╗╔╝───║╔═╝───╚═╗╔═╝
    // ──║║─────║║║║──────║║╚╗║─────║║─────║║║║──────║║──────║║────║╚═╗─────║║──
    // ──║║─────║║║║──────║║─║║─────║║─────║║║║──────║║──────║║────╚═╗║─────║║──
    // ──║║─────║╚╝║──────║╚═╝║─────║║─────║╚╝║──────║╚═╗───╔╝╚╗───╔═╝║─────║║──
    // ──╚╝─────╚══╝──────╚═══╝─────╚╝─────╚══╝──────╚══╝───╚══╝───╚══╝─────╚╝──

    public static List<BookingResponseDto> toBookingDto(Collection<Booking> bookings) {
        return bookings.stream()
                .map(BookingDtoMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    public static List<BookingResponseDto> toBookingDto(Page<Booking> bookings) {
        return toBookingDto(bookings.toList());
    }

    private static BookingResponseDto.ItemDto toItemDtoForBookingDto(Item item) {
        BookingResponseDto.ItemDto itemDto = new BookingResponseDto.ItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());

        return itemDto;
    }

    private static BookingResponseDto.UserDto toUserDtoForBookingDto(User user) {
        BookingResponseDto.UserDto userDto = new BookingResponseDto.UserDto();

        userDto.setId(user.getId());

        return userDto;
    }
}
