package ru.practicum.shareit.server.constants;

import lombok.experimental.UtilityClass;

/**
 * name = description
 * <p>
 * EG (exempli gratia) = example
 */
@UtilityClass
public class OpenApiConsts {

    @UtilityClass
    public static class Param {
        public static final String FROM = "Количество элементов, которые нужно пропустить для формирования текущего набора";
        public static final String SIZE = "Количество элементов в наборе";
        public static final String USER_ID = "Идентификатор пользователя";
        public static final String ITEM_REQUEST_ID = "Идентификатор запроса вещи";
        public static final String ITEM_ID = "Идентификатор вещи";
        public static final String STATE = "Состояние бронирования";
        public static final String FROM_EG = "10";
        public static final String SIZE_EG = "20";
        public static final String USER_ID_EG = User.ID_EG;
        public static final String ITEM_REQUEST_ID_EG = ItemRequest.ID_EG;
        public static final String ITEM_ID_EG = Item.ID_EG;
    }

    @UtilityClass
    public static class User {
        public static final String ID = "Идентификатор";
        public static final String NAME = "Имя";
        public static final String EMAIL = "Адрес электронной почты";
        public static final String ID_EG = "1";
        public static final String NAME_EG = "Вася Уточкин";
        public static final String EMAIL_EG = "vasya.utochkin@example.org";
    }

    @UtilityClass
    public static class Item {
        public static final String ID = "Идентификатор";
        public static final String NAME = "Название";
        public static final String DESCRIPTION = "Описание";
        public static final String IS_AVAILABLE = "Доступность";
        public static final String REQUEST_ID = "Идентификатор запроса";
        public static final String ID_EG = "4";
        public static final String NAME_EG = "Щётка для обуви";
        public static final String DESCRIPTION_EG = "Стандартная щётка для обуви";
        public static final String IS_AVAILABLE_EG = "true";
        public static final String REQUEST_ID_EG = ItemRequest.ID_EG;
    }

    @UtilityClass
    public static class ItemRequest {
        public static final String ID = "Идентификатор";
        public static final String DESCRIPTION = "Описание";
        public static final String CREATED = "Дата и время создания";
        public static final String ITEMS = "Вещи, добавленные по запросу";
        public static final String ID_EG = "1";
        public static final String DESCRIPTION_EG = "Хотел бы воспользоваться щёткой для обуви";
    }

    @UtilityClass
    public static class Booking {
        public static final String ID = "Идентификатор";
        public static final String START = "Дата и время начала бронирования";
        public static final String END = "Дата и время конца бронирования";
        public static final String STATUS = "Статус";
        public static final String BOOKER = "Инициатор бронирования";
        public static final String ITEM = "Вещь для бронирования";
        public static final String ID_EG = "5";
        public static final String STATUS_EG = "WAITING";
    }

    @UtilityClass
    public static class Comment {
        public static final String ID = "Идентификатор";
        public static final String TEXT = "Текст комментария";
        public static final String AUTHOR_NAME = "Имя автора";
        public static final String CREATED = "Дата и время создания";
        public static final String ID_EG = "7";
        public static final String TEXT_EG = "Все прошло хорошо, рекомендую!";
        public static final String AUTHOR_NAME_EG = User.NAME_EG;
    }

    @UtilityClass
    public static class ApiError {
        public static final String STATUS = "статус HTTP-ответа";
        public static final String REASON = "Общее описание ошибки";
        public static final String MESSAGE = "Сообщение об ошибке";
        public static final String TIMESTAMP = "Дата и время когда произошла ошибка";
        public static final String ERRORS = "Сопутствующие ошибки или иная информация(вложенные ошибки, стектрейсы и пр.)";
        public static final String STATUS_EG = "BAD_REQUEST";
        public static final String REASON_EG = "Request cannot be understood by the server due to incorrect syntax";
        public static final String MESSAGE_EG = "Unknown state: ABC";
        public static final String ERRORS_EG = "[" +
                "\"parameter 'size' must be greater than 0, but it was '0'\"," +
                "\"parameter 'from' must be greater than or equal to 0, but it was '-1'\"" +
                "]";
    }

    @UtilityClass
    public static class Response {

        public static final String GET_PAGINATION_BAD_REQUEST = "{" +
                "\"status\":\"BAD_REQUEST\"," +
                "\"reason\":\"Bad Request\"," +
                "\"message\":\"Parameter(s) failed validation\"," +
                "\"errors\":[" +
                "\"parameter 'size' must be greater than 0, but it was '0'\"," +
                "\"parameter 'from' must be greater than or equal to 0, but it was '-1'\"" +
                "]," +
                "\"timestamp\":\"2023-10-17T16:59:11.237789\"" +
                "}";

