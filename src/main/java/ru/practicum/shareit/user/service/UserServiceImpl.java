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
import java.util.Optional;

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
        User user = UserDtoMapper.toUser(userDto);
        User savedUser = userRepository.save(user);
        log.debug("USER[ID_{}] added.", savedUser.getId());
        return UserDtoMapper.toUserDto(savedUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(RequestUserDto userDto, Long userId) {
        checkUserExistsById(userRepository, userId);
        User updatedUser = getUpdatedUser(userId, userDto);
        User savedUser = userRepository.save(updatedUser);
        log.debug("USER[ID_{}] updated.", savedUser.getId());
        return UserDtoMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        checkUserExistsById(userRepository, userId);
        User user = userRepository.getReferenceById(userId);
        UserDto userDto = UserDtoMapper.toUserDto(user);
        log.debug("USER[ID_{}]<DTO> returned.", userDto.getId());
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> usersDto = UserDtoMapper.toUserDto(users);
        log.debug("All USER<DTO> returned, {} in total.", usersDto.size());
        return usersDto;
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        checkUserExistsById(userRepository, userId);
        userRepository.deleteById(userId);
        log.debug("USER[ID_{}] deleted.", userId);
    }

    private User getUpdatedUser(Long userId, RequestUserDto userDto) {
        User user = userRepository.getReferenceById(userId);

        Optional.ofNullable(userDto.getName()).ifPresent(user::setName);
        Optional.ofNullable(userDto.getEmail()).ifPresent(user::setEmail);

        return user;
    }
}
