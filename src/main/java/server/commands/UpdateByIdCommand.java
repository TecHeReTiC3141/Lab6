package server.commands;

import common.Request;
import common.Response;
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
     * @param request - объект класса Request
     */
    public Response execute(Request request) {
        long id = Long.parseLong(request.getArgs()[0]);
        boolean isFound = manager.findElementById(id);
        if (!isFound) {
            return new Response("Элемент с id " + id + " не найден. Обновление не выполнено.");
        }
        request.getRoute().setId(id);
        return new Response(manager.updateElementById(id, request.getRoute()));

    }
}
