package common.exceptions;

/**
 * Исключение, выбрасываемое при попытке установить невалидное значение дистанции.
 */
public class InvalidDistanceException extends ValidationException {
    public InvalidDistanceException() {
        super("Дистанция должна быть положительным вещественным числом");
    }
}
