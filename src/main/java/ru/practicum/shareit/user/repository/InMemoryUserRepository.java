package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> userRepository = new HashMap<>();
    private long uniqueId = 1;

    @Override
    public User addUser(User user) {
        assignIdToUser(user);
        userRepository.put(user.getId(), user);
        return getUserById(user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userRepository.values());
    }

    @Override
    public User updateUser(User user) {
        userRepository.put(user.getId(), user);
        return getUserById(user.getId());
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.get(userId);
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.remove(userId);
    }

    @Override
    public boolean containsById(Long userId) {
        return userRepository.containsKey(userId);
    }

    private void assignIdToUser(User user) {
        Long id = generateUniqueId();
        user.setId(id);
    }

    private long generateUniqueId() {
        return uniqueId++;
    }
}
