package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class StatsDtoIn {
    @NotNull(message = "Следует указать app.")
    String app;

    @NotNull(message = "Следует указать uri.")
    String uri;

    @NotNull(message = "Следует указать ip.")
    String ip;

    @NotNull(message = "Следует указать timestamp.")
    String timestamp;
}
