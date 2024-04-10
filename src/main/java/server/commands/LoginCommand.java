package server.commands;

import common.Response;
import common.routeClasses.Route;

public class LoginCommand extends AuthCommand {

    public LoginCommand(String name, String description) {
        super(name, description);
    }

    public Response execute(String[] commandParts, Route route) {
        return new Response("Login command executed");
    }
}
