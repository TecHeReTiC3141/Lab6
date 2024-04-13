package server.commands;

import common.Request;
import common.Response;
import server.CollectionManager;
import server.DatabaseManager;

/**
 * Класс, реализующий команду clear, очищающую коллекцию.
 */

public class ClearCommand extends BaseCommand {

    public ClearCommand(String name, String description, CollectionManager manager, DatabaseManager databaseManager) {
        super(name, description, manager, databaseManager);
    }

    /**
     * Метод, очищающий коллекцию.
     *
     * @param request - объект класса Request
     */

    public Response execute(Request request) {
        boolean areDeleted = databaseManager.removeAllUserRoutes(request.getUsername());
        if (areDeleted) {
            long userId = databaseManager.getUserId(request.getUsername());
            collectionManager.removeAllUserRoutes(userId);
            return new Response("Все ваши маршруты были успешно удалены из коллекции");
        }
        return new Response("Коллекция не была очищена от ваших маршрутов. Возможно, у Вас нет прав на удаление элементов коллекции");
    }
}
