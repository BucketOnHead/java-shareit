package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAllByBookerIdOrderByStartTimeDesc(
            Long bookerId, Pageable page);

    Page<Booking> findAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
            Long bookerId, LocalDateTime time1, LocalDateTime time2, Pageable page);

    Page<Booking> findAllByBookerIdAndEndTimeIsBefore(
            Long bookerId, LocalDateTime time, Pageable page);

    Page<Booking> findAllByBookerIdAndStartTimeIsAfterOrderByStartTimeDesc(
            Long bookerId, LocalDateTime time, Pageable page);

    Page<Booking> findAllByBookerIdAndStatusEquals(
            Long bookerId, Booking.Status status, Pageable page);

    Page<Booking> findAllByItemOwnerIdOrderByStartTimeDesc(
            Long ownerId, Pageable page);

    Page<Booking> findAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
            Long ownerId, LocalDateTime time1, LocalDateTime time2, Pageable page);

    Page<Booking> findAllByItemOwnerIdAndEndTimeIsBefore(
            Long ownerId, LocalDateTime time, Pageable page);

    Page<Booking> findAllByItemOwnerIdAndStartTimeIsAfterOrderByStartTimeDesc(
            Long ownerId, LocalDateTime time, Pageable page);

    Page<Booking> findAllByItemOwnerIdAndStatusEquals(
            Long ownerId, Booking.Status status, Pageable page);

    Optional<Booking> findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc(
            Long itemId, LocalDateTime time);

    Optional<Booking> findFirstByItemIdAndStartTimeIsAfter(
            Long itemId, LocalDateTime time);

    boolean existsByBookerIdAndItemIdAndEndTimeIsBefore(
            Long bookerId, Long itemId, LocalDateTime time);

    default void validateBookingExistsById(Long bookingId) {
        if (!existsById(bookingId)) {
            throw BookingNotFoundException.getFromBookingId(bookingId);
        }
    }
}
