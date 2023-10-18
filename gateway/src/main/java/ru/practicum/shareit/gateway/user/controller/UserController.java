package ru.practicum.shareit.gateway.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.consts.DefaultParams;
import ru.practicum.shareit.server.client.user.UserClient;
import ru.practicum.shareit.server.constants.OpenApiConsts;
import ru.practicum.shareit.server.dto.error.ApiError;
import ru.practicum.shareit.server.dto.user.request.UserCreationDto;
import ru.practicum.shareit.server.dto.user.response.UserDto;
import ru.practicum.shareit.server.dto.validation.Groups;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Tag(name = "Пользователи", description = "API для работы с пользователями")
@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @Operation(summary = "Добавление нового пользователя")
    @ApiResponse(
            responseCode = "200",
            description = "Пользователь добавлен",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserDto.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Запрос составлен некорректно",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = OpenApiConsts.Response.POST_USER_BAD_REQUEST)
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Конфликт с базой данных",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = OpenApiConsts.Response.POST_USER_CONFLICT)
            )
    )
    @PostMapping
    public UserDto addUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные добавляемого пользователя")
            @RequestBody @Validated(Groups.OnCreate.class) UserCreationDto userDto
    ) {
        return userClient.addUser(userDto);
    }

    @Operation(
            summary = "Получение информации о пользователе по идентификатору",
            description = "В случае, если пользователь не найден возвращает код 404"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Пользователь найден",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = OpenApiConsts.Response.GET_USER_NOT_FOUND)
            )
    )
    @GetMapping("/{userId}")
    public UserDto getUserById(
            @Parameter(description = OpenApiConsts.USER_ID_PARAM_DESC, example = OpenApiConsts.USER_ID_PARAM_EG)
            @PathVariable Long userId
    ) {
        return userClient.getUserById(userId);
    }

    @Operation(
            summary = "Получение информации о пользователях",
            description = "Возвращает информацию обо всех пользователях\n\n" +
                    "В случае, если не найдено ни одного пользователя, возвращает пустой список"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Пользователи найдены",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Запрос составлен некорректно",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = OpenApiConsts.Response.GET_USERS_BAD_REQUEST)
            )
    )
    @GetMapping
    public List<UserDto> getUsers(
            @Parameter(description = OpenApiConsts.FROM_DESC, example = OpenApiConsts.FROM_EG)
            @RequestParam(defaultValue = DefaultParams.FROM) @PositiveOrZero Integer from,

            @Parameter(description = OpenApiConsts.SIZE_DESC, example = OpenApiConsts.SIZE_EG)
            @RequestParam(defaultValue = DefaultParams.SIZE) @Positive Integer size
    ) {
        return userClient.getUsers(from, size);
    }

    @Operation(
            summary = "Обновить данные пользователя",
            description = "Для обновления пользователя используется тоже дто, что и для создания, " +
                    "все параметры НЕОБЯЗАТЕЛЬНЫ, email при указании должен быть правильно формата"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Пользователь обновлен",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserDto.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Запрос составлен некорректно",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = OpenApiConsts.Response.POST_USER_BAD_REQUEST)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = OpenApiConsts.Response.GET_USER_NOT_FOUND)
            )
    )
    @PatchMapping("/{userId}")
    public UserDto updateUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для обновления пользователя")
            @RequestBody @Validated(Groups.OnUpdate.class) UserCreationDto userDto,

            @Parameter(description = OpenApiConsts.USER_ID_PARAM_DESC, example = OpenApiConsts.USER_ID_PARAM_EG)
            @PathVariable Long userId
    ) {
        return userClient.updateUser(userDto, userId);
    }

    @Operation(summary = "Удаление пользователя по идентификатору")
    @ApiResponse(
            responseCode = "200",
            description = "Пользователь удалён"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = OpenApiConsts.Response.GET_USER_NOT_FOUND)
            )
    )
    @DeleteMapping("/{userId}")
    public void deleteUserById(
            @Parameter(description = OpenApiConsts.USER_ID_PARAM_DESC, example = OpenApiConsts.USER_ID_PARAM_EG)
            @PathVariable Long userId
    ) {
        userClient.deleteUserById(userId);
    }
}
