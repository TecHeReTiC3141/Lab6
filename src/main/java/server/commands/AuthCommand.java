package server.commands;

import common.Request;
import common.Response;
import common.routeClasses.Route;
import server.CollectionManager;

public class AuthCommand extends BaseCommand {

    protected final boolean needsAuthed = true;

    public AuthCommand(String name, String description) {
        super(name, description);
    }

    public Response execute(Request request) {
        return new Response("Auth command executed");
    }

}
