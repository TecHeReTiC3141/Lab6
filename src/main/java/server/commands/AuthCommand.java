package server.commands;

import common.Response;
import common.routeClasses.Route;
import server.CollectionManager;

public class AuthCommand extends BaseCommand {

    public AuthCommand(String name, String description) {
        super(name, description);
    }

    public Response execute(String[] commandParts, Route route) {
        return new Response("Auth command executed");
    }

}
