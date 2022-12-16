package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.in.RequestItemDto;
import ru.practicum.shareit.item.dto.in.comment.RequestCommentDto;
import ru.practicum.shareit.item.dto.out.DetailedItemDto;
import ru.practicum.shareit.item.dto.out.ItemDto;
import ru.practicum.shareit.item.dto.out.comment.CommentDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.comment.CommentService;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {ItemController.class})
@ExtendWith(SpringExtension.class)
class ItemControllerTest {
    @MockBean
    private CommentService commentService;

    @Autowired
    private ItemController itemController;

    @MockBean
    private ItemService itemService;

    /**
     * Method under test: {@link ItemController#updateItem(RequestItemDto, Long, Long)}
     */
    @Test
    void testUpdateItem() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setAvailable(true);
        itemDto.setDescription("The characteristics of someone or something");
        itemDto.setId(123L);
        itemDto.setName("Name");
        itemDto.setRequestId(123L);
        when(itemService.updateItem((RequestItemDto) any(), (Long) any(), (Long) any())).thenReturn(itemDto);

        RequestItemDto requestItemDto = new RequestItemDto();
        requestItemDto.setAvailable(true);
        requestItemDto.setDescription("The characteristics of someone or something");
        requestItemDto.setName("Name");
        requestItemDto.setRequestId(123L);
        String content = (new ObjectMapper()).writeValueAsString(requestItemDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/items/{itemId}", 123L)
                .header("X-Sharer-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":123,\"name\":\"Name\",\"description\":\"The characteristics of someone or something\",\"available\":true"
                                        + ",\"requestId\":123}"));
    }

    /**
     * Method under test: {@link ItemController#getItemById(Long, Long)}
     */
    @Test
    void testGetItemById() throws Exception {
        DetailedItemDto.BookingDto bookingDto = new DetailedItemDto.BookingDto();
        bookingDto.setBookerId(123L);
        bookingDto.setId(123L);

        DetailedItemDto.BookingDto bookingDto1 = new DetailedItemDto.BookingDto();
        bookingDto1.setBookerId(123L);
        bookingDto1.setId(123L);

        DetailedItemDto detailedItemDto = new DetailedItemDto();
        detailedItemDto.setAvailable(true);
        detailedItemDto.setComments(new ArrayList<>());
        detailedItemDto.setDescription("The characteristics of someone or something");
        detailedItemDto.setId(123L);
        detailedItemDto.setLastBooking(bookingDto);
        detailedItemDto.setName("Name");
        detailedItemDto.setNextBooking(bookingDto1);
        when(itemService.getItemById((Long) any(), (Long) any())).thenReturn(detailedItemDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/items/{itemId}", 123L)
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":123,\"name\":\"Name\",\"description\":\"The characteristics of someone or something\",\"available\":true"
                                        + ",\"lastBooking\":{\"id\":123,\"bookerId\":123},\"nextBooking\":{\"id\":123,\"bookerId\":123},\"comments\":[]}"));
    }

    /**
     * Method under test: {@link ItemController#getItemsByOwnerId(Long)}
     */
    @Test
    void testGetItemsByOwnerId() throws Exception {
        when(itemService.getItemsByOwnerId((Long) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/items")
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ItemController#searchItemsByNameOrDescription(String)}
     */
    @Test
    void testSearchItemsByNameOrDescription() throws Exception {
        when(itemService.searchItemsByNameOrDescription((String) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/items/search").param("text", "foo");
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ItemController#addComment(RequestCommentDto, Long, Long)}
     */
    @Test
    void testAddComment() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setAuthorName("JaneDoe");
        commentDto.setCreated(LocalDateTime.of(1, 1, 1, 1, 1));
        commentDto.setId(123L);
        commentDto.setText("Text");
        when(commentService.addComment((RequestCommentDto) any(), (Long) any(), (Long) any())).thenReturn(commentDto);

        RequestCommentDto requestCommentDto = new RequestCommentDto();
        requestCommentDto.setText("Text");
        String content = (new ObjectMapper()).writeValueAsString(requestCommentDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/items/{itemId}/comment", 123L)
                .header("X-Sharer-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":123,\"text\":\"Text\",\"authorName\":\"JaneDoe\",\"created\":[1,1,1,1,1]}"));
    }

    /**
     * Method under test: {@link ItemController#addItem(RequestItemDto, Long)}
     */
    @Test
    void testAddItem() throws Exception {
        when(itemService.getItemsByOwnerId((Long) any())).thenReturn(new ArrayList<>());

        RequestItemDto requestItemDto = new RequestItemDto();
        requestItemDto.setAvailable(true);
        requestItemDto.setDescription("The characteristics of someone or something");
        requestItemDto.setName("Name");
        requestItemDto.setRequestId(123L);
        String content = (new ObjectMapper()).writeValueAsString(requestItemDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/items")
                .header("X-Sharer-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(itemController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}

