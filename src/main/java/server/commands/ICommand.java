package server.commands;


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
     * @param commandParts массив, содержащий название и аргументы команды
     */
    String execute(String[] commandParts, Route route);

    /**
     * Метод, в котором выполняется логика команды, при этом нужно обработать передаваемые значения.
     *
     * @param commandParts массив, содержащий название и аргументы команды
     * @param parse флаг, указывающий, нужно ли парсить аргументы команды
     */
    String execute(String[] commandParts, Route route, boolean parse);
}
