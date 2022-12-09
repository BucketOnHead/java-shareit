package ru.practicum.shareit.item.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.in.comment.RequestCommentDto;
import ru.practicum.shareit.item.dto.out.comment.CommentDto;
import ru.practicum.shareit.item.exception.comment.IncorrectCommentException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.comment.CommentRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static ru.practicum.shareit.item.mapper.comment.CommentDtoMapper.toComment;
import static ru.practicum.shareit.item.mapper.comment.CommentDtoMapper.toCommentDto;
import static ru.practicum.shareit.item.service.ItemServiceImpl.checkItemExistsById;
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
    public CommentDto addComment(RequestCommentDto requestCommentDto, Long authorId, Long itemId) {
        checkUserExistsById(userRepository, authorId);
        checkItemExistsById(itemRepository, itemId);
        checkUserBookingByUserIdAndItemId(authorId, itemId);
        Comment comment = getComment(requestCommentDto, authorId, itemId);
        Comment addedComment = commentRepository.save(comment);
        log.debug("Comment ID_{} added.", addedComment.getId());
        return toCommentDto(addedComment);
    }

    private Comment getComment(RequestCommentDto requestCommentDto, Long authorId, Long itemId) {
        User author = userRepository.getReferenceById(authorId);
        Item item = itemRepository.getReferenceById(itemId);
        return toComment(requestCommentDto, author, item);
    }

    private void checkUserBookingByUserIdAndItemId(Long userId, Long itemId) {
        LocalDateTime time = LocalDateTime.now();
        if (!bookingRepository.existsByBookerIdAndItemIdAndEndTimeIsBefore(userId, itemId, time)) {
            throw IncorrectCommentException.getFromUserIdAndItemIdAndTime(userId, itemId, time);
        }
    }
}
