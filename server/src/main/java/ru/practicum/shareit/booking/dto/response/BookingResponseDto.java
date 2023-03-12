package ru.practicum.shareit.booking.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class BookingResponseDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private UserDto booker;
    private ItemDto item;

    @Setter
    @Getter
    @NoArgsConstructor
    public static class ItemDto {
        private Long id;
        private String name;

        public static ItemDto fromItem(Item item) {
            var itemDto = new ItemDto();

            itemDto.setId(item.getId());
            itemDto.setName(item.getName());

            return itemDto;
        }
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class UserDto {
        private Long id;

        public static UserDto fromUser(User user) {
            var userDto = new UserDto();

            userDto.setId(user.getId());

            return userDto;
        }
    }
}
