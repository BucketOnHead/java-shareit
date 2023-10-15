package ru.practicum.shareit.item.client.comment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.shareit.commondto.item.request.comment.CommentCreationDto;
import ru.practicum.shareit.commondto.item.response.comment.CommentDto;
import ru.practicum.shareit.constants.HttpHeadersConstants;

@Service
public class CommentClient {
    private final WebClient client;

    public CommentClient(@Value("${shareit-server.url}") String serverUrl) {
        this.client = WebClient.create(serverUrl);
    }

    public CommentDto addComment(CommentCreationDto comment, Long userId, Long itemId) {
        return client.post()
                .uri("/items/{id}/comment", itemId)
                .bodyValue(comment)
                .header(HttpHeadersConstants.X_SHARER_USER_ID, userId.toString())
                .retrieve()
                .bodyToMono(CommentDto.class)
                .block();
    }
}
