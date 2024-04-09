package server.commands;

import common.Response;

public class RegisterCommand extends AuthCommand {

        public RegisterCommand(String name, String description) {
            super(name, description);
        }

        public Response execute(String[] commandParts, Route route) {
            return new Response("Register command executed");
        }
}
