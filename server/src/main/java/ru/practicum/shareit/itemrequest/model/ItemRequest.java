package ru.practicum.shareit.itemrequest.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_request_id")
    Long id;

    @Column(length = 1000, nullable = false)
    String description;

    @CreationTimestamp
    @Column(name = "creation_time", nullable = false)
    LocalDateTime created;

    @ManyToOne(optional = false)
    @JoinColumn(name = "requester_id")
    User requester;
}
