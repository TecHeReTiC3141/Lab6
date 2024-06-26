package server.commands;

import common.Request;
import common.Response;
import common.routeClasses.Route;
import server.CollectionManager;
import server.DatabaseManager;

/**
 * Класс команды exit, завершающей программу.
 */

public class ExitCommand extends BaseCommand {


    /**
     * Конструктор класса.
     *
     * @param name        название команды
     * @param description описание команды
     * @param manager   менеджер коллекции
     */
    public ExitCommand(String name, String description, CollectionManager manager, DatabaseManager databaseManager) {
        super(name, description, manager, databaseManager);
    }

    /**
     * Метод, реализующий логику команды exit
     * @param request - объект класса Request

     */
    public Response execute(Request request) {
        return new Response("Завершение работы программы");
    }

}
