package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
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

    boolean existsByBookerIdAndItemIdAndEndTimeIsBefore(
            Long bookerId, Long itemId, LocalDateTime time);

    Optional<Booking> findTopByItemIdAndStartTimeLessThanAndStatus(Long id, LocalDateTime now,
                                                                   Booking.Status status, Sort sort);

    Optional<Booking> findTopByItemIdAndStartTimeGreaterThanEqualAndStatus(Long id, LocalDateTime now,
                                                                           Booking.Status status, Sort sort);

    default void validateBookingExistsById(Long bookingId) {
        if (!existsById(bookingId)) {
            throw BookingNotFoundException.getFromBookingId(bookingId);
        }
    }
}
