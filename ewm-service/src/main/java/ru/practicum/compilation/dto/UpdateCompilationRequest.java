package ru.practicum.compilation.dto;

import lombok.*;

import java.util.Set;

@ToString
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@RequiredArgsConstructor
public class UpdateCompilationRequest {
    private Set<Integer> events;
    private Boolean pinned;
    private String title;
}
