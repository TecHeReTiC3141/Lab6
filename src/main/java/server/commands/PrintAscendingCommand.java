package server.commands;

import common.Request;
import common.Response;
import common.routeClasses.Route;
import server.CollectionManager;

/**
 * Класс, реализующий команду print_ascending, которая выводит элементы коллекции, отсортированные в естественном порядке.
 */
public class PrintAscendingCommand extends BaseCommand {

    public PrintAscendingCommand(String name, String description, CollectionManager manager) {
        super(name, description, manager);
    }

    /**
     * Метод, реализующий логику команды print_ascending.
     *
     * @param request - объект класса Request
     */

    public Response execute(Request request) {

        return new Response(manager.printAscendingCommand());
    }
}
