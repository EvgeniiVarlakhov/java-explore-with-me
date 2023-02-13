package ru.practicum.compilation.model;

import lombok.*;
import ru.practicum.event.model.Event;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compilations")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pinned")
    private boolean pinned;

    @Column(name = "title")
    private String title;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "compilations_events",
            joinColumns = {@JoinColumn(name = "comp_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    List<Event> events = new ArrayList<>();

}
