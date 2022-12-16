package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.in.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.out.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ItemRequestControllerTest {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private static RequestItemRequestDto DEFAULT_ITEM_REQUEST_IN_DTO;
    private static ItemRequestDto DEFAULT_ITEM_REQUEST_OUT_DTO;
    private final ObjectMapper objectMapper;

    private final MockMvc mockMvc;

    @MockBean
    private final ItemRequestServiceImpl itemRequestService;

    @BeforeAll
    static void initItemRequestDto() {
        RequestItemRequestDto itemRequestInDto = new RequestItemRequestDto();
        itemRequestInDto.setDescription("Item request in dto description");
        ItemRequestControllerTest.DEFAULT_ITEM_REQUEST_IN_DTO = itemRequestInDto;

        ItemRequestDto itemRequestOutDto = new ItemRequestDto();
        itemRequestOutDto.setId(1L);
        itemRequestOutDto.setDescription("Item request out dto description");
        itemRequestOutDto.setCreated(null);
        itemRequestOutDto.setItems(null);
        ItemRequestControllerTest.DEFAULT_ITEM_REQUEST_OUT_DTO = itemRequestOutDto;
    }

    /**
     * Method under test: {@link ItemRequestController#addItemRequest(RequestItemRequestDto, Long)}
     */
    @Test
    void testAddItemRequest() throws Exception {
        when(itemRequestService.addItemRequest(any(RequestItemRequestDto.class), anyLong()))
                .thenReturn(DEFAULT_ITEM_REQUEST_OUT_DTO);

        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(DEFAULT_ITEM_REQUEST_IN_DTO))
                        .header(USER_ID_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(DEFAULT_ITEM_REQUEST_OUT_DTO)));
    }

    /**
     * Method under test: {@link ItemRequestController#getItemRequestsByRequesterId(Long)}
     */
    @Test
    void testGetItemRequestsByRequesterId() throws Exception {
        when(itemRequestService.getItemRequestsByRequesterId(anyLong()))
                .thenReturn(List.of(DEFAULT_ITEM_REQUEST_OUT_DTO));

        mockMvc.perform(get("/requests")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(DEFAULT_ITEM_REQUEST_OUT_DTO))));
    }

    /**
     * Method under test: {@link ItemRequestController#getItemRequestsById(Long, Long)}
     */
    @Test
    void testGetItemRequestsById() throws Exception {
        when(itemRequestService.getItemRequestById(anyLong(), anyLong()))
                .thenReturn(DEFAULT_ITEM_REQUEST_OUT_DTO);

        mockMvc.perform(get("/requests/1")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(DEFAULT_ITEM_REQUEST_OUT_DTO)));
    }

    /**
     * Method under test: {@link ItemRequestController#getPageWithItemRequestsByRequesterId(Long, Integer, Integer)}
     */
    @Test
    void testGetPageWithItemRequestsByRequesterId() throws Exception {
        when(itemRequestService.getItemRequestsByRequesterId(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(DEFAULT_ITEM_REQUEST_OUT_DTO));

        mockMvc.perform(get("/requests/all")
                        .header(USER_ID_HEADER, 1L)
                        .param("from", "1")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(DEFAULT_ITEM_REQUEST_OUT_DTO))));
    }
}
