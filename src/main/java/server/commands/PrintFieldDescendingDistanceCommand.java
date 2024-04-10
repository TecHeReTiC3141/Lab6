package server.commands;

import common.Request;
import common.Response;
import common.routeClasses.Route;
import server.CollectionManager;

/**
 * Класс, реализующий команду print_field_descending_distance,
 * которая выводит значения поля distance элементов коллекции в порядке убывания.
 */

public class PrintFieldDescendingDistanceCommand extends BaseCommand {

    public PrintFieldDescendingDistanceCommand(String name, String description, CollectionManager manager) {
        super(name, description, manager);
    }

    /**
     * Метод, реализующий логику  команды print_field_descending_distance.
     *
     * @param request - объект класса Request

     */

    public Response execute(Request request) {
        return new Response(manager.printDescendingDistance());
    }
}
