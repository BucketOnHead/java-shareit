package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.request.UserCreationDto;
import ru.practicum.shareit.user.dto.response.UserResponseDto;
import ru.practicum.shareit.user.logger.UserServiceLoggerHelper;
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
        User user = userMapper.mapToUser(userDto);
        User savedUser = userRepository.save(user);
        UserServiceLoggerHelper.userSaved(log, savedUser);
        return userMapper.mapToUserResponseDto(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(UserCreationDto userDto, Long userId) {
        userRepository.validateUserExistsById(userId);
        User updatedUser = getUpdatedUser(userId, userDto);
        User savedUser = userRepository.save(updatedUser);
        UserServiceLoggerHelper.userUpdated(log, savedUser);
        return userMapper.mapToUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        userRepository.validateUserExistsById(userId);
        User user = userRepository.getReferenceById(userId);
        UserResponseDto userDto = userMapper.mapToUserResponseDto(user);
        UserServiceLoggerHelper.userDtoByIdReturned(log, userDto);
        return userDto;
    }

    @Override
    public List<UserResponseDto> getUsers(Integer from, Integer size) {
        Page<User> users = userRepository.findAll(PageRequest.of(from, size));
        List<UserResponseDto> usersDto = userMapper.mapToUserResponseDto(users);
        UserServiceLoggerHelper.userDtoPageReturned(log, from, size, usersDto);
        return usersDto;
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.validateUserExistsById(userId);
        userRepository.deleteById(userId);
        UserServiceLoggerHelper.userByIdDeleted(log, userId);
    }

    private User getUpdatedUser(Long userId, UserCreationDto userDto) {
        User user = userRepository.getReferenceById(userId);

        Optional.ofNullable(userDto.getName()).ifPresent(user::setName);
        Optional.ofNullable(userDto.getEmail()).ifPresent(user::setEmail);

        return user;
    }
}
