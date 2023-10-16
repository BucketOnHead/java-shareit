package ru.practicum.shareit.itemrequest.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.shareit.server.dto.itemrequest.request.ItemRequestCreationDto;
import ru.practicum.shareit.server.dto.itemrequest.response.ItemRequestDto;
import ru.practicum.shareit.server.constants.HttpHeaderConstants;

import java.util.List;

@Service
public class ItemRequestClient {
    private final WebClient client;

    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl) {
        this.client = WebClient.create(serverUrl);
    }

    public ItemRequestDto addItemRequest(ItemRequestCreationDto requestDto, Long userId) {
        return client.post()
                .uri("/requests")
                .header(HttpHeaderConstants.X_SHARER_USER_ID, userId.toString())
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(ItemRequestDto.class)
                .block();
    }

    public List<ItemRequestDto> getItemRequestsByRequesterId(Long userId) {
        return client.get()
                .uri("/requests")
                .header(HttpHeaderConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToFlux(ItemRequestDto.class)
                .collectList()
                .block();
    }

    public ItemRequestDto getItemRequestById(Long itemRequestId, Long userId) {
        return client.get()
                .uri("/requests/{id}", itemRequestId)
                .header(HttpHeaderConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToMono(ItemRequestDto.class)
                .block();
    }

    public List<ItemRequestDto> getItemRequestsByRequesterId(Long userId, Integer from, Integer size) {
        return client.get()
                .uri(builder -> builder.path("/requests/all")
                        .queryParam("from", from)
                        .queryParam("size", size)
                        .build())
                .header(HttpHeaderConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToFlux(ItemRequestDto.class)
                .collectList()
                .block();
    }
}
