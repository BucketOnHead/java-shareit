package ru.practicum.shareit.item.client.comment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.shareit.item.dto.request.comment.CreateCommentRequestDto;
import ru.practicum.shareit.item.dto.response.comment.CommentResponseDto;

@Service
public class CommentClient {
    private final WebClient client;

    public CommentClient(@Value("${shareit-server.url}") String serverUrl) {
        this.client = WebClient.create(serverUrl);
    }

    public CommentResponseDto addComment(CreateCommentRequestDto comment, Long userId, Long itemId) {
        return client.post()
                .uri("/items/{id}/comment", itemId)
                .bodyValue(comment)
                .header("X-Sharer-User-Id", userId.toString())
                .retrieve()
                .bodyToMono(CommentResponseDto.class)
                .block();
    }
}
