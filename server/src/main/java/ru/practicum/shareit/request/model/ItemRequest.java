package ru.practicum.shareit.request.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Setter
@Getter
@NoArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, name = "creation_time")
    private LocalDateTime creationTime;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "requester_id")
    private User requester;
}
