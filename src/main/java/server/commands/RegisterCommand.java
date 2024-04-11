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
        boolean ok = databaseManager.registerUser(request.getArgs()[0], request.getArgs()[1]);
        return new Response(ok ? "Пользователь %s успешно зарегистрирован".formatted(request.getArgs()[0])
                : "Не удалось зарегистрировать пользователя", ok);
    }
}
