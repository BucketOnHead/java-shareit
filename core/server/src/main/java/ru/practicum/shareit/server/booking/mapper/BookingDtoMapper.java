package ru.practicum.shareit.server.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.dto.booking.request.BookingCreationDto;
import ru.practicum.shareit.server.dto.booking.response.BookingDto;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.model.User;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", nullValueMappingStrategy = RETURN_DEFAULT)
public interface BookingDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    Booking mapToBooking(BookingCreationDto bookingDto, User booker, Item item);

    BookingDto mapToBookingDto(Booking booking);

    List<BookingDto> mapToBookingDto(Iterable<Booking> bookings);

    BookingDto.UserDto mapToBookingUserDto(User user);

    BookingDto.ItemDto mapToBookingItemDto(Item item);
}
