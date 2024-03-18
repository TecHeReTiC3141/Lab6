package server.commands;

import common.routeClasses.Route;
import server.CollectionManager;

import java.util.ArrayList;

/**
 * Класс, реализующий команду help, которая выводит справку по доступным командам.
 */
public class HelpCommand extends BaseCommand {

    /**
     * Список команд для справки
     */
    ArrayList<BaseCommand> commands = new ArrayList<>();

    public HelpCommand(String name, String description, CollectionManager manager) {
        super(name, description, manager);
    }

    public void setCommands(ArrayList<BaseCommand> commands) {
        this.commands = commands;
    }

    /**
     * Main method.
     */
    public String execute(String[] commandParts, Route route) {
        StringBuilder result = new StringBuilder();
        result.append("Список доступных команд:");
        for (BaseCommand command : commands) {
            result.append(command.getName() + ": " + command.getDescription() + '\n');
        }
        return result.toString();
    }
}
