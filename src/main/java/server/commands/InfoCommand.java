package server.commands;

import common.Request;
import common.Response;
import common.routeClasses.Route;
import server.CollectionManager;

/**
 * Класс, реализующий команду info, выводящую информацию о коллекции (тип, дата инициализации, количество элементов).
 */

public class InfoCommand extends BaseCommand {


    public InfoCommand(String name, String description, CollectionManager manager) {
        super(name, description, manager);
    }

    /**
     * Метод, реализующий логику команды info
     *
     * @param request - объект класса Request

     */
    public Response execute(Request request) {
        return new Response(
                "Тип коллекции: " + manager.getCollectionClassName() + "\n" +
                        "Дата инициализации: " + manager.getInitDate() + "\n" +
                        "Количество элементов: " + manager.getCollectionSize()
        );
    }
}
