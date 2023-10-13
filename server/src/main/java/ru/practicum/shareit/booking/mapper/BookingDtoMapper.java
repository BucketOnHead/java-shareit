package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.request.BookingCreationDto;
import ru.practicum.shareit.booking.dto.response.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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
