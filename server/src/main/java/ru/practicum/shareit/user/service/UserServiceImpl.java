package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.request.UserCreationDto;
import ru.practicum.shareit.user.dto.response.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserDtoMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto addUser(UserCreationDto userDto) {
        var user = userMapper.mapToUser(userDto);
        var savedUser = userRepository.save(user);

        log.info("User with id: {} added", savedUser.getId());
        log.debug("User added: {}", savedUser);

        return userMapper.mapToUserResponseDto(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(UserCreationDto userDto, Long userId) {
        userRepository.validateUserExistsById(userId);
        var user = userRepository.getReferenceById(userId);
        var updatedUser = updateUser(user, userDto);
        var savedUser = userRepository.save(updatedUser);

        log.info("User with id: {} updated", savedUser.getId());
        log.debug("User updated: {}", savedUser);

        return userMapper.mapToUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        userRepository.validateUserExistsById(userId);
        var user = userRepository.getReferenceById(userId);
        var userDto = userMapper.mapToUserResponseDto(user);

        log.info("User with id: {} retrieved", user.getId());
        log.debug("User retrieved: {}", userDto);

        return userDto;
    }

    @Override
    public List<UserResponseDto> getUsers(Integer from, Integer size) {
        var users = userRepository.findAll(PageRequest.of(from, size));
        var usersDto = userMapper.mapToUserResponseDto(users);

        log.info("Users with pagination retrieved: (from: {}, size: {}), count: {}", from, size, usersDto.size());
        log.debug("Users with pagination retrieved: {}", usersDto);

        return usersDto;
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.validateUserExistsById(userId);
        userRepository.deleteById(userId);

        log.info("User with id: {} deleted", userId);
    }

    private User updateUser(User user, UserCreationDto userDto) {
        Optional.ofNullable(userDto.getName()).ifPresent(user::setName);
        Optional.ofNullable(userDto.getEmail()).ifPresent(user::setEmail);

        return user;
    }
}
