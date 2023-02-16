package ru.practicum.user.dto;

import lombok.*;
import ru.practicum.user.CreateUser;
import ru.practicum.user.UpdateUser;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class UserDto {
    @Email(message = "Email имеет неправильный формат.", groups = {CreateUser.class, UpdateUser.class})
    @NotBlank(message = "Email не может быть пустым.", groups = {CreateUser.class})
    private String email;

    @Positive
    private Integer id;

    @NotBlank(message = "Name не может быть пустым.", groups = {CreateUser.class})
    private String name;
}
