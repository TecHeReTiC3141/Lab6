package server.commands;

import common.Request;
import common.Response;
import common.routeClasses.Route;
import server.CollectionManager;
import server.DatabaseManager;

public class AuthCommand extends BaseCommand {

    protected final boolean needsAuthed = false;

    public AuthCommand(String name, String description, CollectionManager manager, DatabaseManager databaseManager) {
        super(name, description, manager, databaseManager);
    }

    public Response execute(Request request) {
        return new Response("Auth command executed");
    }

}
