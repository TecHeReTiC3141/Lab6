package server.commands;

import common.Request;
import common.Response;
import server.CollectionManager;
import server.DatabaseManager;

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
    protected CollectionManager collectionManager;
    protected DatabaseManager databaseManager;

    protected final boolean needsAuthed = false;


    /**
     * Конструктор класса.
     *
     * @param name        название команды
     * @param description описание команды
     * @param collectionManager   менеджер коллекции
     */
    public BaseCommand(String name, String description, CollectionManager collectionManager, DatabaseManager databaseManager) {
        this.name = name;
        this.description = description;
        this.collectionManager = collectionManager;
        this.databaseManager = databaseManager;
    }

    public BaseCommand(String name, String description) {
        this.name = name;
        this.description = description;
        this.collectionManager = null;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public boolean getNeedsAuthed() {
        return needsAuthed;
    }

    /**
     * Метод, в котором выполняется логика команды.
     *
     * @param request объект класса Request
     */
    public Response execute(Request request) {
        return new Response();
    }


}
