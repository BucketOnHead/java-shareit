package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User addUser(User user);

    List<User> getAllUsers();

    User updateUser(User user);

    User getUserById(Long userId);

    void deleteUserById(Long userId);

    boolean containsById(Long userId);
}
