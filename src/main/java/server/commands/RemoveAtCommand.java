package server.commands;

import common.Request;
import common.Response;
import common.routeClasses.Route;
import server.CollectionManager;
import server.DatabaseManager;

/**
 * Класс, реализующий команду remove_at, удаляющую элемент коллекции по его индексу.
 */

public class RemoveAtCommand extends BaseCommand {

    public RemoveAtCommand(String name, String description, CollectionManager manager, DatabaseManager databaseManager) {
        super(name, description, manager, databaseManager);
    }

    /**
     * Метод, реализующий логику команды remove_at.
     *
     * @param request - объект класса Request
     */

    public Response execute(Request request) {
        try {
            long id = collectionManager.getElementIdAtIndex(Integer.parseInt(request.getArgs()[0]));
            boolean isDeleted = databaseManager.removeRouteById(id, request.getUsername());
            if (isDeleted) {
                collectionManager.removeElementAt(Integer.parseInt(request.getArgs()[0]));
                return new Response("Элемент успешно удален", true);
            }
            return new Response("Элемент на позиции " + Integer.parseInt(request.getArgs()[0]) +
                        " не обновлен. Возможно, его не существует или у Вас нет прав его модификации", false);
        } catch (ArrayIndexOutOfBoundsException e) {
            return new Response("Элемента с таким индексом не существует. Проверьте, что это число больше 0 и меньше размера коллекции", false);
        }
    }
}
