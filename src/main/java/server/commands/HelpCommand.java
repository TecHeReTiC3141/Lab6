package server.commands;

import common.Request;
import common.Response;
import common.routeClasses.Route;
import server.CollectionManager;
import server.DatabaseManager;

import java.util.ArrayList;

/**
 * Класс, реализующий команду help, которая выводит справку по доступным командам.
 */
public class HelpCommand extends BaseCommand {

    /**
     * Список команд для справки
     */
    ArrayList<BaseCommand> commands = new ArrayList<>();

    public HelpCommand(String name, String description, CollectionManager manager, DatabaseManager databaseManager) {
        super(name, description, manager, databaseManager);
    }

    public void setCommands(ArrayList<BaseCommand> commands) {
        this.commands = commands;
    }

    /**
     * Main method.
     */
    public Response execute(Request request) {
        StringBuilder result = new StringBuilder();
        result.append("Список доступных команд:\n");
        for (BaseCommand command : commands) {
            result.append(command.getName() + ": " + command.getDescription() + '\n');
        }
        return new Response(result.toString(), true);
    }
}
