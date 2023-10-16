package ru.practicum.shareit.server.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.server.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_comments")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_comment_id")
    Long id;

    @Column(length = 1000, nullable = false)
    String text;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id")
    User author;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id")
    Item item;

    @CreationTimestamp
    @Column(name = "creation_time", nullable = false)
    LocalDateTime created;
}
