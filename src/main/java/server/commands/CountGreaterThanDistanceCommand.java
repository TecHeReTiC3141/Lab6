package server.commands;

import common.Response;
import common.routeClasses.Route;
import server.CollectionManager;

/**
 * Класс, реализующий команду count_greater_than_distance, которая выводит количество элементов со значением поля distance больше заданного.
 */
public class CountGreaterThanDistanceCommand extends BaseCommand {

    public CountGreaterThanDistanceCommand(String name, String description, CollectionManager manager) {
        super(name, description, manager);
    }

    /**
     * Метод, реализующий логику команды count_greater_than_distance
     *
     * @param commandParts массив, содержащий название и аргументы команды
     */

    public Response execute(String[] commandParts, Route route) {
        if (manager.getIsEmpty()) {
            return new Response("Коллекция пуста", false);
        }
        double distance = Double.parseDouble(commandParts[0]);
        long count = manager.countGreaterThanDistance(distance);
        return new Response("Количество элементов, значение поля distance которых больше %s - %s%n".formatted(distance, count));
    }
}
