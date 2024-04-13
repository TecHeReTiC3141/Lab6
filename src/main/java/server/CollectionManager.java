package server;

import common.routeClasses.Route;
import common.routeClasses.RouteDistanceComparator;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Класс, отвечающий за работу коллекции
 */

public class CollectionManager {

    /**
     * Дата инициализации коллекции
     */
    private final Date initDate = new Date();

    private CopyOnWriteArrayList<Route> collection; // TODO: make collection concurrent thread-safe

    private final Logger logger;

    private final DatabaseManager databaseManager;

    public CollectionManager(CopyOnWriteArrayList<Route> collection, Logger logger, DatabaseManager databaseManager) {
        this.collection = collection;
        this.logger = logger;
        this.databaseManager = databaseManager;
    }

    /**
     * Метод, загружающий начальную коллекцию из файла в переменной окружения DATA_FILE
     */
    public void loadInitialCollection() {
        ArrayList<Route> routes = databaseManager.loadDataFromDatabase();

        collection.addAll(routes);

    }

    /**
     * Метод, возвращающий, пуста ли коллекция
     */
    public boolean getIsEmpty() {
        return collection.isEmpty();
    }

    /**
     * Метод, возвращающий название класса коллекции.
     *
     * @return название класса коллекции
     */
    public String getCollectionClassName() {
        return collection.getClass().getName();
    }

    /**
     * Метод, возвращающий дату инициализации коллекции.
     *
     * @return дата инициализации коллекции
     */

    public Date getInitDate() {
        return initDate;
    }

    /**
     * Метод, возвращающий количество элементов в коллекции.
     *
     * @return количество элементов в коллекции
     */

    public int getCollectionSize() {
        return collection.size();
    }

    /**
     * Метод, добавляющий route в коллекцию и устанавливающий id элемента при необходимости.
     *
     * @param route   элемент, который нужно добавить
     * @param silence флаг, указывающий, нужно ли выводить сообщение о добавлении элемента
     */

    public String putToCollection(Route route, boolean silence) {
        if (route.getId() == 0) {
            route.setId(databaseManager.getLastRouteId());
        }

        collection.add(route);
        if (!silence) return "Маршрут успешно добавлен в коллекцию";
        return "Silently added";
    }

    /**
     * Метод, очищающий коллекцию.
     */
    public void removeAllUserRoutes(long userId) {
        collection = collection.stream().filter(route -> route.getCreatorId() != userId)
                .collect(CopyOnWriteArrayList::new, CopyOnWriteArrayList::add, CopyOnWriteArrayList::addAll);
    }

    /**
     * Метод, возвращающий количество элементов коллекции, значение поля distance которых больше заданного.
     *
     * @param distance значение поля distance
     * @return количество элементов коллекции, значение поля distance которых больше заданного
     */
    public long countGreaterThanDistance(double distance) {
        return collection.stream().filter(route -> route.getDistance() > distance).count();
    }


    /**
     * Метод, выводящий элементы коллекции в порядке возрастания.
     */

    public String printAscendingCommand() {
        if (collection.isEmpty()) {
            return "Коллекция пуста";
        }
        StringBuilder result = new StringBuilder();
        collection.stream()
                .sorted()
                .forEach(r -> result.append(r.toString()).append('\n'));
        return result.toString();
    }

    /**
     * Метод, выводящий поле distance элементов коллекции в порядке убывания.
     */
    public String printDescendingDistance() {
        if (collection.isEmpty()) {
            return "Коллекция пуста";
        }
        StringBuilder result = new StringBuilder("Поля distance в порядке убывания:\n");
        collection.stream().sorted(new RouteDistanceComparator()).forEach(r -> result.append("%s - %s%n".formatted(r.getId(), r.getDistance())));
        return result.toString();
    }

    /**
     * Метод, удаляющий элемент коллекции по его индексу в коллекции.
     *
     * @param index индекс элемента, который нужно удалить
     */

    public Long getElementIdAtIndex(int index) {
        if (index < 0 || index >= collection.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return collection.get(index).getId();
    }

    public void removeElementAt(int index) {
        collection.remove(index);
    }

    /**
     * Метод, удаляющий элемент коллекции по его id.
     *
     * @param id id элемента, который нужно удалить
     */

    public String removeById(long id) {
        if (collection.isEmpty()) {
            return "Коллекция пуста";
        }
        boolean removed = collection.removeIf(route -> route.getId() == id);
        if (removed) {
            return "Элемент успешно удален";
        }
        return "Элемент с таким id не найден";

    }

    /**
     * Метод, меняющий порядок элементов коллекции на обратный.
     */
    public void reorder() {
        Collections.reverse(collection);
    }

    /**
     * Метод, выводящий коллекцию.
     */

    public String showCollection() {
        if (collection.isEmpty()) {
            return "Коллекция пуста";
        }
        StringBuilder result = new StringBuilder("Содержимое коллекции: \n");
        collection.stream().forEach(r -> result.append(r.toString()).append('\n'));
        return result.toString();
    }

    /**
     * Метод, сортирующий коллекцию в естественном порядке.
     */
    public void sortCollection() {
        this.collection = collection.stream().sorted()
                .collect(CopyOnWriteArrayList::new, CopyOnWriteArrayList::add, CopyOnWriteArrayList::addAll);
    }

    /**
     * Метод, проверяющий, есть ли элемент с данным id в коллекции.
     *
     * @param id id искомого элемента
     * @return true, если элемент найден, иначе - false
     */

    public boolean findElementById(long id) {
        return collection.stream().anyMatch(r -> r.getId() == id);
    }


    /**
     * Метод, обновляющий элемент коллекции по его id, заменяя его другим элементом.
     *
     * @param id       id элемента, который нужно обновить
     * @param newRoute новый элемент
     */
    public String updateElementById(long id, Route newRoute) {
        for (Route route : collection) {
            if (route.getId() == id) {
                collection.remove(route);
                collection.add(newRoute);
                return "Элемент с id " + id + " успешно обновлен.";
            }
        }
        return "Не найдено";
    }
}
