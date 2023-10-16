package ru.practicum.shareit.commondto.itemrequest.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.commondto.validation.Groups;

import javax.validation.constraints.NotBlank;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ItemRequestCreationDto {

    @NotBlank(groups = Groups.OnCreate.class)
    String description;
}
