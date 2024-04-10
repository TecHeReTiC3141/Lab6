package server.commands;

import common.Request;
import common.Response;
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

    protected final boolean needsAuthed = false;


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
