package server.commands;

import common.Request;
import common.Response;
import server.CollectionManager;
import server.DatabaseManager;

public class RegisterCommand extends AuthCommand {

    public RegisterCommand(String name, String description, CollectionManager manager, DatabaseManager databaseManager) {
        super(name, description, manager, databaseManager);
    }

    public Response execute(Request request) {
        return new Response("Register command executed");
    }
}