        public static final String POST_USER_BAD_REQUEST = "{" +
                "\"status\":\"BAD_REQUEST\"," +
                "\"reason\":\"Bad Request\"," +
                "\"message\":\"Field(s) failed validation\"," +
                "\"errors\":[" +
                "\"field 'email' must have the format of an email address, but it was 'vasyaexample.com'\"" +
                "]," +
                "\"timestamp\":\"2023-10-17T19:12:12.08784\"" +
                "}";

        public static final String POST_USER_CONFLICT = "{" +
                "\"status\":\"CONFLICT\"," +
                "\"reason\":\"Request conflicts with another request or with server configuration\"," +
                "\"message\":\"User email must be unique\"," +
                "\"timestamp\":\"2023-10-17T19:27:41.322698\"" +
                "}";

        public static final String USER_NOT_FOUND = "{" +
                "\"status\":\"NOT_FOUND\"," +
                "\"reason\":\"Requested resource does not exist\"," +
                "\"message\":\"User with id: 1 not found\"," +
                "\"timestamp\":\"2023-10-17T21:25:18.098461\"" +
                "}";

        public static final String ITEM_REQUEST_BAD_REQUEST = "{" +
                "\"status\":\"BAD_REQUEST\"," +
                "\"reason\":\"Bad Request\"," +
                "\"message\":\"Field(s) failed validation\"," +
                "\"errors\":[" +
                "\"field 'description' should not be empty, but it was 'null'\"" +
                "]," +
                "\"timestamp\":\"2023-10-18T14:42:50.201833\"" +
                "}";

        public static final String POST_ITEM_REQUEST_OK = "{" +
                "\"id\":1," +
                "\"description\":\"Хотел бы воспользоваться щёткой для обуви\"," +
                "\"created\":\"2023-10-18T14:49:08.869138\"," +
                "\"items\":null" +
                "}";

        public static final String ITEM_REQUEST_NOT_FOUND = "{" +
                "\"status\":\"NOT_FOUND\"," +
                "\"reason\":\"Requested resource does not exist\"," +
                "\"message\":\"Item request with id: 99 not found\"," +
                "\"timestamp\":\"2023-10-18T15:20:42.426754\"" +
                "}";

        public static final String COMMENT_BAD_REQUEST = "{" +
                "\"status\":\"BAD_REQUEST\"," +
                "\"reason\":\"Request cannot be understood by the server due to incorrect syntax\"," +
                "\"message\":\"Unable to leave comment for item with id: 1: no approved booking for user with id: 4 or booking is not yet finished\"," +
                "\"timestamp\":\"2023-10-19T09:57:46.77556\"" +
                "}";

        public static final String ITEM_NOT_FOUND = "{" +
                "\"status\":\"NOT_FOUND\"," +
                "\"reason\":\"Requested resource does not exist\"," +
                "\"message\":\"Item not found with id: 99\"," +
                "\"timestamp\":\"2023-10-19T10:11:42.797793\"" +
                "}";

        public static final String ITEM_FORBIDDEN = "{" +
                "\"status\":\"FORBIDDEN\"," +
                "\"reason\":\"Access denied\"," +
                "\"message\":\"User with id: 8 is not owner of item with id: 1\"," +
                "\"timestamp\":\"2023-10-19T11:12:52.394164\"" +
                "}";

        public static final String BOOKING_SELF_BOOK_NOT_FOUND = "{" +
                "\"status\":\"NOT_FOUND\"," +
                "\"reason\":\"Requested resource does not exist\"," +
                "\"message\":\"User with id: 1 cannot book their item with id: 1\"," +
                "\"timestamp\":\"2023-10-19T14:26:41.520621\"" +
                "}";

        public static final String BOOK_ITEM_NOT_AVAILABLE = "{" +
                "\"status\":\"BAD_REQUEST\"," +
                "\"reason\":\"Request cannot be understood by the server due to incorrect syntax\"," +
                "\"message\":\"Item with id: 2 not available for booking\"," +
                "\"timestamp\":\"2023-10-19T14:29:43.586513\"" +
                "}";

        public static final String BOOKING_BAD_REQUEST = "{" +
                "\"status\":\"BAD_REQUEST\"," +
                "\"reason\":\"Bad Request\"," +
                "\"message\":\"Field(s) failed validation\"," +
                "\"errors\":[" +
                "\"field 'end' не должно равняться null, but it was 'null'\"," +
                "\"field 'start' не должно равняться null, but it was 'null'\"" +
                "]," +
                "\"timestamp\":\"2023-10-19T14:32:41.237296\"" +
                "}";
    }
}
