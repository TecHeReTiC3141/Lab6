package server.serverModules;

import common.Request;
import common.Response;
import server.CollectionManager;
import server.DatabaseManager;
import server.commands.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Класс, запускающий команды, введенные пользователем
 */

public class CommandExecutionModule {

    /**
     * Map, содержащий все команды, доступные пользователю
     */
    private final Map<String, BaseCommand> commands;

    public CommandExecutionModule(CollectionManager collectionManager, DatabaseManager databaseManager) {
        commands = new HashMap<>() {
            {
                put("info", new InfoCommand("info", "вывести информацию о коллекции", collectionManager, databaseManager));
                put("login", new LoginCommand("login {username} {password}", "авторизоваться в системе", collectionManager, databaseManager));
                put("register", new RegisterCommand("register {username} {password}", "создать новый профиль в системе", collectionManager, databaseManager));
                put("show", new ShowCommand("show", "вывести все элементы коллекции", collectionManager, databaseManager));
                put("add", new AddCommand("add {element}", "добавить новый элемент в коллекцию", collectionManager, databaseManager));
                put("update", new UpdateByIdCommand("update id {element}", "обновить значение элемента коллекции, id которого равен заданному", collectionManager, databaseManager));
                put("remove_by_id", new RemoveByIdCommand("remove_by_id id", "удалить элемент из коллекции по его id", collectionManager, databaseManager));
                put("clear", new ClearCommand("clear", "очистить коллекцию", collectionManager, databaseManager));
                put("remove_at", new RemoveAtCommand("remove_at index", "удалить элемент с заданным номером", collectionManager, databaseManager));
                put("reorder", new ReorderCommand("reorder", "отсортировать коллекцию в порядке возрастания", collectionManager, databaseManager));
                put("sort", new SortCommand("sort", "отсортировать коллекцию в порядке убывания", collectionManager, databaseManager));
                put("count_greater_than_distance", new CountGreaterThanDistanceCommand("count_greater_than_distance distance", "вывести количество элементов, значение поля distance которых больше заданного", collectionManager, databaseManager));
                put("print_ascending", new PrintAscendingCommand("print_ascending", "вывести элементы коллекции в естественном порядке возрастания", collectionManager, databaseManager));
                put("print_field_descending_distance", new PrintFieldDescendingDistanceCommand("print_field_descending_distance", "вывести значения поля distance в порядке убывания", collectionManager, databaseManager));
                put("exit", new ExitCommand("exit", "Выйти из программы (без сохранения коллекции в файл)", collectionManager, databaseManager));
                put("execute_script", new ExecuteScriptCommand("execute_script", "считать и исполнить скрипт из указанного файла", collectionManager, databaseManager));
            }
        };
        HelpCommand help = new HelpCommand("help", "вывести справку по доступным командам", collectionManager, databaseManager);
        help.setCommands(new ArrayList<BaseCommand>(commands.values()));
        commands.put("help", help);
    }

    /**
     * Метод, обрабатывающий команду, введенную пользователем
     *
     * @return true, если команда была успешно обработана, иначе false
     */

    public Response handleRequest(Request request) {
        FutureTask<Response> future = new FutureTask<>(new RequestExecutor(request));
        new Thread(future).start();
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            return new Response("Ошибка при выполнении команды", false);
        }
    }

    public synchronized Response processCommand(Request request) {
        BaseCommand command = commands.get(request.getCommand());
        if (command.getNeedsAuthed() && !request.getIsLoggedIn()) {
            return new Response("Вы должны авторизоваться, чтобы выполнить эту команду", false);
        }
        return command.execute(request);
    }

    private class RequestExecutor implements Callable<Response> {
        private final Request request;

        public RequestExecutor(Request request) {
            this.request = request;
        }

        public Response call() {
            return processCommand(request);
        }
    }
}
