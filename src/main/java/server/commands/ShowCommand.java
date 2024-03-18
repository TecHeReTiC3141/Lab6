package server.commands;

import common.routeClasses.Route;
import server.CollectionManager;

/**
 * Класс, реализующий команду show, которая выводит содержимое коллекции.
 */

public class ShowCommand extends BaseCommand {

    public ShowCommand(String name, String description, CollectionManager manager) {
        super(name, description, manager);
    }

    /**
     * Метод, реализующий логику команду show.
     *
     * @param commandParts название и аргументы команды
     */

    public String execute(String[] commandParts, Route route) {
        return manager.showCollection();
    }
}
