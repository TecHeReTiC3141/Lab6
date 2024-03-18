package common.exceptions;

/**
 * Исключение, выбрасываемое при выходе из программы
 */
public class ExitException extends RuntimeException {

    public ExitException() {
        super("Выход из программы");
    }
}
