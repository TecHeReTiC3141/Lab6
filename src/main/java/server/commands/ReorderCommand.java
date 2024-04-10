package server.commands;

import common.Request;
import common.Response;
import server.CollectionManager;
import server.DatabaseManager;

/**
 * Класс, реализующий команду reorder, которая изменяет порядок элементов коллекции на обратный.
 */

public class ReorderCommand extends BaseCommand {

    public ReorderCommand(String name, String description, CollectionManager manager, DatabaseManager databaseManager) {
        super(name, description, manager, databaseManager);
    }

    /**
     * Метод, изменяющий порядок элементов коллекции на обратный.
     *
     *      * @param request - объект класса Request
     */

    public Response execute(Request request) {
        collectionManager.reorder();
        return new Response("Порядок элементов коллекции изменен на обратный");
    }
}
