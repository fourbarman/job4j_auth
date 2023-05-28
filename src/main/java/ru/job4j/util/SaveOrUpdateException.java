package ru.job4j.util;

/**
 * SaveOrUpdateException.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 28.05.2023.
 */
public class SaveOrUpdateException extends RuntimeException {
    public SaveOrUpdateException(String message) {
        super(message);
    }
}
