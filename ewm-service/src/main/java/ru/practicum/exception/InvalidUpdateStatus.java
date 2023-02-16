package ru.practicum.exception;

public class InvalidUpdateStatus extends RuntimeException {

    public InvalidUpdateStatus(String message) {
        super(message);
    }

}
