package ru.practicum.exception;

public class InvalidMadeRequestException extends RuntimeException {

    public InvalidMadeRequestException(String message) {
        super(message);
    }

}
