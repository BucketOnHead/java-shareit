package ru.practicum.shareit.server.dto.item.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.constants.OpenApiConsts;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Расширенные данные о вещи")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ItemDetailsDto {

    @Schema(description = OpenApiConsts.Item.ID, example = OpenApiConsts.Item.ID_EG)
    Long id;

    @Schema(description = OpenApiConsts.Item.NAME, example = OpenApiConsts.Item.NAME_EG)
    String name;

    @Schema(description = OpenApiConsts.Item.DESCRIPTION, example = OpenApiConsts.Item.DESCRIPTION_EG)
    String description;

    @Schema(description = OpenApiConsts.Item.IS_AVAILABLE, example = OpenApiConsts.Item.IS_AVAILABLE_EG)
    Boolean available;

    @Schema(description = "Последнее бронирование")
    BookingDto lastBooking;

    @Schema(description = "Следующее бронирование")
    BookingDto nextBooking;

    @Schema(description = "Отзывы")
    List<CommentDto> comments;

    @Schema(description = "Информация о бронировании")
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    public static class BookingDto {

        @Schema(description = OpenApiConsts.Booking.ID, example = OpenApiConsts.Booking.ID_EG)
        Long id;

        @Schema(description = OpenApiConsts.User.ID, example = OpenApiConsts.User.ID_EG)
        Long bookerId;
    }

    @Schema(description = "Информация об отзывах")
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    public static class CommentDto {

        @Schema(description = OpenApiConsts.Comment.ID, example = OpenApiConsts.Comment.ID_EG)
        Long id;

        @Schema(description = OpenApiConsts.Comment.AUTHOR_NAME, example = OpenApiConsts.Comment.AUTHOR_NAME_EG)
        String authorName;

        @Schema(description = OpenApiConsts.Comment.TEXT, example = OpenApiConsts.Comment.TEXT_EG)
        String text;

        @Schema(description = OpenApiConsts.Comment.CREATED)
        LocalDateTime created;
    }
}
