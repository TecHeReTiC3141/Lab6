package client.consoles;

/**
 * Консоль, отвечающая за чтение команд пользователя из консоли.
 */

public class SystemInConsole extends BaseConsole {

    /**
     * Конструктор консоли
     *
     */
    public SystemInConsole() {
        super(System.in);
    }

}
