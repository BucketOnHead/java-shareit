package ru.practicum.shareit.item.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.request.comment.CommentRequestDto;
import ru.practicum.shareit.item.dto.response.comment.SimpleCommentResponseDto;
import ru.practicum.shareit.item.exception.comment.IncorrectCommentException;
import ru.practicum.shareit.item.mapper.comment.CommentDtoMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.comment.CommentRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static ru.practicum.shareit.user.service.UserServiceImpl.checkUserExistsById;

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
        checkUserExistsById(userRepository, authorUserId);
        itemRepository.validateItemExistsById(itemId);
        checkUserBookingByUserIdAndItemId(authorUserId, itemId);
        Comment comment = getComment(commentRequestDto, authorUserId, itemId);
        Comment addedComment = commentRepository.save(comment);
        log.debug("COMMENT[ID_{}] added.", addedComment.getId());
        return CommentDtoMapper.toSimpleCommentResponseDto(addedComment);
    }

    private void checkUserBookingByUserIdAndItemId(Long userId, Long itemId) {
        LocalDateTime time = LocalDateTime.now();
        if (!bookingRepository.existsByBookerIdAndItemIdAndEndTimeIsBefore(userId, itemId, time)) {
            throw IncorrectCommentException.fromItemIdAndUserIdAndTime(itemId, userId, time);
        }
    }

    private Comment getComment(CommentRequestDto commentRequestDto, Long authorId, Long itemId) {
        User author = userRepository.getReferenceById(authorId);
        Item item = itemRepository.getReferenceById(itemId);
        return CommentDtoMapper.toComment(commentRequestDto, author, item, LocalDateTime.now());
    }
}
