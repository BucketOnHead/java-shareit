package ru.practicum.shareit.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.shareit.server.client.booking.BookingClient;
import ru.practicum.shareit.server.client.item.ItemClient;
import ru.practicum.shareit.server.client.item.comment.CommentClient;
import ru.practicum.shareit.server.client.itemrequest.ItemRequestClient;
import ru.practicum.shareit.server.client.user.UserClient;

@Configuration
public class WebClientConfig {

    @Value("${shareit.server.url}")
    private String url;

    @Bean
    public WebClient webClient() {
        return WebClient.create(url);
    }

    @Bean
    public BookingClient bookingClient(WebClient client) {
        return new BookingClient(client);
    }

    @Bean
    public ItemClient itemClient(WebClient client) {
        return new ItemClient(client);
    }

    @Bean
    public CommentClient commentClient(WebClient client) {
        return new CommentClient(client);
    }

    @Bean
    public ItemRequestClient itemRequestClient(WebClient client) {
        return new ItemRequestClient(client);
    }

    @Bean
    public UserClient userClient(WebClient client) {
        return new UserClient(client);
    }
}
