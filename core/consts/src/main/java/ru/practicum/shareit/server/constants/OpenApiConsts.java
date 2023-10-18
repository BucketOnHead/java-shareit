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
        public static final String FROM_EG = "10";
        public static final String SIZE_EG = "20";
        public static final String USER_ID_EG = "1";
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

        public static final String GET_USERS_BAD_REQUEST = "{" +
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

        public static final String GET_USER_NOT_FOUND = "{" +
                "\"status\":\"NOT_FOUND\"," +
                "\"reason\":\"Requested resource does not exist\"," +
                "\"message\":\"User with id: 1 not found\"," +
                "\"timestamp\":\"2023-10-17T21:25:18.098461\"" +
                "}";
    }
}
