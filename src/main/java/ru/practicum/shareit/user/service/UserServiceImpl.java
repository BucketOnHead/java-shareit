package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.in.RequestUserDto;
import ru.practicum.shareit.user.dto.out.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static ru.practicum.shareit.user.mapper.UserDtoMapper.toUser;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public static void checkUserExistsById(UserRepository userRepository, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw UserNotFoundException.getFromUserId(userId);
        }
    }

    @Override
    @Transactional
    public UserDto addUser(RequestUserDto userDto) {
        User user = toUser(userDto);
        User addedUser = userRepository.save(user);
        log.debug("User ID_{} added.", addedUser.getId());
        return UserDtoMapper.toUserDto(addedUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(RequestUserDto userDto, Long userId) {
        checkUserExistsById(userRepository, userId);
        User user = getUser(userDto, userId);
        User updatedUser = userRepository.save(user);
        log.debug("User ID_{} updated.", updatedUser.getId());
        return UserDtoMapper.toUserDto(updatedUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        checkUserExistsById(userRepository, userId);
        User user = userRepository.getReferenceById(userId);
        log.debug("User ID_{} returned.", user.getId());
        return UserDtoMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        log.debug("All users returned, {} in total.", users.size());
        return UserDtoMapper.toUserDto(users);
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        checkUserExistsById(userRepository, userId);
        log.debug("User ID_{} deleted.", userId);
        userRepository.deleteById(userId);
    }

    private User getUser(RequestUserDto userDto, Long userId) {
        User user = userRepository.getReferenceById(userId);
        return toUser(userDto, user);
    }
}
