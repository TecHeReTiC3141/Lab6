package server.commands;

import common.Request;
import common.Response;
import server.CollectionManager;
import server.DatabaseManager;

/**
 * Класс, объекты которого обновляют элемент коллекции по его id, заменяя его другим элементом.
 */

public class UpdateByIdCommand extends BaseCommand {

    public UpdateByIdCommand(String name, String description, CollectionManager manager, DatabaseManager databaseManager) {
        super(name, description, manager, databaseManager);
    }

    /**
     * Метод, реализующий логику команды update.
     *
     * @param request - объект класса Request
     */
    public Response execute(Request request) {
        long id = Long.parseLong(request.getArgs()[0]);
        boolean isUpdated = databaseManager.updateRouteById(request.getRoute(), id, request.getUsername());
        if (!isUpdated) {
            return new Response("Элемент с id " + id +
                    " не обновлен. Возможно, его не существует или у Вас нет прав его модифицикации .", false);
        }
        request.getRoute().setId(id);
        return new Response(collectionManager.updateElementById(id, request.getRoute()), true);

    }
}
