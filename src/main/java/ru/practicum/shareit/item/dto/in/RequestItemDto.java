package ru.practicum.shareit.item.dto.in;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item.CreationInfo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Setter
@Getter
@NoArgsConstructor
public class RequestItemDto {
    @NotEmpty(groups = {CreationInfo.class})
    private String name;

    @NotEmpty(groups = {CreationInfo.class})
    private String description;

    @NotNull(groups = {CreationInfo.class})
    private Boolean available;

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public Optional<Boolean> getAvailable() {
        return Optional.ofNullable(available);
    }
}
