package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingByBookerIdOrderByStartTimeDesc(
            Long bookerId);

    List<Booking> findBookingByBookerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
            Long bookerId, LocalDateTime time1, LocalDateTime time2);

    List<Booking> findBookingsByBookerIdAndEndTimeIsBefore(
            Long bookerId, LocalDateTime time);

    List<Booking> findBookingsByBookerIdAndStartTimeIsAfterOrderByStartTimeDesc(
            Long bookerId, LocalDateTime time);

    List<Booking> findBookingsByBookerIdAndStatusEquals(
            Long bookerId, Booking.Status status);

    List<Booking> findBookingsByItemOwnerIdOrderByStartTimeDesc(
            Long ownerId);

    List<Booking> findBookingsByItemOwnerIdAndStartTimeIsBeforeAndEndTimeIsAfter(
            Long ownerId, LocalDateTime time1, LocalDateTime time2);

    List<Booking> findBookingsByItemOwnerIdAndEndTimeIsBefore(
            Long ownerId, LocalDateTime time);

    List<Booking> findBookingsByItemOwnerIdAndStartTimeIsAfterOrderByStartTimeDesc(
            Long ownerId, LocalDateTime time);

    List<Booking> findBookingsByItemOwnerIdAndStatusEquals(
            Long ownerId, Booking.Status status);

    Optional<Booking> findFirstByItemIdAndEndTimeIsBeforeOrderByEndTimeDesc(
            Long itemId, LocalDateTime time);

    Optional<Booking> findFirstByItemIdAndStartTimeIsAfter(
            Long itemId, LocalDateTime time);

    boolean existsByBookerIdAndItemIdAndEndTimeIsBefore(
            Long bookerId, Long itemId, LocalDateTime time);
}
