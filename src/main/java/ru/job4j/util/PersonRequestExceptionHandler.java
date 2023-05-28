package ru.job4j.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * PersonRequestExceptionHandler.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 28.05.2023.
 */
@Slf4j
@ControllerAdvice
public class PersonRequestExceptionHandler {

    @ExceptionHandler(value = {PersonNotFoundException.class})
    public ResponseEntity<Object> handle(PersonNotFoundException pnf) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                pnf.getMessage(),
                ZonedDateTime.now(ZoneId.of("UTC")),
                httpStatus);
        log.error(pnf.getMessage(), pnf);
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {SaveOrUpdateException.class})
    public ResponseEntity<Object> handle(SaveOrUpdateException e) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        ApiException apiException = new ApiException(
                e.getMessage(),
                ZonedDateTime.now(ZoneId.of("UTC")),
                httpStatus
        );
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {IllegalStateException.class, NullPointerException.class})
    public ResponseEntity<Object> handle(Exception e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                e.getMessage(),
                ZonedDateTime.now(ZoneId.of("UTC")),
                httpStatus
        );
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(apiException, httpStatus);
    }
}
