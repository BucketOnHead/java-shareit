package ru.practicum.shareit.item.service.comment;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.in.comment.RequestCommentDto;
import ru.practicum.shareit.item.dto.out.comment.CommentDto;
import ru.practicum.shareit.item.exception.comment.IncorrectCommentException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.comment.CommentRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {CommentServiceImpl.class})
@ExtendWith(SpringExtension.class)
class CommentServiceImplTest {
    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private CommentServiceImpl commentServiceImpl;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link CommentServiceImpl#addComment(RequestCommentDto, Long, Long)}
     */
    @Test
    void testAddComment() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("Name");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user1);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("Name");
        item.setOwner(user2);

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        comment.setId(123L);
        comment.setItem(item);
        comment.setText("Text");
        when(commentRepository.save((Comment) any())).thenReturn(comment);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(123L);
        user3.setName("Name");

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest1.setDescription("The characteristics of someone or something");
        itemRequest1.setId(123L);
        itemRequest1.setRequester(user3);

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(123L);
        user4.setName("Name");

        Item item1 = new Item();
        item1.setDescription("The characteristics of someone or something");
        item1.setId(123L);
        item1.setIsAvailable(true);
        item1.setItemRequest(itemRequest1);
        item1.setName("Name");
        item1.setOwner(user4);
        when(itemRepository.getReferenceById((Long) any())).thenReturn(item1);
        when(itemRepository.existsById((Long) any())).thenReturn(true);

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(123L);
        user5.setName("Name");
        when(userRepository.getReferenceById((Long) any())).thenReturn(user5);
        when(userRepository.existsById((Long) any())).thenReturn(true);
        when(bookingRepository.existsByBookerIdAndItemIdAndEndTimeIsBefore((Long) any(), (Long) any(),
                (LocalDateTime) any())).thenReturn(true);

        RequestCommentDto requestCommentDto = new RequestCommentDto();
        requestCommentDto.setText("Text");
        CommentDto actualAddCommentResult = commentServiceImpl.addComment(requestCommentDto, 123L, 123L);
        assertEquals("Name", actualAddCommentResult.getAuthorName());
        assertEquals("Text", actualAddCommentResult.getText());
        assertEquals(123L, actualAddCommentResult.getId().longValue());
        assertEquals("0001-01-01", actualAddCommentResult.getCreated().toLocalDate().toString());
        verify(commentRepository).save((Comment) any());
        verify(itemRepository).existsById((Long) any());
        verify(itemRepository).getReferenceById((Long) any());
        verify(userRepository).existsById((Long) any());
        verify(userRepository).getReferenceById((Long) any());
        verify(bookingRepository).existsByBookerIdAndItemIdAndEndTimeIsBefore((Long) any(), (Long) any(),
                (LocalDateTime) any());
    }

    /**
     * Method under test: {@link CommentServiceImpl#addComment(RequestCommentDto, Long, Long)}
     */
    @Test
    void testAddComment2() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("Name");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user1);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("Name");
        item.setOwner(user2);

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        comment.setId(123L);
        comment.setItem(item);
        comment.setText("Text");
        when(commentRepository.save((Comment) any())).thenReturn(comment);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(123L);
        user3.setName("Name");

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest1.setDescription("The characteristics of someone or something");
        itemRequest1.setId(123L);
        itemRequest1.setRequester(user3);

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(123L);
        user4.setName("Name");

        Item item1 = new Item();
        item1.setDescription("The characteristics of someone or something");
        item1.setId(123L);
        item1.setIsAvailable(true);
        item1.setItemRequest(itemRequest1);
        item1.setName("Name");
        item1.setOwner(user4);
        when(itemRepository.getReferenceById((Long) any())).thenReturn(item1);
        when(itemRepository.existsById((Long) any())).thenReturn(true);
        when(userRepository.getReferenceById((Long) any())).thenThrow(new IncorrectCommentException("An error occurred"));
        when(userRepository.existsById((Long) any())).thenReturn(true);
        when(bookingRepository.existsByBookerIdAndItemIdAndEndTimeIsBefore((Long) any(), (Long) any(),
                (LocalDateTime) any())).thenReturn(true);

        RequestCommentDto requestCommentDto = new RequestCommentDto();
        requestCommentDto.setText("Text");
        assertThrows(IncorrectCommentException.class, () -> commentServiceImpl.addComment(requestCommentDto, 123L, 123L));
        verify(itemRepository).existsById((Long) any());
        verify(userRepository).existsById((Long) any());
        verify(userRepository).getReferenceById((Long) any());
        verify(bookingRepository).existsByBookerIdAndItemIdAndEndTimeIsBefore((Long) any(), (Long) any(),
                (LocalDateTime) any());
    }

    /**
     * Method under test: {@link CommentServiceImpl#addComment(RequestCommentDto, Long, Long)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testAddComment3() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   ru.practicum.shareit.item.exception.comment.IncorrectCommentException: An error occurred
        //       at ru.practicum.shareit.item.mapper.comment.CommentDtoMapper.toCommentDto(CommentDtoMapper.java:49)
        //       at ru.practicum.shareit.item.service.comment.CommentServiceImpl.addComment(CommentServiceImpl.java:43)
        //   See https://diff.blue/R013 to resolve this issue.

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setName("Name");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest.setDescription("The characteristics of someone or something");
        itemRequest.setId(123L);
        itemRequest.setRequester(user1);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setName("Name");

        Item item = new Item();
        item.setDescription("The characteristics of someone or something");
        item.setId(123L);
        item.setIsAvailable(true);
        item.setItemRequest(itemRequest);
        item.setName("Name");
        item.setOwner(user2);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(123L);
        user3.setName("Name");
        Comment comment = mock(Comment.class);
        when(comment.getCreated()).thenThrow(new IncorrectCommentException("An error occurred"));
        when(comment.getId()).thenReturn(123L);
        when(comment.getText()).thenReturn("Text");
        when(comment.getAuthor()).thenReturn(user3);
        doNothing().when(comment).setAuthor((User) any());
        doNothing().when(comment).setCreated((LocalDateTime) any());
        doNothing().when(comment).setId((Long) any());
        doNothing().when(comment).setItem((Item) any());
        doNothing().when(comment).setText((String) any());
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        comment.setId(123L);
        comment.setItem(item);
        comment.setText("Text");
        when(commentRepository.save((Comment) any())).thenReturn(comment);

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(123L);
        user4.setName("Name");

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setCreationTime(LocalDateTime.of(1, 1, 1, 1, 1));
        itemRequest1.setDescription("The characteristics of someone or something");
        itemRequest1.setId(123L);
        itemRequest1.setRequester(user4);

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(123L);
        user5.setName("Name");

        Item item1 = new Item();
        item1.setDescription("The characteristics of someone or something");
        item1.setId(123L);
        item1.setIsAvailable(true);
        item1.setItemRequest(itemRequest1);
        item1.setName("Name");
        item1.setOwner(user5);
        when(itemRepository.getReferenceById((Long) any())).thenReturn(item1);
        when(itemRepository.existsById((Long) any())).thenReturn(true);

        User user6 = new User();
        user6.setEmail("jane.doe@example.org");
        user6.setId(123L);
        user6.setName("Name");
        when(userRepository.getReferenceById((Long) any())).thenReturn(user6);
        when(userRepository.existsById((Long) any())).thenReturn(true);
        when(bookingRepository.existsByBookerIdAndItemIdAndEndTimeIsBefore((Long) any(), (Long) any(),
                (LocalDateTime) any())).thenReturn(true);

        RequestCommentDto requestCommentDto = new RequestCommentDto();
        requestCommentDto.setText("Text");
        commentServiceImpl.addComment(requestCommentDto, 123L, 123L);
    }
}

