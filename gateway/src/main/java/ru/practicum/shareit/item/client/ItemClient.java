package ru.practicum.shareit.item.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.shareit.item.dto.request.CreateItemRequestDto;
import ru.practicum.shareit.item.dto.response.DetailedItemResponseDto;
import ru.practicum.shareit.item.dto.response.ItemResponseDto;

import java.util.Comparator;
import java.util.List;

@Service
public class ItemClient {
    private final WebClient client;

    public ItemClient(@Value("${shareit-server.url}") String serverUrl) {
        this.client = WebClient.create(serverUrl);
    }

    public ItemResponseDto addItem(CreateItemRequestDto itemDto, Long userId) {
        return client.post()
                .uri("/items")
                .bodyValue(itemDto)
                .header("X-Sharer-User-Id", userId.toString())
                .retrieve()
                .bodyToMono(ItemResponseDto.class)
                .block();
    }

    public ItemResponseDto updateItem(CreateItemRequestDto itemDto, Long itemId, Long userId) {
        return client.patch()
                .uri("/items/{id}", itemId)
                .bodyValue(itemDto)
                .header("X-Sharer-User-Id", userId.toString())
                .retrieve()
                .bodyToMono(ItemResponseDto.class)
                .block();
    }

    public DetailedItemResponseDto getItemById(Long itemId, Long userId) {
        return client.get()
                .uri("/items/{id}", itemId)
                .header("X-Sharer-User-Id", userId.toString())
                .retrieve()
                .bodyToMono(DetailedItemResponseDto.class)
                .block();
    }

    public List<DetailedItemResponseDto> getItemsByOwnerId(Long userId, Integer from, Integer size) {
        return client.get()
                .uri("/items")
                .header("X-Sharer-User-Id", userId.toString())
                .header("from", from.toString())
                .header("size", size.toString())
                .retrieve()
                .bodyToFlux(DetailedItemResponseDto.class)
                .sort(Comparator.comparingLong(DetailedItemResponseDto::getId))
                .collectList()
                .block();
    }

    public List<ItemResponseDto> searchItemsByNameOrDescription(String text, Integer from, Integer size) {
        return client.get()
                .uri(builder -> builder.path("/items/search")
                        .queryParam("text", text)
                        .build())
                .header("from", from.toString())
                .header("size", size.toString())
                .retrieve()
                .bodyToFlux(ItemResponseDto.class)
                .collectList()
                .block();
    }
}
