package ru.practicum.shareit.server.exception.handler.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Map;

@UtilityClass
public class ErrorUtils {

    /**
     * Returns a customized error message based on provided DataIntegrityViolationException
     * and a map of constraint messages. Returns null if no suitable message is found.
     */
    public String getMessage(DataIntegrityViolationException ex, Map<String, String> constrainsMessage) {
        if (ex == null || constrainsMessage == null) {
            return null;
        }

        var rootCause = ex.getRootCause();
        if (rootCause == null) {
            return null;
        }

        var rootCauseMessage = rootCause.getMessage();
        for (var entry : constrainsMessage.entrySet()) {
            if (StringUtils.containsIgnoreCase(rootCauseMessage, entry.getKey())) {
                return entry.getValue();
            }
        }

        return null;
    }
}
