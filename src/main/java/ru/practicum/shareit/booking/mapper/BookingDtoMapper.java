package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.in.BookingCreationRequestDto;
import ru.practicum.shareit.booking.dto.out.BookingDto;
import ru.practicum.shareit.booking.dto.out.ShortBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookingDtoMapper {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    // ╔══╗───╔═══╗───╔══╗───╔╗──╔╗──────╔══╗────╔════╗───╔══╗
    // ║╔═╝───║╔═╗║───║╔╗║───║║──║║──────║╔╗╚╗───╚═╗╔═╝───║╔╗║
    // ║╚═╗───║╚═╝║───║║║║───║╚╗╔╝║──────║║╚╗║─────║║─────║║║║
    // ║╔═╝───║╔╗╔╝───║║║║───║╔╗╔╗║──────║║─║║─────║║─────║║║║
    // ║║─────║║║║────║╚╝║───║║╚╝║║──────║╚═╝║─────║║─────║╚╝║
    // ╚╝─────╚╝╚╝────╚══╝───╚╝──╚╝──────╚═══╝─────╚╝─────╚══╝

    public Booking toBooking(BookingCreationRequestDto bookingDto, Long userId) {
        Booking booking = new Booking();

        User booker = userRepository.getReferenceById(userId);
        Item item = itemRepository.getReferenceById(bookingDto.getItemId());

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

    public BookingDto toBookingDto(Booking booking,
                                   UserDtoMapper userDtoMapper,
                                   ItemDtoMapper itemDtoMapper) {
        BookingDto bookingDto = new BookingDto();

        bookingDto.setStart(booking.getStartTime());
        bookingDto.setEnd(booking.getEndTime());
        bookingDto.setId(booking.getId());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setBooker(userDtoMapper.toShortUserDto(booking.getBooker()));
        bookingDto.setItem(itemDtoMapper.toShortItemDto(booking.getItem()));

        return bookingDto;
    }

    public ShortBookingDto toShortBookingDto(Booking booking) {
        ShortBookingDto shortBookingDto = new ShortBookingDto();

        shortBookingDto.setId(booking.getId());
        shortBookingDto.setBookerId(booking.getBooker().getId());

        return shortBookingDto;
    }

    public List<BookingDto> toBookingDto(Collection<Booking> bookings,
                                         UserDtoMapper userDtoMapper,
                                         ItemDtoMapper itemDtoMapper) {
        return bookings.stream()
                .map(booking -> toBookingDto(booking, userDtoMapper, itemDtoMapper))
                .collect(Collectors.toList());
    }
}
