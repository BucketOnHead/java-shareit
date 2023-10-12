package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Pageable LIMIT_1 = PageRequest.of(0, 1);

    Page<Booking> findAllByBookerId(Long bookerId, Pageable page);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime time1, LocalDateTime time2,
                                                             Pageable page);

    Page<Booking> findAllByBookerIdAndEndBefore(Long bookerId, LocalDateTime time, Pageable page);

    Page<Booking> findAllByBookerIdAndStartAfter(Long bookerId, LocalDateTime time, Pageable page);

    Page<Booking> findAllByBookerIdAndStatus(Long bookerId, BookingStatus status, Pageable page);

    Page<Booking> findAllByItemOwnerId(Long ownerId, Pageable page);

    Page<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(Long ownerId, LocalDateTime time1, LocalDateTime time2,
                                                                Pageable page);

    Page<Booking> findAllByItemOwnerIdAndEndBefore(Long ownerId, LocalDateTime time, Pageable page);

    Page<Booking> findAllByItemOwnerIdAndStartIsAfter(Long ownerId, LocalDateTime time, Pageable page);

    Page<Booking> findAllByItemOwnerIdAndStatus(Long ownerId, BookingStatus status, Pageable page);

    boolean existsByBookerIdAndItemIdAndStatusAndEndBefore(Long itemId, Long bookerId, BookingStatus status,
                                                           LocalDateTime time);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.start < :time " +
            "AND (b.status IS NULL OR b.status = :status) " +
            "ORDER BY b.start DESC")
    Page<Booking> findAllLastBookingByTime(Long itemId, BookingStatus status, LocalDateTime time, Pageable page);

    default Optional<Booking> findLastBookingByTime(Long itemId, BookingStatus status, LocalDateTime time) {
        return findAllLastBookingByTime(itemId, status, time, LIMIT_1)
                .stream()
                .findFirst();
    }

    default Map<Long, Optional<Booking>> findAllLastBookingByTime(Iterable<Long> ids, BookingStatus status,
                                                                  LocalDateTime time) {
        return StreamSupport.stream(ids.spliterator(), false)
                .collect(Collectors.toMap(
                        Function.identity(),
                        id -> findLastBookingByTime(id, status, time)));
    }

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.start > :time " +
            "AND (b.status IS NULL OR b.status = :status) " +
            "ORDER BY b.start ASC")
    Page<Booking> findAllNextBookingByTime(Long itemId, BookingStatus status, LocalDateTime time, Pageable page);

    default Optional<Booking> findNextBookingByTime(Long itemId, BookingStatus status, LocalDateTime time) {
        return findAllNextBookingByTime(itemId, status, time, LIMIT_1)
                .stream()
                .findFirst();
    }

    default Map<Long, Optional<Booking>> findAllNextBookingByTime(Iterable<Long> ids, BookingStatus status,
                                                                  LocalDateTime time) {
        return StreamSupport.stream(ids.spliterator(), false)
                .collect(Collectors.toMap(
                        Function.identity(),
                        id -> findNextBookingByTime(id, status, time)));
    }

    default Booking findByIdOrThrow(Long bookingId) {
        var optionalBooking = findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new BookingNotFoundException(bookingId);
        }

        return optionalBooking.get();
    }
}
