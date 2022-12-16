package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartTimeDesc(
            Long bookerId);

    Page<Booking> findAllByBookerIdOrderByStartTimeDesc(
            Long bookerId, Pageable page);

    List<Booking> findAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
            Long bookerId, LocalDateTime time1, LocalDateTime time2);

    Page<Booking> findAllByBookerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
            Long bookerId, LocalDateTime time1, LocalDateTime time2, Pageable page);

    List<Booking> findAllByBookerIdAndEndTimeIsBefore(
            Long bookerId, LocalDateTime time);

    Page<Booking> findAllByBookerIdAndEndTimeIsBefore(
            Long bookerId, LocalDateTime time, Pageable page);

    List<Booking> findAllByBookerIdAndStartTimeIsAfterOrderByStartTimeDesc(
            Long bookerId, LocalDateTime time);

    Page<Booking> findAllByBookerIdAndStartTimeIsAfterOrderByStartTimeDesc(
            Long bookerId, LocalDateTime time, Pageable page);

    List<Booking> findAllByBookerIdAndStatusEquals(
            Long bookerId, Booking.Status status);

    Page<Booking> findAllByBookerIdAndStatusEquals(
            Long bookerId, Booking.Status status, Pageable page);

    List<Booking> findAllByItemOwnerIdOrderByStartTimeDesc(
            Long ownerId);

    Page<Booking> findAllByItemOwnerIdOrderByStartTimeDesc(
            Long ownerId, Pageable page);

    List<Booking> findAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
            Long ownerId, LocalDateTime time1, LocalDateTime time2);

    Page<Booking> findAllByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
            Long ownerId, LocalDateTime time1, LocalDateTime time2, Pageable page);

    List<Booking> findAllByItemOwnerIdAndEndTimeIsBefore(
            Long ownerId, LocalDateTime time);

    Page<Booking> findAllByItemOwnerIdAndEndTimeIsBefore(
            Long ownerId, LocalDateTime time, Pageable page);

    List<Booking> findAllByItemOwnerIdAndStartTimeIsAfterOrderByStartTimeDesc(
            Long ownerId, LocalDateTime time);

    Page<Booking> findAllByItemOwnerIdAndStartTimeIsAfterOrderByStartTimeDesc(
            Long ownerId, LocalDateTime time, Pageable page);

    List<Booking> findAllByItemOwnerIdAndStatusEquals(
            Long ownerId, Booking.Status status);

    Page<Booking> findAllByItemOwnerIdAndStatusEquals(
            Long ownerId, Booking.Status status, Pageable page);

    Optional<Booking> findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc(
            Long itemId, LocalDateTime time);

    Optional<Booking> findFirstByItemIdAndStartTimeIsAfter(
            Long itemId, LocalDateTime time);

    boolean existsByBookerIdAndItemIdAndEndTimeIsBefore(
            Long bookerId, Long itemId, LocalDateTime time);
}
