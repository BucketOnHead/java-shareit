package ru.practicum.server.client.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.shareit.server.constants.HttpHeaderConstants;
import ru.practicum.shareit.server.dto.booking.request.BookingCreationDto;
import ru.practicum.shareit.server.dto.booking.response.BookingDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingClient {
    private final WebClient client;

    public BookingDto addBooking(BookingCreationDto bookingDto, Long userId) {
        return client.post()
                .uri("/bookings")
                .bodyValue(bookingDto)
                .header(HttpHeaderConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToMono(BookingDto.class)
                .block();
    }

    public BookingDto updateBookingStatus(Long bookingId, Boolean approved, Long userId) {
        return client.patch()
                .uri(builder -> builder.path("/bookings/{id}")
                        .queryParam("approved", approved)
                        .build(bookingId))
                .header(HttpHeaderConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToMono(BookingDto.class)
                .block();
    }

    public BookingDto getBookingById(Long bookingId, Long userId) {
        return client.get()
                .uri("/bookings/{id}", bookingId)
                .header(HttpHeaderConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToMono(BookingDto.class)
                .block();
    }

    public List<BookingDto> getAllByBookerId(Long userId, String state, Integer from, Integer size) {
        return client.get()
                .uri(builder -> builder.path("/bookings")
                        .queryParam("state", state)
                        .queryParam("from", from)
                        .queryParam("size", size)
                        .build())
                .header(HttpHeaderConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToFlux(BookingDto.class)
                .collectList()
                .block();

    }

    public List<BookingDto> getAllByBookerItems(Long userId, String state, Integer from, Integer size) {
        return client.get()
                .uri(builder -> builder.path("/bookings/owner")
                        .queryParam("state", state)
                        .queryParam("from", from)
                        .queryParam("size", size)
                        .build())
                .header(HttpHeaderConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToFlux(BookingDto.class)
                .collectList()
                .block();
    }
}
