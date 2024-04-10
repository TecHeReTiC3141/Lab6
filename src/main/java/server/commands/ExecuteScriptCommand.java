package server.commands;

import common.Request;
import common.Response;
import server.CollectionManager;
import server.DatabaseManager;

/**
 * Класс, реализующий команду execute_script, считывающую и исполняющую скрипт из указанного файла
 */

public class ExecuteScriptCommand extends BaseCommand {

    public ExecuteScriptCommand(String name, String description, CollectionManager manager, DatabaseManager databaseManager) {
        super(name, description, manager, databaseManager);
    }

    /**
     * Метод, реализующий логику команды execute_script
     *
     * @param request название команды и ее аргументы (файл, из которого нужно считать и исполнить скрипт)
     */

    public Response execute(Request request) {
        return new Response("Execute_script has leaked into the server. Please, contact the developers.", false);
    }
}
