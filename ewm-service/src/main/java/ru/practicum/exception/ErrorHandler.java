package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.Constant;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidValidationException(final InvalidValidationException e) {
        log.error("400 {}", e.getMessage(), e);
        return new ApiError(null,
                HttpStatus.BAD_REQUEST.toString(),
                "Ошибка валидации",
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleException(final Exception e) {
        log.error("400 {}", e.getMessage(), e);
        return new ApiError(e.getStackTrace(),
                HttpStatus.BAD_REQUEST.toString(),
                e.toString(),
                "Произошла непредвиденная ошибка.",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolationException(final ConstraintViolationException e) {
        log.error("400 {}", e.getMessage(), e);
        return new ApiError(null,
                HttpStatus.BAD_REQUEST.toString(),
                e.toString(),
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.error("409 {}", e.getMessage(), e);
        return new ApiError(null,
                HttpStatus.CONFLICT.toString(),
                e.toString(),
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationError(final MethodArgumentNotValidException e) {
        log.error("400 {}", e.getMessage(), e);
        return new ApiError(null,
                HttpStatus.BAD_REQUEST.toString(),
                e.toString(),
                Objects.requireNonNull(e.getFieldError()).getDefaultMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException e) {
        log.error("400 {}", e.getMessage(), e);
        return new ApiError(null,
                HttpStatus.BAD_REQUEST.toString(),
                "Incorrectly made request",
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleObjectNotFoundException(final ObjectNotFoundException e) {
        log.error("404 {}", e.getMessage(), e);
        return new ApiError(null,
                HttpStatus.NOT_FOUND.toString(),
                "The required object was not found",
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleInvalidMadeRequestException(final InvalidMadeRequestException e) {
        log.error("409 {}", e.getMessage(), e);
        return new ApiError(null,
                HttpStatus.CONFLICT.toString(),
                "Incorrect made request.",
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleInvalidUpdateStatusException(final InvalidUpdateStatus e) {
        log.error("409 {}", e.getMessage(), e);
        return new ApiError(null,
                HttpStatus.CONFLICT.toString(),
                "Incorrect made request.",
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
    }

}
