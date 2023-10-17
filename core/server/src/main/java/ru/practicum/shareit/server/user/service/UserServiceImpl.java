package ru.practicum.shareit.server.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.dto.user.request.UserCreationDto;
import ru.practicum.shareit.server.dto.user.response.UserDto;
import ru.practicum.shareit.server.user.repository.UserRepository;
import ru.practicum.shareit.server.user.mapper.UserDtoMapper;
import ru.practicum.shareit.server.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserDtoMapper userMapper;

    @Override
    @Transactional
    public UserDto addUser(UserCreationDto userDto) {
        var user = userMapper.mapToUser(userDto);
        var savedUser = userRepository.save(user);

        log.info("User with id: {} added", savedUser.getId());
        log.debug("User added: {}", savedUser);

        return userMapper.mapToUserDto(savedUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserCreationDto userDto, Long userId) {
        var user = userRepository.findByIdOrThrow(userId);
        var updatedUser = updateUser(user, userDto);
        var savedUser = userRepository.save(updatedUser);

        log.info("User with id: {} updated", savedUser.getId());
        log.debug("User updated: {}", savedUser);

        return userMapper.mapToUserDto(savedUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        var user = userRepository.findByIdOrThrow(userId);
        var userDto = userMapper.mapToUserDto(user);

        log.info("User with id: {} returned", user.getId());
        log.debug("User returned: {}", userDto);

        return userDto;
    }

    @Override
    public List<UserDto> getUsers(Integer from, Integer size) {
        var users = userRepository.findAll(PageRequest.of(from / size, size));
        var usersDto = userMapper.mapToUserDto(users);

        log.info("Users page with from: {} and size: {} returned, count: {}", from, size, usersDto.size());
        log.debug("Users page returned: {}", usersDto);

        return usersDto;
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.existsByIdOrThrow(userId);
        userRepository.deleteById(userId);
        log.info("User with id: {} deleted", userId);
    }

    private User updateUser(User user, UserCreationDto userDto) {
        var name = userDto.getName();
        if (name != null) {
            user.setName(name);
            log.trace("User name updated: {}", user);
        }

        var email = userDto.getEmail();
        if (email != null) {
            user.setEmail(email);
            log.trace("User email updated: {}", user);
        }

        return user;
    }
}
