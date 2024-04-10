package server.commands;

import common.Request;
import common.Response;
import common.routeClasses.Route;
import server.CollectionManager;

/**
 * Класс, реализующий команду sort, которая сортирует коллекцию по возрастанию в естественном порядке.
 */

public class SortCommand extends BaseCommand {

    public SortCommand(String name, String description, CollectionManager manager) {
        super(name, description, manager);
    }

    /**
     * Метод, реализующий логику команды sort.
     *
     * @param request - объект класса Request

     */
    public Response execute(Request request) {
        manager.sortCollection();
        return new Response("Коллекция успешно отсортирована");
    }
}
