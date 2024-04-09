package server.commands;

import common.Response;
import common.routeClasses.Route;
import server.CollectionManager;

/**
 * Абстрактный класс, реализующий интерфейс ICommand.
 */
public abstract class BaseCommand implements ICommand {

    /**
     * The Name.
     */
    protected final String name;
    /**
     * The Description.
     */
    protected final String description;

    /**
     * The Collection.
     */
    protected CollectionManager manager;


    /**
     * Конструктор класса.
     *
     * @param name        название команды
     * @param description описание команды
     * @param manager   менеджер коллекции
     */
    public BaseCommand(String name, String description, CollectionManager manager) {
        this.name = name;
        this.description = description;
        this.manager = manager;
    }

    public BaseCommand(String name, String description) {
        this.name = name;
        this.description = description;
        this.manager = null;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    /**
     * Метод, в котором выполняется логика команды.
     *
     * @param commandParts массив, содержащий название и аргументы команды
     */
    public Response execute(String[] commandParts, Route route) {
        return new Response();
    }

    /**
     * Метод, в котором выполняется логика команды, при этом нужно обработать передаваемые значения.
     *
     * @param commandParts массив, содержащий название и аргументы команды
     * @param parse флаг, указывающий, нужно ли парсить аргументы команды
     */
    public Response execute(String[] commandParts, Route route, boolean parse) {
        return new Response();
    }
}
