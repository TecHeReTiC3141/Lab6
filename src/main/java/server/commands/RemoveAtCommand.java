package server.commands;

import common.Request;
import common.Response;
import server.CollectionManager;
import server.DatabaseManager;

/**
 * Класс, реализующий команду remove_at, удаляющую элемент коллекции по его индексу.
 */

public class RemoveAtCommand extends BaseCommand {

    public RemoveAtCommand(String name, String description, CollectionManager manager, DatabaseManager databaseManager) {
        super(name, description, manager, databaseManager);
    }

    /**
     * Метод, реализующий логику команды remove_at.
     *
     * @param request - объект класса Request
     */

    public Response execute(Request request) {
        try {
            collectionManager.removeElementAt(Integer.parseInt(request.getArgs()[0]));
            return new Response("Элемент успешно удален");
        } catch (ArrayIndexOutOfBoundsException e) {
            return new Response("Элемента с таким индексом не существует. Проверьте, что это число больше 0 и меньше размера коллекции", false);
        }
    }
}
