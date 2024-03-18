package client;

import common.routeClasses.Route;

import java.io.Serializable;
import java.util.Arrays;

public class Request implements Serializable {

    private final String command;

    private final String[] args;

    private final Route route;

    public Request(String command, String[] args, Route route) {
        this.command = command;
        this.args = args;
        this.route = route;
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

    public String toString() {
        return "Request{" +
                "command='" + command + '\'' +
                ", args=" + Arrays.toString(args) +
                ", route=" + route +
                '}';
    }
}
