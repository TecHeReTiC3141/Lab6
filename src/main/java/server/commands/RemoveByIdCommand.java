package server.commands;

import common.Response;
import common.routeClasses.Route;
import server.CollectionManager;

/**
 * Класс, предназначенный для удаления элемента коллекции по его id.
 */

public class RemoveByIdCommand extends BaseCommand {

    public RemoveByIdCommand(String name, String description, CollectionManager manager) {
        super(name, description, manager);
    }

    /**
     * Метод, удаляющий элемент коллекции по его id.
     *
     * @param commandParts массив, содержащий название аргументы команды
     */

    public Response execute(String[] commandParts, Route route) {
        long id = Long.parseLong(commandParts[0]);
        return new Response(manager.removeById(id));
    }
}
