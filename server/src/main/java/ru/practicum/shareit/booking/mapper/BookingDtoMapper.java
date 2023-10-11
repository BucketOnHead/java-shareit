package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.request.BookingCreationDto;
import ru.practicum.shareit.booking.dto.response.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "startTime", source = "bookingDto.start")
    @Mapping(target = "endTime", source = "bookingDto.end")
    Booking mapToBooking(BookingCreationDto bookingDto, User booker, Item item);

    @Mapping(target = "start", source = "booking.startTime")
    @Mapping(target = "end", source = "booking.endTime")
    BookingDto mapToBookingResponseDto(Booking booking);

    List<BookingDto> mapToBookingResponseDto(Iterable<Booking> bookings);

    BookingDto.UserDto mapToBookingResponseUserDto(User user);

    BookingDto.ItemDto mapToBookingResponseDto(Item item);
}
