package ru.practicum.shareit.gateway.user.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.shareit.server.dto.user.request.UserCreationDto;
import ru.practicum.shareit.server.dto.user.response.UserDto;

import java.util.List;

@Service
public class UserClient {
    private final WebClient client;

    public UserClient(@Value("${shareit-server.url}") String serverUrl) {
        this.client = WebClient.create(serverUrl);
    }

    public UserDto addUser(UserCreationDto userDto) {
        return client.post()
                .uri("/users")
                .bodyValue(userDto)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    public UserDto updateUser(UserCreationDto userDto, Long userId) {
        return client.patch()
                .uri("/users/{id}", userId)
                .bodyValue(userDto)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    public UserDto getUserById(Long userId) {
        return client.get()
                .uri("/users/{id}", userId)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }


    public List<UserDto> getAllUsers(Integer from, Integer size) {
        return client.get()
                .uri(builder -> builder.path("/users")
                        .queryParam("from", from)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .bodyToFlux(UserDto.class)
                .collectList()
                .block();
    }

    public void deleteUserById(Long userId) {
        client.delete()
                .uri("/users/{id}", userId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
