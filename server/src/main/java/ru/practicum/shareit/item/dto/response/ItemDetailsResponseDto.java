package ru.practicum.shareit.item.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ItemDetailsResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments;

    @Setter
    @Getter
    @NoArgsConstructor
    public static class BookingDto {
        private Long id;
        private Long bookerId;

        public static BookingDto fromBooking(Booking booking) {
            BookingDto bookingDto = new BookingDto();

            bookingDto.setId(bookingDto.getId());
            bookingDto.setBookerId(booking.getBooker().getId());

            return bookingDto;
        }
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class CommentDto {
        private Long id;
        private String authorName;
        private String text;
        private LocalDateTime created;

        public static CommentDto fromComment(Comment comment) {
            CommentDto commentDto = new CommentDto();

            commentDto.setId(comment.getId());
            commentDto.setText(comment.getText());
            commentDto.setAuthorName(comment.getAuthor().getName());
            commentDto.setCreated(comment.getCreated());

            return commentDto;
        }

        public static List<CommentDto> fromComment(Iterable<Comment> comments) {
            List<CommentDto> commentDtos = new ArrayList<>();

            for (Comment comment : comments) {
                CommentDto commentDto = fromComment(comment);
                commentDtos.add(commentDto);
            }

            return commentDtos;
        }
    }
}
