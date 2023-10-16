package ru.practicum.shareit.server.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.itemrequest.model.ItemRequest;
import ru.practicum.shareit.server.user.model.User;

import javax.persistence.*;

@Entity
@Table(name = "items")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    Long id;

    @Column(length = 200, nullable = false)
    String name;

    @Column(length = 1000, nullable = false)
    String description;

    @Column(name = "is_available", nullable = false)
    Boolean isAvailable;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id")
    User owner;

    @ManyToOne
    @JoinColumn(name = "item_request_id")
    ItemRequest itemRequest;
}
