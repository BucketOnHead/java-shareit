package ru.practicum.shareit.item.logger.comment;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import ru.practicum.shareit.item.model.Comment;

@UtilityClass
public final class CommentServiceLoggerHelper {

    public static void commentSaved(
            Logger log,
            Comment comment
    ) {
        log.debug("COMMENT[" +
                        "id={}" +
                        "] saved.",
                comment.getId());
    }
}
