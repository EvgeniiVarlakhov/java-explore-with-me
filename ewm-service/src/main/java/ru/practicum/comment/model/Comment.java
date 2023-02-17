package ru.practicum.comment.model;

import lombok.*;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@ToString
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "text_comment", length = 2000)
    private String text;

    @Column(name = "event_id", nullable = false)
    private int eventId;

    @ManyToOne
    @JoinColumn(name = "author_ID", nullable = false)
    private User author;

    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime;

}
