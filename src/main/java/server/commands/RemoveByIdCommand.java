package server.commands;

import common.Request;
import common.Response;
import server.CollectionManager;
import server.DatabaseManager;

/**
 * Класс, предназначенный для удаления элемента коллекции по его id.
 */

public class RemoveByIdCommand extends BaseCommand {

    public RemoveByIdCommand(String name, String description, CollectionManager manager, DatabaseManager databaseManager) {
        super(name, description, manager, databaseManager);
    }

    /**
     * Метод, удаляющий элемент коллекции по его id.
     *
     * @param request - объект класса Request
     */

    public Response execute(Request request) {
        long id = Long.parseLong(request.getArgs()[0]);
        boolean isDeleted = databaseManager.removeRouteById(id, request.getUsername());
        return new Response(isDeleted ? collectionManager.removeById(id) : "Элемент с id " + id +
                " не обновлен. Возможно, его не существует или у Вас нет прав его модификации", isDeleted);
    }
}
