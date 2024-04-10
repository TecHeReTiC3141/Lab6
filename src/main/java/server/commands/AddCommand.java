package server.commands;

import common.Request;
import common.Response;
import server.CollectionManager;
import server.DatabaseManager;

/**
 * Класс, предоставляющий метод для добавления элемента в коллекцию.
 * Элемент может быть как добавлен пользователем вручную, так и считан из файла или из строки скрипта.
 */
public class AddCommand extends BaseCommand {


    public AddCommand(String name, String description, CollectionManager manager, DatabaseManager databaseManager) {
        super(name, description, manager, databaseManager);
    }

    /**
     * Метод, считывающий route и его в коллекци.
     *
     * @param request - объект класса Request
     */

    public Response execute(Request request) {
        return new Response(collectionManager.putToCollection(request.getRoute(), false));
    }

}
