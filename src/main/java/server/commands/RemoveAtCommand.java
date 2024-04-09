package server.commands;

import common.Response;
import common.routeClasses.Route;
import server.CollectionManager;

/**
 * Класс, реализующий команду remove_at, удаляющую элемент коллекции по его индексу.
 */

public class RemoveAtCommand extends BaseCommand {

    public RemoveAtCommand(String name, String description, CollectionManager manager) {
        super(name, description, manager);
    }

    /**
     * Метод, реализующий логику команды remove_at.
     *
     * @param commandParts массив, содержащий название и аргументы команды
     */

    public Response execute(String[] commandParts, Route route) {
        try {
            manager.removeElementAt(Integer.parseInt(commandParts[0]));
            return new Response("Элемент успешно удален");
        } catch (ArrayIndexOutOfBoundsException e) {
            return new Response("Элемента с таким индексом не существует. Проверьте, что это число больше 0 и меньше размера коллекции", false);
        }
    }
}
