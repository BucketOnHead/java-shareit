package ru.practicum.shareit.item.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.request.comment.CommentRequestDto;
import ru.practicum.shareit.item.dto.response.comment.SimpleCommentResponseDto;
import ru.practicum.shareit.item.exception.comment.IncorrectCommentException;
import ru.practicum.shareit.item.logger.comment.CommentServiceLoggerHelper;
import ru.practicum.shareit.item.mapper.comment.CommentDtoMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.comment.CommentRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public SimpleCommentResponseDto addComment(CommentRequestDto commentRequestDto, Long authorUserId, Long itemId) {
        userRepository.validateUserExistsById(authorUserId);
        itemRepository.validateItemExistsById(itemId);
        checkUserBookingByUserIdAndItemId(authorUserId, itemId);

        Comment comment = getComment(commentRequestDto, authorUserId, itemId);
        Comment savedComment = commentRepository.save(comment);

        CommentServiceLoggerHelper.commentSaved(log, savedComment);
        return CommentDtoMapper.toSimpleCommentResponseDto(savedComment);
    }

    private void checkUserBookingByUserIdAndItemId(Long userId, Long itemId) {
        LocalDateTime time = LocalDateTime.now();
        if (!bookingRepository.existsByBookerIdAndItemIdAndEndTimeIsBefore(userId, itemId, time)) {
            throw IncorrectCommentException.fromItemIdAndUserIdAndTime(itemId, userId, time);
        }
    }

    private Comment getComment(CommentRequestDto commentRequestDto, Long authorId, Long itemId) {
        return CommentDtoMapper.toComment(
                commentRequestDto,
                userRepository.getReferenceById(authorId),
                itemRepository.getReferenceById(itemId),
                LocalDateTime.now());
    }
}
