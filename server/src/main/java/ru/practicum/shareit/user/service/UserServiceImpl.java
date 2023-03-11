package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.request.UserRequestDto;
import ru.practicum.shareit.user.dto.response.UserResponseDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
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

    public static void validateUserExistsById(UserRepository userRepository, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw UserNotFoundException.getFromUserId(userId);
        }
    }

    @Override
    @Transactional
    public UserResponseDto addUser(UserRequestDto userDto) {
        User user = UserDtoMapper.toUser(userDto);
        User savedUser = userRepository.save(user);
        log.debug("USER[ID_{}] added.", savedUser.getId());
        return UserDtoMapper.toUserDto(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(UserRequestDto userDto, Long userId) {
        validateUserExistsById(userRepository, userId);
        User updatedUser = getUpdatedUser(userId, userDto);
        User savedUser = userRepository.save(updatedUser);
        log.debug("USER[ID_{}] updated.", savedUser.getId());
        return UserDtoMapper.toUserDto(savedUser);
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        validateUserExistsById(userRepository, userId);
        User user = userRepository.getReferenceById(userId);
        UserResponseDto userDto = UserDtoMapper.toUserDto(user);
        log.debug("USER[ID_{}]<DTO> returned.", userDto.getId());
        return userDto;
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDto> usersDto = UserDtoMapper.toUserDto(users);
        log.debug("All USER<DTO> returned, {} in total.", usersDto.size());
        return usersDto;
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        validateUserExistsById(userRepository, userId);
        userRepository.deleteById(userId);
        log.debug("USER[ID_{}] deleted.", userId);
    }

    private User getUpdatedUser(Long userId, UserRequestDto userDto) {
        User user = userRepository.getReferenceById(userId);

        Optional.ofNullable(userDto.getName()).ifPresent(user::setName);
        Optional.ofNullable(userDto.getEmail()).ifPresent(user::setEmail);

        return user;
    }
}
