package server.commands;

import common.Request;
import common.Response;
import server.CollectionManager;
import server.DatabaseManager;

/**
 * Класс, реализующий команду show, которая выводит содержимое коллекции.
 */

public class ShowCommand extends BaseCommand {

    public ShowCommand(String name, String description, CollectionManager manager, DatabaseManager databaseManager) {
        super(name, description, manager, databaseManager);
    }

    /**
     * Метод, реализующий логику команду show.
     *
     * @param request - объект класса Request
     */

    public Response execute(Request request) {
        return new Response(collectionManager.showCollection());
    }
}
