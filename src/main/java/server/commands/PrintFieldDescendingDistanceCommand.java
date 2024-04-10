package server.commands;

import common.Request;
import common.Response;
import server.CollectionManager;
import server.DatabaseManager;

/**
 * Класс, реализующий команду print_field_descending_distance,
 * которая выводит значения поля distance элементов коллекции в порядке убывания.
 */

public class PrintFieldDescendingDistanceCommand extends BaseCommand {

    public PrintFieldDescendingDistanceCommand(String name, String description, CollectionManager manager, DatabaseManager databaseManager) {
        super(name, description, manager, databaseManager);
    }

    /**
     * Метод, реализующий логику  команды print_field_descending_distance.
     *
     * @param request - объект класса Request

     */

    public Response execute(Request request) {
        return new Response(collectionManager.printDescendingDistance());
    }
}
