package server.commands;

import common.Request;
import common.Response;
import common.routeClasses.Route;
import server.CollectionManager;

/**
 * Класс, реализующий команду reorder, которая изменяет порядок элементов коллекции на обратный.
 */

public class ReorderCommand extends BaseCommand {


    public ReorderCommand(String name, String description, CollectionManager manager) {
        super(name, description, manager);
    }

    /**
     * Метод, изменяющий порядок элементов коллекции на обратный.
     *
     *      * @param request - объект класса Request
     */

    public Response execute(Request request) {
        manager.reorder();
        return new Response("Порядок элементов коллекции изменен на обратный");
    }
}
