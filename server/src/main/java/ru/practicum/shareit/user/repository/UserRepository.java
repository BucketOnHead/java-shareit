package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Validates if a user exists by their ID.
     *
     * @param userId The ID of the user to validate.
     * @throws UserNotFoundException If a user with the provided ID does not exist.
     */
    default void validateUserExistsById(Long userId) {
        if (!existsById(userId)) {
            throw UserNotFoundException.byId(userId);
        }
    }
}
