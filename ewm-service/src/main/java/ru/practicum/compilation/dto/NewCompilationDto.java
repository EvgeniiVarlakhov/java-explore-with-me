package ru.practicum.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@ToString
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@RequiredArgsConstructor
public class NewCompilationDto {
    private List<Integer> events;
    private Boolean pinned;

    @NotNull
    private String title;
}
