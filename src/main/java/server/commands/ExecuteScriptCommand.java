package server.commands;

import server.CollectionManager;

/**
 * Класс, реализующий команду execute_script, считывающую и исполняющую скрипт из указанного файла
 */

public class ExecuteScriptCommand extends BaseCommand {

    public ExecuteScriptCommand(String name, String description, CollectionManager manager) {
        super(name, description, manager);
    }

    /**
     * Метод, реализующий логику команды execute_script
     *
     * @param commandParts название команды и ее аргументы (файл, из которого нужно считать и исполнить скрипт)
     */

    public String execute(String[] commandParts) {
        return "Execute_script has leaked into the server. Please, contact the developers.";
    }
}
