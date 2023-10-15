package ru.practicum.shareit.item.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.shareit.commondto.item.request.ItemCreationDto;
import ru.practicum.shareit.commondto.item.response.ItemDetailsDto;
import ru.practicum.shareit.commondto.item.response.ItemDto;
import ru.practicum.shareit.constants.HttpHeadersConstants;

import java.util.List;

@Service
public class ItemClient {
    private final WebClient client;

    public ItemClient(@Value("${shareit-server.url}") String serverUrl) {
        this.client = WebClient.create(serverUrl);
    }

    public ItemDto addItem(ItemCreationDto itemDto, Long userId) {
        return client.post()
                .uri("/items")
                .bodyValue(itemDto)
                .header(HttpHeadersConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToMono(ItemDto.class)
                .block();
    }

    public ItemDto updateItem(ItemCreationDto itemDto, Long itemId, Long userId) {
        return client.patch()
                .uri("/items/{id}", itemId)
                .bodyValue(itemDto)
                .header(HttpHeadersConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToMono(ItemDto.class)
                .block();
    }

    public ItemDetailsDto getItemById(Long itemId, Long userId) {
        return client.get()
                .uri("/items/{id}", itemId)
                .header(HttpHeadersConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToMono(ItemDetailsDto.class)
                .block();
    }

    public List<ItemDetailsDto> getItemsByOwnerId(Long userId, Integer from, Integer size) {
        return client.get()
                .uri("/items")
                .header(HttpHeadersConstants.X_SHARER_USER_ID, userId.toString())
                .header("from", from.toString())
                .header("size", size.toString())
                .retrieve()
                .bodyToFlux(ItemDetailsDto.class)
                .collectList()
                .block();
    }

    public List<ItemDto> searchItemsByNameOrDescription(String text, Integer from, Integer size) {
        return client.get()
                .uri(builder -> builder.path("/items/search")
                        .queryParam("text", text)
                        .build())
                .header("from", from.toString())
                .header("size", size.toString())
                .retrieve()
                .bodyToFlux(ItemDto.class)
                .collectList()
                .block();
    }
}
