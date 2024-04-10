package server.commands;

import common.Request;
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
     * @param request - объект класса Request
     */

    public Response execute(Request request) {
        long id = Long.parseLong(request.getArgs()[0]);
        return new Response(manager.removeById(id));
    }
}
