package ru.practicum.category.dto;

import lombok.*;
import ru.practicum.category.CreateCat;

import javax.validation.constraints.NotBlank;

@ToString
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@RequiredArgsConstructor
public class NewCategoryDto {

    @NotBlank(message = "Name не может быть пустым.", groups = {CreateCat.class})
    private String name;
}
