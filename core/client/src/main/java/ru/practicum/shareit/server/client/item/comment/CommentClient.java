package ru.practicum.shareit.server.client.item.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.shareit.server.constants.HttpHeaderConstants;
import ru.practicum.shareit.server.dto.item.request.comment.CommentCreationDto;
import ru.practicum.shareit.server.dto.item.response.comment.CommentDto;

@Service
@RequiredArgsConstructor
public class CommentClient {
    private final WebClient client;

    public CommentDto addComment(CommentCreationDto comment, Long userId, Long itemId) {
        return client.post()
                .uri("/items/{id}/comment", itemId)
                .bodyValue(comment)
                .header(HttpHeaderConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToMono(CommentDto.class)
                .block();
    }
}
