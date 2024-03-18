package common.exceptions;

/**
 * Исключение, выбрасываемое при вводе неверного количества аргументов.
 */
public class WrongArgumentsException extends ValidationException {

    /**
     * Instantiates a new Wrong arguments exception.
     *
     * @param message the message
     */
    public WrongArgumentsException(String message) {
        super(message);
    }
}
