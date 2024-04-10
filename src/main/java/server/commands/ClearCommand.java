package server.commands;

import common.Request;
import common.Response;
import common.routeClasses.Route;
import server.CollectionManager;

/**
 * Класс, реализующий команду clear, очищающую коллекцию.
 */

public class ClearCommand extends BaseCommand {

    public ClearCommand(String name, String description, CollectionManager manager) {
        super(name, description, manager);
    }

    /**
     * Метод, очищающий коллекцию.
     *
     * @param request - объект класса Request
     */

    public Response execute(Request request) {
        manager.clearCollection();
        return new Response("Коллекция очищена");
    }
}
