package ru.practicum.shareit.request.controller;

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
import ru.practicum.shareit.request.dto.in.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.out.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {ItemRequestController.class})
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ItemRequestControllerTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final ItemRequestController itemRequestController;
    @MockBean
    private ItemRequestService itemRequestService;

    @BeforeAll
    public static void configureMapper() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Method under test: {@link ItemRequestController#addItemRequest(RequestItemRequestDto, Long)}
     */
    @Test
    void testAddItemRequest() throws Exception {
        // test parameters
        final Long itemRequestDtoId = 1L;
        // test context
        final RequestItemRequestDto requestItemRequestDto = getRequestItemRequestDto(itemRequestDtoId);
        final ItemRequestDto        itemRequestDto        = getItemRequestDtoWithEmptyItems(itemRequestDtoId);
        when(itemRequestService.addItemRequest(any(RequestItemRequestDto.class), anyLong()))
                .thenReturn(itemRequestDto);

        var requestBuilder = post("/requests")
                .header("X-Sharer-User-Id", "123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestItemRequestDto));

        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(itemRequestDto)));
    }

    /**
     * Method under test: {@link ItemRequestController#getItemRequestsByRequesterId(Long)}
     */
    @Test
    void testGetItemRequestsByRequesterId() throws Exception {
        // test parameters
        final List<ItemRequestDto> itemRequests = new ArrayList<>();
        // test context
        when(itemRequestService.getItemRequestsByRequesterId(anyLong())).thenReturn(itemRequests);

        var requestBuilder = get("/requests")
                .header("X-Sharer-User-Id", "123");

        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(itemRequests)));
    }

    /**
     * Method under test: {@link ItemRequestController#getItemRequestsById(Long, Long)}
     */
    @Test
    void testGetItemRequestsById() throws Exception {
        // test parameters
        final Long itemRequestId = 1L;
        // test context
        ItemRequestDto itemRequestDto = getItemRequestDtoWithEmptyItems(itemRequestId);
        when(itemRequestService.getItemRequestById(anyLong(), anyLong())).thenReturn(itemRequestDto);

        var requestBuilder = get("/requests/{itemRequestId}", itemRequestId)
                .header("X-Sharer-User-Id", "123");

        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(itemRequestDto)));
    }

    /**
     * Method under test: {@link ItemRequestController#getPageWithItemRequestsByRequesterId(Long, Integer, Integer)}
     */
    @Test
    void testGetPageWithItemRequestsByRequesterId_WithEmptyItemRequestRepository() throws Exception {
        // test parameters
        final List<ItemRequestDto> itemRequests = new ArrayList<>();
        // test context
        when(itemRequestService.getItemRequestsByRequesterId(anyLong(), anyInt(), anyInt()))
                .thenReturn(itemRequests);

        var requestBuilder = get("/requests/all")
                .header("X-Sharer-User-Id", "123");

        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(itemRequests)));
    }

    private ItemRequestDto getItemRequestDtoWithEmptyItems(Long itemRequestId) {
        final String         description    = String.format("Item request dto description %d", itemRequestId);
        final ItemRequestDto itemRequestDto = new ItemRequestDto();

        itemRequestDto.setId(itemRequestId);
        itemRequestDto.setDescription(description);
        itemRequestDto.setCreated(LocalDateTime.of(1, 2, 3, 4, 5, 6, 7));
        itemRequestDto.setItems(new ArrayList<>());

        return itemRequestDto;
    }

    private RequestItemRequestDto getRequestItemRequestDto(Long itemRequestDtoId) {
        final String                description    = String.format("Request item request dto %d", itemRequestDtoId);
        final RequestItemRequestDto itemRequestDto = new RequestItemRequestDto();

        itemRequestDto.setDescription(description);

        return itemRequestDto;
    }
}
