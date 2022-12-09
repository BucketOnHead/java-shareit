package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.in.UserCreationRequestDto;
import ru.practicum.shareit.user.dto.in.UserUpdateRequestDto;
import ru.practicum.shareit.user.dto.out.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    public static void checkUserExistsById(UserRepository userRepository, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw UserNotFoundException.getFromUserId(userId);
        }
    }

    @Override
    @Transactional
    public UserDto addUser(UserCreationRequestDto userDto) {
        User user = userDtoMapper.toUser(userDto);
        User addedUser = userRepository.save(user);
        log.debug("User ID_{} added.", addedUser.getId());
        return userDtoMapper.toUserDto(addedUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserUpdateRequestDto userDto, Long userId) {
        checkUserExistsById(userRepository, userId);
        User user = userDtoMapper.toUser(userDto, userId);
        User updatedUser = userRepository.save(user);
        log.debug("User ID_{} updated.", updatedUser.getId());
        return userDtoMapper.toUserDto(updatedUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        checkUserExistsById(userRepository, userId);
        User user = userRepository.getReferenceById(userId);
        log.debug("User ID_{} returned.", user.getId());
        return userDtoMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        log.debug("All users returned, {} in total.", users.size());
        return userDtoMapper.toUserDto(users);
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        checkUserExistsById(userRepository, userId);
        log.debug("User ID_{} deleted.", userId);
        userRepository.deleteById(userId);
    }
}
