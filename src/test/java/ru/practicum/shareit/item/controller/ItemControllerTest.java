package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.in.RequestItemDto;
import ru.practicum.shareit.item.dto.in.comment.RequestCommentDto;
import ru.practicum.shareit.item.dto.out.DetailedItemDto;
import ru.practicum.shareit.item.dto.out.DetailedItemDto.BookingDto;
import ru.practicum.shareit.item.dto.out.ItemDto;
import ru.practicum.shareit.item.dto.out.comment.CommentDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.comment.CommentService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {ItemController.class})
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ItemControllerTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final ItemController itemController;
    @MockBean
    private CommentService commentService;
    @MockBean
    private ItemService itemService;

    @BeforeAll
    public static void configureMapper() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Method under test: {@link ItemController#addItem(RequestItemDto, Long)}
     */
    @Test
    void testAddItem() throws Exception {
        // test parameters
        final Long requestItemDtoId = 1L;
        final Long ownerId          = 1L;
        final Long itemDtoId        = 1L;
        // test context
        final RequestItemDto requestItemDto = getRequestItemDto(requestItemDtoId, TRUE);
        final ItemDto        itemDto        = getItemDto(itemDtoId, requestItemDto);
        when(itemService.addItem(any(RequestItemDto.class), anyLong())).thenReturn(itemDto);

        var requestBuilder = post("/items")
                .header("X-Sharer-User-Id", ownerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestItemDto));

        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));
    }

    /**
     * Method under test: {@link ItemController#updateItem(RequestItemDto, Long, Long)}
     */
    @Test
    void testUpdateItem() throws Exception {
        // test parameters
        final Long requestItemDtoId = 1L;
        final Long itemId           = 1L;
        // test context
        final RequestItemDto requestItemDto = getRequestItemDto(requestItemDtoId, TRUE);
        final ItemDto        itemDto        = getItemDto(itemId, requestItemDto);
        when(itemService.updateItem(any(RequestItemDto.class), anyLong(), anyLong()))
                .thenReturn(itemDto);

        var requestBuilder = patch("/items/{itemId}", itemId)
                .header("X-Sharer-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestItemDto));

        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));
    }

    /**
     * Method under test: {@link ItemController#getItemById(Long, Long)}
     */
    @Test
    void testGetItemById() throws Exception {
        // test parameters
        final Long bookerId      = 1L;
        final Long bookerId2     = 2L;
        final Long lastBookingId = 1L;
        final Long nextBookingId = 1L;
        final Long itemId        = 1L;
        final Long userId        = 7L;
        // test context
        final List<DetailedItemDto.CommentDto> comments = new ArrayList<>();
        final BookingDto       lastBooking     = getBookingDto(lastBookingId, bookerId);
        final BookingDto       nextBooking     = getBookingDto(nextBookingId, bookerId2);
        final DetailedItemDto  detailedItemDto = getDetailedItemDto(itemId, TRUE, lastBooking, nextBooking, comments);
        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(detailedItemDto);

        var requestBuilder = get("/items/{itemId}", itemId)
                .header("X-Sharer-User-Id", userId);

        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(detailedItemDto)));
    }

    /**
     * Method under test: {@link ItemController#getItemsByOwnerId(Long, Integer, Integer)}
     */
    @Test
    void testGetItemsByOwnerId() throws Exception {
        // test parameters
        final Long userId = 1L;
        // test context
        final List<DetailedItemDto> items = new ArrayList<>();
        when(itemService.getItemsByOwnerId(anyLong(), anyInt(), anyInt())).thenReturn(items);

        var requestBuilder = get("/items")
                .header("X-Sharer-User-Id", userId);

        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(items)));
    }

    /**
     * Method under test: {@link ItemController#searchItemsByNameOrDescription(String)}
     */
    @Test
    void testSearchItemsByNameOrDescription() throws Exception {
        // test parameters
        String text = "text";
        // test context
        final List<ItemDto> items = new ArrayList<>();
        when(itemService.searchItemsByNameOrDescription(anyString())).thenReturn(items);

        var requestBuilder = get("/items/search")
                .param("text", text);

        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(items)));
    }

    /**
     * Method under test: {@link ItemController#addComment(RequestCommentDto, Long, Long)}
     */
    @Test
    void testAddComment() throws Exception {
        // test parameters
        final Long commentId = 1L;
        final Long itemId    = 1L;
        // test context
        final RequestCommentDto requestCommentDto = getRequestCommentDto(commentId);
        final CommentDto        commentDto        = getCommentDto(commentId, requestCommentDto);
        when(commentService.addComment(any(RequestCommentDto.class), anyLong(), anyLong()))
                .thenReturn(commentDto);

        var requestBuilder = post("/items/{itemId}/comment", itemId)
                .header("X-Sharer-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestCommentDto));

        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(commentDto)));
    }

    private RequestItemDto getRequestItemDto(Long requestItemDtoId, Boolean available) {
        final String         name        = String.format("RequestItemDtoName%d", requestItemDtoId);
        final String         description = String.format("Request item dto %d description", requestItemDtoId);
        final RequestItemDto itemDto     = new RequestItemDto();

        itemDto.setRequestId(requestItemDtoId);
        itemDto.setName(name);
        itemDto.setDescription(description);
        itemDto.setAvailable(available);

        return itemDto;
    }

    private ItemDto getItemDto(Long itemId, RequestItemDto requestItemDto) {
        ItemDto itemDto = new ItemDto();

        itemDto.setId(itemId);
        itemDto.setName(requestItemDto.getName());
        itemDto.setDescription(requestItemDto.getDescription());
        itemDto.setAvailable(requestItemDto.getAvailable());
        itemDto.setRequestId(requestItemDto.getRequestId());

        return itemDto;
    }

    private BookingDto getBookingDto(Long dtoId, Long bookerId) {
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(dtoId);
        bookingDto.setBookerId(bookerId);

        return bookingDto;
    }

    private DetailedItemDto getDetailedItemDto(
            Long itemId,
            Boolean available,
            BookingDto lastBooking,
            BookingDto nextBooking,
            List<DetailedItemDto.CommentDto> comments
    ) {
        final String          name        = String.format("DetailedItemDto%dName", itemId);
        final String          description = String.format("Detailed item dto %d description", itemId);
        final DetailedItemDto itemDto     = new DetailedItemDto();

        itemDto.setId(itemId);
        itemDto.setName(name);
        itemDto.setDescription(description);
        itemDto.setAvailable(available);
        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);
        itemDto.setComments(comments);

        return itemDto;
    }

    private CommentDto getCommentDto(Long commentId, RequestCommentDto requestCommentDto) {
        final String        author     = String.format("Comment%dAuthor", commentId);
        final LocalDateTime time       = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
        final CommentDto    commentDto = new CommentDto();

        commentDto.setId(commentId);
        commentDto.setAuthorName(author);
        commentDto.setText(requestCommentDto.getText());
        commentDto.setCreated(time);

        return commentDto;
    }

    private RequestCommentDto getRequestCommentDto(Long commentId) {
        final String            text       = String.format("Comment %d text", commentId);
        final RequestCommentDto commentDto = new RequestCommentDto();

        commentDto.setText(text);

        return commentDto;
    }
}
