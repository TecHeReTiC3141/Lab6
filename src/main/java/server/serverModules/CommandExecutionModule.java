package server.serverModules;

import common.Request;
import common.Response;
import server.CollectionManager;
import server.commands.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс, запускающий команды, введенные пользователем
 */

public class CommandExecutionModule {

    /**
     * Map, содержащий все команды, доступные пользователю
     */
    private final Map<String, BaseCommand> commands;

    public CommandExecutionModule(CollectionManager manager) {
        commands = new HashMap<>() {
            {
                put("info", new InfoCommand("info", "вывести информацию о коллекции", manager));
                put("login", new RegisterCommand("login {username} {password}", "авторизоваться в системе"));
                put("register", new RegisterCommand("register {username} {password}", "создать новый профиль в системе"));
                put("show", new ShowCommand("show", "вывести все элементы коллекции", manager));
                put("add", new AddCommand("add {element}", "добавить новый элемент в коллекцию", manager));
                put("update", new UpdateByIdCommand("update id {element}", "обновить значение элемента коллекции, id которого равен заданному", manager));
                put("remove_by_id", new RemoveByIdCommand("remove_by_id id", "удалить элемент из коллекции по его id", manager));
                put("clear", new ClearCommand("clear", "очистить коллекцию", manager));
                put("remove_at", new RemoveAtCommand("remove_at index", "удалить элемент с заданным номером", manager));
                put("reorder", new ReorderCommand("reorder", "отсортировать коллекцию в порядке возрастания", manager));
                put("sort", new SortCommand("sort", "отсортировать коллекцию в порядке убывания", manager));
                put("count_greater_than_distance", new CountGreaterThanDistanceCommand("count_greater_than_distance distance", "вывести количество элементов, значение поля distance которых больше заданного", manager));
                put("print_ascending", new PrintAscendingCommand("print_ascending", "вывести элементы коллекции в естественном порядке возрастания", manager));
                put("print_field_descending_distance", new PrintFieldDescendingDistanceCommand("print_field_descending_distance", "вывести значения поля distance в порядке убывания", manager));
                put("exit", new ExitCommand("exit", "Выйти из программы (без сохранения коллекции в файл)", manager));
                put("execute_script", new ExecuteScriptCommand("execute_script", "считать и исполнить скрипт из указанного файла", manager));
            }
        };
        HelpCommand help = new HelpCommand("help", "вывести справку по доступным командам", manager);
        help.setCommands(new ArrayList<BaseCommand>(commands.values()));
        commands.put("help", help);
    }

    /**
     * Метод, обрабатывающий команду, введенную пользователем
     *
     * @return true, если команда была успешно обработана, иначе false
     */

    public Response processCommand(Request request) { // TODO: replace arguments with Request + in all commands
        BaseCommand command = commands.get(request.getCommand());
        if (command.getNeedsAuthed() && !request.getIsLoggedIn()) {
            return new Response("Вы должны авторизоваться, чтобы выполнить эту команду", false);
        }
        return command.execute(request);
    }
}
