package ru.practicum.shareit.server.item.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.booking.repository.BookingRepository;
import ru.practicum.shareit.server.dto.item.request.comment.CommentCreationDto;
import ru.practicum.shareit.server.dto.item.response.comment.CommentDto;
import ru.practicum.shareit.server.constants.booking.BookingStatus;
import ru.practicum.shareit.server.item.exception.comment.CommentNotAllowedException;
import ru.practicum.shareit.server.item.mapper.CommentDtoMapper;
import ru.practicum.shareit.server.item.repository.ItemRepository;
import ru.practicum.shareit.server.item.repository.comment.CommentRepository;
import ru.practicum.shareit.server.user.repository.UserRepository;

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
    public CommentDto addComment(CommentCreationDto commentDto, Long authorId, Long itemId) {
        var now = LocalDateTime.now();
        if (!isItemAvailableForCommenting(authorId, itemId, now)) {
            throw new CommentNotAllowedException(itemId, authorId);
        }

        var author = userRepository.findByIdOrThrow(authorId);
        var item = itemRepository.findByIdOrThrow(itemId);
        var comment = commentMapper.mapToComment(commentDto, author, item);
        var savedComment = commentRepository.save(comment);

        log.info("Comment with id: {} added", savedComment.getId());
        log.debug("Comment added: {}", savedComment);

        return commentMapper.mapToCommentDto(savedComment);
    }

    private boolean isItemAvailableForCommenting(Long userId, Long itemId, LocalDateTime time) {
        var status = BookingStatus.APPROVED;
        var bool = bookingRepository.existsByBookerIdAndItemIdAndStatusAndEndBefore(userId, itemId, status, time);

        log.trace("User with id: {} can{} comment item with id: {} at moment: {}", userId, ((bool) ? "" : "not"),
                itemId, time);
        return bool;
    }
}
