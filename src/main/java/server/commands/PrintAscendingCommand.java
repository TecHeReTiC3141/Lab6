package server.commands;

import common.Request;
import common.Response;
import server.CollectionManager;
import server.DatabaseManager;

/**
 * Класс, реализующий команду print_ascending, которая выводит элементы коллекции, отсортированные в естественном порядке.
 */
public class PrintAscendingCommand extends BaseCommand {

    public PrintAscendingCommand(String name, String description, CollectionManager manager, DatabaseManager databaseManager) {
        super(name, description, manager, databaseManager);
    }

    /**
     * Метод, реализующий логику команды print_ascending.
     *
     * @param request - объект класса Request
     */

    public Response execute(Request request) {

        return new Response(collectionManager.printAscendingCommand());
    }
}
