package common.exceptions;

/**
 * Исключение, выбрасываемое при попытке создания объекта Route без указания обязательных параметров.
 */
public class AbsentRequiredParametersException extends ValidationException {

    /**
     * Instantiates a new Absent required parameters exception.
     *
     * @param message the message
     */
    public AbsentRequiredParametersException(String message) {
        super(message);
    }
}
