package server.commands;

import common.routeClasses.Route;
import server.CollectionManager;

/**
 * Класс, реализующий команду sort, которая сортирует коллекцию по возрастанию в естественном порядке.
 */

public class SortCommand extends BaseCommand {

    public SortCommand(String name, String description, CollectionManager manager) {
        super(name, description, manager);
    }

    /**
     * Метод, реализующий логику команды sort.
     *
     * @param commandParts массив, содержащий название и аргументы команды
     */
    public String execute(String[] commandParts, Route route) {
        manager.sortCollection();
        return "Коллекция успешно отсортирована";
    }
}
