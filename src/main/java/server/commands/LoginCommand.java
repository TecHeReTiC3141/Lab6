package server.commands;

import common.Response;
import common.routeClasses.Route;
import server.CollectionManager;
import server.DatabaseManager;

public class LoginCommand extends AuthCommand {

    public LoginCommand(String name, String description, CollectionManager manager, DatabaseManager databaseManager) {
        super(name, description, manager, databaseManager);
    }
    public Response execute(String[] commandParts, Route route) {
        return new Response("Login command executed");
    }
}
