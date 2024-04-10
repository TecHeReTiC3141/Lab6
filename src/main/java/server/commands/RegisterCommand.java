package server.commands;

import common.Request;
import common.Response;

public class RegisterCommand extends AuthCommand {

        public RegisterCommand(String name, String description) {
            super(name, description);
        }

        public Response execute(Request request) {
            return new Response("Register command executed");
        }
}
