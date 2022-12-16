package ru.practicum.shareit.booking.mapper;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.booking.dto.in.RequestBookingDto;
import ru.practicum.shareit.booking.dto.out.BookingDto;
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

    public static Booking toBooking(RequestBookingDto bookingDto, User booker, Item item) {
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

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();

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

    public static List<BookingDto> toBookingDto(Collection<Booking> bookings) {
        return bookings.stream()
                .map(BookingDtoMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    public static List<BookingDto> toBookingDto(Page<Booking> bookings) {
        return toBookingDto(bookings.toList());
    }

    private static BookingDto.ItemDto toItemDtoForBookingDto(Item item) {
        BookingDto.ItemDto itemDto = new BookingDto.ItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());

        return itemDto;
    }

    private static BookingDto.UserDto toUserDtoForBookingDto(User user) {
        BookingDto.UserDto userDto = new BookingDto.UserDto();

        userDto.setId(user.getId());

        return userDto;
    }
}
