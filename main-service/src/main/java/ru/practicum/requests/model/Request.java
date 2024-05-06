package ru.practicum.requests.model;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.*;
import ru.practicum.events.model.Event;
import ru.practicum.statuses.RequestStatus;
import ru.practicum.users.model.User;

@Entity
@Table(name = "requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    @ToString.Exclude
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @ToString.Exclude
    private Event event;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
