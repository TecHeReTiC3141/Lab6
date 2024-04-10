package server.commands;

import common.Request;
import common.Response;
import common.routeClasses.Route;
import server.CollectionManager;

/**
 * Класс, предоставляющий метод для добавления элемента в коллекцию.
 * Элемент может быть как добавлен пользователем вручную, так и считан из файла или из строки скрипта.
 */
public class AddCommand extends BaseCommand {


    public AddCommand(String name, String description, CollectionManager manager) {
        super(name, description, manager);
    }

    /**
     * Метод, считывающий route и его в коллекци.
     *
     * @param request - объект класса Request
     */

    public Response execute(Request request) {
        return new Response(manager.putToCollection(request.getRoute(), false));
    }

}
