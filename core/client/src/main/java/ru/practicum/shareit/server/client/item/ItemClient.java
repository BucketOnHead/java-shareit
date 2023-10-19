package ru.practicum.shareit.server.client.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.shareit.server.constants.HttpHeaderConstants;
import ru.practicum.shareit.server.dto.item.request.ItemCreationDto;
import ru.practicum.shareit.server.dto.item.response.ItemDetailsDto;
import ru.practicum.shareit.server.dto.item.response.ItemDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemClient {
    private final WebClient client;

    public ItemDto addItem(ItemCreationDto itemDto, Long userId) {
        return client.post()
                .uri("/items")
                .bodyValue(itemDto)
                .header(HttpHeaderConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToMono(ItemDto.class)
                .block();
    }

    public ItemDto updateItem(ItemCreationDto itemDto, Long itemId, Long userId) {
        return client.patch()
                .uri("/items/{id}", itemId)
                .bodyValue(itemDto)
                .header(HttpHeaderConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToMono(ItemDto.class)
                .block();
    }

    public ItemDetailsDto getItemById(Long itemId, Long userId) {
        return client.get()
                .uri("/items/{id}", itemId)
                .header(HttpHeaderConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToMono(ItemDetailsDto.class)
                .block();
    }

    public List<ItemDetailsDto> getItemsByUserId(Long userId, Integer from, Integer size) {
        return client.get()
                .uri(builder -> builder.path("/items")
                        .queryParam("from", from)
                        .queryParam("size", size)
                        .build())
                .header(HttpHeaderConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToFlux(ItemDetailsDto.class)
                .collectList()
                .block();
    }

    public List<ItemDto> getItemsByText(String text, Integer from, Integer size) {
        return client.get()
                .uri(builder -> builder.path("/items/search")
                        .queryParam("text", text)
                        .queryParam("from", from)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .bodyToFlux(ItemDto.class)
                .collectList()
                .block();
    }
}
