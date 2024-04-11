package server.commands;

import common.Request;
import common.Response;
import server.CollectionManager;
import server.DatabaseManager;

public class LoginCommand extends AuthCommand {

    public LoginCommand(String name, String description, CollectionManager manager, DatabaseManager databaseManager) {
        super(name, description, manager, databaseManager);
    }
    public Response execute(Request request) {
        boolean ok = databaseManager.checkCredentials(request.getArgs()[0], request.getArgs()[1]);
        return new Response(ok ? "Пользователь %s успешно авторизован".formatted(request.getArgs()[0]) : "Проверьте учетные данные", ok);    }
}
