package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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
    public ErrorResponse handleInvalidValidationException(final InvalidValidationException e) {
        log.error("400 {}", e.getMessage(), e);
        return new ErrorResponse(
                e.getStackTrace(),
                HttpStatus.BAD_REQUEST.toString(),
                e.toString(),
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final Exception e) {
        log.error("500 {}", e.getMessage(), e);
        return new ErrorResponse(e.getStackTrace(),
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),e.toString(), "Произошла непредвиденная ошибка.", LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException e) {
        log.error("400 {}", e.getMessage(), e);
        return new ErrorResponse(e.getStackTrace(),
                HttpStatus.BAD_REQUEST.toString(),e.toString(), e.getMessage(), LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationError(final MethodArgumentNotValidException e) {
        log.error("400 {}", e.getMessage(), e);
        return new ErrorResponse(e.getStackTrace(),
                HttpStatus.BAD_REQUEST.toString(),e.toString(), Objects.requireNonNull(e.getFieldError()).getDefaultMessage(), LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleObjectNotFoundException(final ObjectNotFoundException e) {
        log.error("404 {}", e.getMessage(), e);
        return new ErrorResponse(e.getStackTrace(),
                HttpStatus.NOT_FOUND.toString(), e.fillInStackTrace().toString(), e.getMessage(), LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constant.TIME_FORMAT)));
    }

}
