package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserEmailAlreadyExistsExceptionException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    @Override
    public UserDto addUser(UserDto userDto) {
        checkUniquenessUserEmail(userDto.getEmail());
        User user = userDtoMapper.toUser(userDto);
        User addedUser = userRepository.addUser(user);
        UserDto addedUserDto = userDtoMapper.toUserDto(addedUser);
        log.debug("User with ID_{} added.", addedUser.getId());
        return addedUserDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        checkUserExistsById(userId);
        checkUniquenessUserEmail(userDto.getEmail());
        User user = userRepository.getUserById(userId);
        fillUserFromUserDto(user, userDto);
        User updatedUser = userRepository.updateUser(user);
        UserDto updatedUserDto = userDtoMapper.toUserDto(updatedUser);
        log.debug("User with ID_{} updated.", userId);
        return updatedUserDto;
    }

    @Override
    public UserDto getUserById(Long userId) {
        checkUserExistsById(userId);
        User returnedUser = userRepository.getUserById(userId);
        UserDto userDto = userDtoMapper.toUserDto(returnedUser);
        log.debug("User with ID_{} returned.", userId);
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> users = userRepository.getAllUsers()
                .stream()
                .map(userDtoMapper::toUserDto)
                .collect(Collectors.toList());
        log.debug("Returned a list of all users.");
        return users;
    }

    @Override
    public void deleteUserById(Long userId) {
        checkUserExistsById(userId);
        userRepository.deleteUserById(userId);
        log.debug("User with ID_{} deleted.", userId);
    }

    private static void fillUserFromUserDto(User user, UserDto userDto) {
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
    }

    private void checkUserExistsById(Long userId) {
        log.debug("Search user with ID_{}.", userId);
        if (!userRepository.containsById(userId)) {
            log.trace("User with ID_{} not found ❌.", userId);
            throw new UserNotFoundException(userId);
        }
        log.trace("User with ID_{} has been found ✔.", userId);
    }

    private void checkUniquenessUserEmail(String email) {
        log.debug("Checking the uniqueness of the email: '{}'.", email);
        boolean isUniqueEmail = userRepository.getAllUsers()
                .stream()
                .map(User::getEmail)
                .noneMatch(userEmail -> userEmail.equals(email));
        if (!isUniqueEmail) {
            log.trace("Email uniqueness check failed ❌");
            throw new UserEmailAlreadyExistsExceptionException(email);
        }
        log.trace("Email uniqueness check passed ✔");
    }
}
