package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.commons.constants.booking.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /**
     * Retrieve the latest completed bookings based
     * on specified parameters, searching across all
     * statuses if "status" is null.
     */
    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.start < :time " +
            "AND (b.status IS NULL OR b.status = :status) " +
            "ORDER BY b.start DESC")
    Page<Booking> findItemLastBookings(Long itemId, BookingStatus status, LocalDateTime time, Pageable page);

    /**
     * Retrieve the upcoming bookings based on specified
     * parameters, searching across all statuses if
     * 'status' is null.
     */
    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.start > :time " +
            "AND (b.status IS NULL OR b.status = :status) " +
            "ORDER BY b.start ASC")
    Page<Booking> findItemNextBooking(Long itemId, BookingStatus status, LocalDateTime time, Pageable page);

    /**
     * Retrieve the nearest past booking based on specified parameters,
     * searching across all statuses if 'status' is null.
     * Utilizes {@link #findItemLastBookings(Long, BookingStatus, LocalDateTime, Pageable)}
     */
    default Optional<Booking> findItemLastBooking(Long itemId, BookingStatus status, LocalDateTime time) {
        return findItemLastBookings(itemId, status, time, LIMIT_1).get().findFirst();
    }

    default List<Booking> findItemsLastBooking(Collection<Long> itemIds, BookingStatus status, LocalDateTime time) {
        return itemIds.stream()
                .map(id -> findItemLastBooking(id, status, time))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve the nearest past booking based on specified parameters,
     * searching across all statuses if 'status' is null.
     * Utilizes {@link #findItemNextBooking(Long, BookingStatus, LocalDateTime, Pageable)}
     */
    default Optional<Booking> findItemNextBooking(Long itemId, BookingStatus status, LocalDateTime time) {
        return findItemNextBooking(itemId, status, time, LIMIT_1)
                .stream()
                .findFirst();
    }

    default List<Booking> findItemsNextBooking(Collection<Long> itemIds, BookingStatus status, LocalDateTime time) {
        return itemIds.stream()
                .map(id -> findItemNextBooking(id, status, time))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    default Booking findByIdOrThrow(Long bookingId) {
        var optionalBooking = findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new BookingNotFoundException(bookingId);
        }

        return optionalBooking.get();
    }
}
