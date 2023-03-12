package ru.practicum.shareit.request.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ItemRequestDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private Iterable<ItemDto> items;

    @Setter
    @Getter
    @NoArgsConstructor
    @SuppressWarnings("java:S2972")
    public static class ItemDto {
        private Long id;
        private String name;
        private String description;
        private Boolean available;
        private Long requestId;

        public static ItemDto fromItem(Item item) {
            var itemDto = new ItemDto();

            itemDto.setId(itemDto.getId());
            itemDto.setName(item.getName());
            itemDto.setDescription(item.getDescription());
            itemDto.setAvailable(item.getIsAvailable());
            itemDto.setRequestId(item.getItemRequest().getId());

            return itemDto;
        }

        public static List<ItemDto> fromItem(Iterable<Item> items) {
            var itemDtos = new ArrayList<ItemDto>();

            for (Item item : items) {
                var itemDto = fromItem(item);
                itemDtos.add(itemDto);
            }

            return itemDtos;
        }
    }
}
