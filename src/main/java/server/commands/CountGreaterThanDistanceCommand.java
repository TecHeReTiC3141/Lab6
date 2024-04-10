package server.commands;

import common.Request;
import common.Response;
import server.CollectionManager;
import server.DatabaseManager;

/**
 * Класс, реализующий команду count_greater_than_distance, которая выводит количество элементов со значением поля distance больше заданного.
 */
public class CountGreaterThanDistanceCommand extends BaseCommand {

    public CountGreaterThanDistanceCommand(String name, String description, CollectionManager manager, DatabaseManager databaseManager) {
        super(name, description, manager, databaseManager);
    }

    /**
     * Метод, реализующий логику команды count_greater_than_distance
     *
     * @param request - объект класса Request
     */

    public Response execute(Request request) {
        if (collectionManager.getIsEmpty()) {
            return new Response("Коллекция пуста", false);
        }
        double distance = Double.parseDouble(request.getArgs()[0]);
        long count = collectionManager.countGreaterThanDistance(distance);
        return new Response("Количество элементов, значение поля distance которых больше %s - %s%n".formatted(distance, count));
    }
}
