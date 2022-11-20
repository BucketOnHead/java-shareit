package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class Booking {
    public enum Status {
        WAITING, APPROVED, REJECTED, CANCELED
    }

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private User booker;
    private Status status;
}
