package common;

import common.routeClasses.Route;

import java.io.Serializable;
import java.util.Arrays;

public class Request implements Serializable {

    private final String command;

    private final String[] args;

    private final Route route;

    private String username;

    public Request(String command, String[] args, Route route, String username) {
        this.command = command;
        this.args = args;
        this.route = route;
        this.username = username;
    }

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }

    public Route getRoute() {
        return route;
    }
    public String getUsername() {
        return username;
    }

    public boolean getIsLoggedIn() {
        return username != null;
    }

    public String toString() { // TODO: turn it into a formatted string
        return "Request{command='%s', args=%s, route=%s, from=%s}"
                .formatted(command, Arrays.toString(args), route, username);
    }
}
