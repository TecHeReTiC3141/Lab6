package common.exceptions;

public class LostConnectionException extends Exception {
    public LostConnectionException() {
        super("Потеряно соединение с сервером");
    }
}
