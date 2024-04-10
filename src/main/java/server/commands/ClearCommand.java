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
        collectionManager.clearCollection();
        return new Response("Коллекция очищена");
    }
}
