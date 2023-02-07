package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private StackTraceElement[] errors;
    private String status;
    private String reason;
    private String massage;
    private String timestamp;
}