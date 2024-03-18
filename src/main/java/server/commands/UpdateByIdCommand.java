package server.commands;

import common.routeClasses.Route;
import server.CollectionManager;

/**
 * Класс, объекты которого обновляют элемент коллекции по его id, заменяя его другим элементом.
 */

public class UpdateByIdCommand extends BaseCommand {

    public UpdateByIdCommand(String name, String description, CollectionManager manager) {
        super(name, description, manager);
    }

    /**
     * Метод, реализующий логику команды update.
     *
     * @param commandParts массив, содержащий название аргументы команды
     */
    public String execute(String[] commandParts, Route route) {
        long id = Long.parseLong(commandParts[0]);
        boolean isFound = manager.findElementById(id);
        if (!isFound) {
            return "Элемент с id " + id + " не найден. Обновление не выполнено.";
        }
        route.setId(id);
        return manager.updateElementById(id, route);

    }
}
