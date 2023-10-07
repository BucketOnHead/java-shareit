package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.request.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.response.BookingResponseDto;
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
    Booking mapToBooking(BookItemRequestDto bookingDto, User booker, Item item);

    @Mapping(target = "start", source = "booking.startTime")
    @Mapping(target = "end", source = "booking.endTime")
    BookingResponseDto mapToBookingResponseDto(Booking booking);
    List<BookingResponseDto> mapToBookingResponseDto(Iterable<Booking> bookings);

    BookingResponseDto.UserDto mapToBookingResponseUserDto(User user);
    BookingResponseDto.ItemDto mapToBookingResponseDto(Item item);
}
