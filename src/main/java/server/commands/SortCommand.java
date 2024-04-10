package server.commands;

import common.Request;
import common.Response;
import server.CollectionManager;
import server.DatabaseManager;

/**
 * Класс, реализующий команду sort, которая сортирует коллекцию по возрастанию в естественном порядке.
 */

public class SortCommand extends BaseCommand {

    public SortCommand(String name, String description, CollectionManager manager, DatabaseManager databaseManager) {
        super(name, description, manager, databaseManager);
    }

    /**
     * Метод, реализующий логику команды sort.
     *
     * @param request - объект класса Request

     */
    public Response execute(Request request) {
        collectionManager.sortCollection();
        return new Response("Коллекция успешно отсортирована");
    }
}
