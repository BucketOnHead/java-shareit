package ru.practicum.shareit.item.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.request.comment.CommentCreationDto;
import ru.practicum.shareit.item.dto.response.comment.SimpleCommentResponseDto;
import ru.practicum.shareit.item.exception.comment.IncorrectCommentException;
import ru.practicum.shareit.item.logger.comment.CommentServiceLoggerHelper;
import ru.practicum.shareit.item.mapper.CommentDtoMapper;
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
    private final CommentDtoMapper commentMapper;

    @Override
    @Transactional
    public SimpleCommentResponseDto addComment(CommentCreationDto commentCreationDto, Long authorUserId, Long itemId) {
        userRepository.existsByIdOrThrow(authorUserId);
        itemRepository.validateItemExistsById(itemId);
        checkUserBookingByUserIdAndItemId(authorUserId, itemId);

        Comment comment = getComment(commentCreationDto, authorUserId, itemId);
        Comment savedComment = commentRepository.save(comment);

        CommentServiceLoggerHelper.commentSaved(log, savedComment);
        return commentMapper.mapToSimpleCommentResponseDto(savedComment);
    }

    private void checkUserBookingByUserIdAndItemId(Long userId, Long itemId) {
        LocalDateTime time = LocalDateTime.now();
        if (!bookingRepository.existsByBookerIdAndItemIdAndEndTimeIsBefore(userId, itemId, time)) {
            throw IncorrectCommentException.fromItemIdAndUserIdAndTime(itemId, userId, time);
        }
    }

    private Comment getComment(CommentCreationDto commentCreationDto, Long authorId, Long itemId) {
        return commentMapper.mapToComment(
                commentCreationDto,
                userRepository.getReferenceById(authorId),
                itemRepository.getReferenceById(itemId));
    }
}
