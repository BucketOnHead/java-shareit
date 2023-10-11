package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    default void existsByIdOrThrow(Long userId) {
        if (!existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    default User findByIdOrThrow(Long userId) {
        var optionalUser = findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(userId);
        }

        return optionalUser.get();
    }
}
