package server.commands;

import common.Request;
import common.Response;
import server.CollectionManager;
import server.DatabaseManager;

/**
 * Класс, реализующий команду info, выводящую информацию о коллекции (тип, дата инициализации, количество элементов).
 */

public class InfoCommand extends BaseCommand {


    public InfoCommand(String name, String description, CollectionManager manager, DatabaseManager databaseManager) {
        super(name, description, manager, databaseManager);
    }

    /**
     * Метод, реализующий логику команды info
     *
     * @param request - объект класса Request

     */
    public Response execute(Request request) {
        return new Response(
                "Тип коллекции: " + collectionManager.getCollectionClassName() + "\n" +
                        "Дата инициализации: " + collectionManager.getInitDate() + "\n" +
                        "Количество элементов: " + collectionManager.getCollectionSize()
        );
    }
}
