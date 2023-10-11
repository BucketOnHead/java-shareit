package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Pageable LIMIT_1 = PageRequest.of(0, 1);

    Page<Booking> findAllByBookerId(Long bookerId, Pageable page);

    Page<Booking> findAllByBookerIdAndStartTimeBeforeAndEndTimeAfter(
            Long bookerId, LocalDateTime time1, LocalDateTime time2, Pageable page);

    Page<Booking> findAllByBookerIdAndEndTimeBefore(Long bookerId, LocalDateTime time, Pageable page);

    Page<Booking> findAllByBookerIdAndStartTimeAfter(Long bookerId, LocalDateTime time, Pageable page);

    Page<Booking> findAllByBookerIdAndStatus(Long bookerId, Booking.Status status, Pageable page);

    Page<Booking> findAllByItemOwnerId(Long ownerId, Pageable page);

    Page<Booking> findAllByItemOwnerIdAndStartTimeBeforeAndEndTimeAfter(
            Long ownerId, LocalDateTime time1, LocalDateTime time2, Pageable page);

    Page<Booking> findAllByItemOwnerIdAndEndTimeBefore(Long ownerId, LocalDateTime time, Pageable page);

    Page<Booking> findAllByItemOwnerIdAndStartTimeIsAfterOrderByStartTimeDesc(
            Long ownerId, LocalDateTime time, Pageable page);

    Page<Booking> findAllByItemOwnerIdAndStatus(Long ownerId, Booking.Status status, Pageable page);

    boolean existsByBookerIdAndItemIdAndStatusAndEndTimeBefore(Long itemId, Long bookerId, Booking.Status status,
                                                               LocalDateTime time);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.startTime < :time " +
            "AND (b.status IS NULL OR b.status = :status) " +
            "ORDER BY b.startTime DESC")
    Page<Booking> findAllLastBookingByTime(@Param("itemId") Long itemId,
                                           @Param("status") Booking.Status status,
                                           @Param("time") LocalDateTime time,
                                           Pageable page);

    default Optional<Booking> findLastBookingByTime(Long itemId, Booking.Status status, LocalDateTime time) {
        return findAllLastBookingByTime(itemId, status, time, LIMIT_1)
                .stream()
                .findFirst();
    }

    default Map<Long, Optional<Booking>> findAllLastBookingByTime(Iterable<Long> ids,
                                                                  Booking.Status status,
                                                                  LocalDateTime time) {
        return StreamSupport.stream(ids.spliterator(), false)
                .collect(Collectors.toMap(
                        Function.identity(),
                        id -> findLastBookingByTime(id, status, time)));
    }

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.startTime > :time " +
            "AND (b.status IS NULL OR b.status = :status) " +
            "ORDER BY b.startTime ASC")
    Page<Booking> findAllNextBookingByTime(@Param("itemId") Long itemId,
                                           @Param("status") Booking.Status status,
                                           @Param("time") LocalDateTime time,
                                           Pageable page);

    default Optional<Booking> findNextBookingByTime(Long itemId, Booking.Status status, LocalDateTime time) {
        return findAllNextBookingByTime(itemId, status, time, LIMIT_1)
                .stream()
                .findFirst();
    }

    default Map<Long, Optional<Booking>> findAllNextBookingByTime(Iterable<Long> ids,
                                                                  Booking.Status status,
                                                                  LocalDateTime time) {
        return StreamSupport.stream(ids.spliterator(), false)
                .collect(Collectors.toMap(
                        Function.identity(),
                        id -> findNextBookingByTime(id, status, time)));
    }

    default void validateBookingExistsById(Long bookingId) {
        if (!existsById(bookingId)) {
            throw new BookingNotFoundException(bookingId);
        }
    }
}
