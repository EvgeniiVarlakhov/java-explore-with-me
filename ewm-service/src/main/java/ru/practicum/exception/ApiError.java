package ru.practicum.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ApiError {
    private StackTraceElement[] errors;
    private String status;
    private String reason;
    private String massage;
    private String timestamp;
}