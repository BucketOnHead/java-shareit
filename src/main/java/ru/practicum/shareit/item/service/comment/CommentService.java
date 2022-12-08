package ru.practicum.shareit.item.service.comment;

import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.in.comment.RequestCommentDto;
import ru.practicum.shareit.item.dto.out.comment.CommentDto;
import ru.practicum.shareit.item.exception.comment.IncorrectCommentException;

import java.time.LocalDateTime;

public interface CommentService {
    static void checkUserBookingByUserIdAndItemId(BookingRepository bookingRepository,
                                                  Long userId, Long itemId,
                                                  LocalDateTime time) {
        if (!bookingRepository.existsByBookerIdAndItemIdAndEndTimeIsBefore(userId, itemId, time)) {
            throw IncorrectCommentException.getFromUserIdAndItemIdAndTime(userId, itemId, time);
        }
    }

    CommentDto addComment(RequestCommentDto comment, Long itemId, Long userId);
}
