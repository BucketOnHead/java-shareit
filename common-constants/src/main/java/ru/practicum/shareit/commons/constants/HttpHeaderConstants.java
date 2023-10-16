package ru.practicum.shareit.commons.constants;

import lombok.experimental.UtilityClass;

/**
 * This class is a utility class that provides
 * constants for commonly used HTTP header names.
 */
@UtilityClass
public class HttpHeaderConstants {

    /**
     * Constant for the "X-Sharer-User-Id" HTTP header name.
     * This header is used to pass the ID of the user who shared a resource.
     */
    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
}
