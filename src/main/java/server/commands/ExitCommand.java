package server.commands;

import common.routeClasses.Route;
import server.CollectionManager;

/**
 * Класс команды exit, завершающей программу.
 */

public class ExitCommand extends BaseCommand {


    /**
     * Конструктор класса.
     *
     * @param name        название команды
     * @param description описание команды
     * @param manager   менеджер коллекции
     */
    public ExitCommand(String name, String description, CollectionManager manager) {
        super(name, description, manager);
    }

    /**
     * Метод, реализующий логику команды exit
     * @param commandParts название команды и ее аргументы
     */
    public String execute(String[] commandParts, Route route) {

        return "Client exited";
    }

}
