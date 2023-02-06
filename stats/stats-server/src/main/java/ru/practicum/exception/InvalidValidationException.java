package ru.practicum.exception;

public class InvalidValidationException extends RuntimeException {

    public InvalidValidationException(String message) {
        super(message);
    }

}