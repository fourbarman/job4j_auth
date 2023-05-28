package ru.job4j.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

/**
 * ApiException.
 * <p>
 * Exception payload.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 28.05.2023.
 */
@Data
@RequiredArgsConstructor
public class ApiException {
    private final String message;
    private final ZonedDateTime timestamp;
    private final HttpStatus httpStatus;
}
