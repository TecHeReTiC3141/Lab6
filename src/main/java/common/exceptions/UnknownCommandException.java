package common.exceptions;

/**
 * Исключение, выбрасываемое при вводе неизвестной команды.
 */
public class UnknownCommandException extends ValidationException {

    /**
     * Instantiates a new Unknown command exception.
     *
     * @param command the command
     */
    public UnknownCommandException(String command) {
        super("Неизвестная команда: %s. Напиши help для получения списка доступных команд".formatted(command));
    }
}
