package server.commands;


import common.Request;
import common.Response;
import common.routeClasses.Route;

/**
 * Интерфейс, содержащий методы, обязательные для реализации во всех классах-командах.
 */
public interface ICommand {

    /**
     * Метод, возвращающий название команды.
     *
     * @return название команды
     */
    String getName();

    /**
     * Метод, возвращающий описание команды.
     *
     * @return описание команды
     */
    String getDescription();

    /**
     * Метод, в котором выполняется логика команды.
     *
     * @param request - объект класса Request
     */
    Response execute(Request request);

}
