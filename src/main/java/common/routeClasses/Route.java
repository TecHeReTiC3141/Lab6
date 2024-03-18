package common.routeClasses;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс Route, объекты которого являются элементами коллекции.
 */
public class Route implements Comparable<Route>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private LocationFrom from; //Поле не может быть null
    private LocationTo to; //Поле может быть null
    private double distance; //Поле не может быть null, Значение поля должно быть больше 1

    private transient DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm:ss");


    /**
     * Instantiates a new Route.
     */
    public Route() {

    }

    /**
     * Instantiates a new Route.
     *
     * @param name         the name
     * @param creationDate the creation date
     * @param coordinates  the coordinates
     * @param from         the from
     * @param to           the to
     * @param distance     the distance
     */
    public Route(String name, ZonedDateTime creationDate, Coordinates coordinates, LocationFrom from, LocationTo to, double distance) {
        this.name = name;
        this.creationDate = creationDate;
        this.coordinates = coordinates;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets creation date.
     *
     * @return the creation date
     */
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Gets coordinates.
     *
     * @return the coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Gets from.
     *
     * @return the from
     */
    public LocationFrom getFrom() {
        return from;
    }

    /**
     * Gets to.
     *
     * @return the to
     */
    public LocationTo getTo() {
        return to;
    }

    /**
     * Gets distance.
     *
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    public DateTimeFormatter getDateFormat() {
        return dateFormat;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets creation date.
     *
     * @param creationDate the creation date
     */
    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Sets coordinates.
     *
     * @param coordinates the coordinates
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Sets from.
     *
     * @param from the from
     */
    public void setFrom(LocationFrom from) {
        this.from = from;
    }

    /**
     * Sets to.
     *
     * @param to the to
     */
    public void setTo(LocationTo to) {
        this.to = to;
    }

    /**
     * Sets distance.
     *
     * @param distance the distance
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    // Сортируем Route по id
    @Override
    public int compareTo(Route o) {
        return (int) (this.getId() - o.getId());
    }

    /**
     * Show values string.
     *
     * @return the string
     */
    public String showValues() {
        return "id=%s,name=%s,coordinatesX=%s,coordinatesY=%s,creationDate=%s,toX=%s,toY=%s,toZ=%s,toName=%s,distance=%s".formatted(
                getId(), getName(), getCoordinates().getX(), getCoordinates().getY(),
                getCreationDate().format(dateFormat), getTo().getX(), getTo().getY(), getTo().getZ(),
                (getTo().getName() == null) ? "null" : getTo().getName(), getDistance())
                + (getFrom() == null ? "" : ",fromX=%s,fromY=%s,fromZ=%s".formatted(getFrom().getX(),
                getFrom().getY(), getFrom().getZ()));
    }

    public String toString() {
        return "Route [%s]".formatted(this.showValues());
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject(); // Write default fields

    }

    // Implement readObject method for custom deserialization
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); // Read default fields

        // Read the pattern of the DateTimeFormatter
        dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm:ss");
    }

    /**
     * Get xml representation of Route.
     * @param document the document
     * @param root the root element of the document
     */
    public void appendNode(Document document, Element root) {
        Element id = document.createElement("id");
        id.setAttribute("value", Long.toString(getId()));
        root.appendChild(id);
        Element name = document.createElement("name");
        name.setAttribute("value", getName());
        root.appendChild(name);
        Element coordinates = document.createElement("coordinates");
        coordinates.setAttribute("x", Long.toString(getCoordinates().getX()));
        coordinates.setAttribute("y", Long.toString(getCoordinates().getY()));
        root.appendChild(coordinates);

        Element creation = document.createElement("creationDate");
        creation.setAttribute("value", getCreationDate().format(dateFormat));
        root.appendChild(creation);

        LocationFrom from = getFrom();
        if (from != null) {
            Element locationFrom = document.createElement("locationFrom");
            locationFrom.setAttribute("x", Integer.toString(from.getX()));
            locationFrom.setAttribute("y", Long.toString(from.getY()));
            locationFrom.setAttribute("z", Double.toString(from.getZ()));
            root.appendChild(locationFrom);
        }
        Element locationTo = document.createElement("locationTo");
        locationTo.setAttribute("x", Float.toString(getTo().getX()));
        locationTo.setAttribute("y", Float.toString(getTo().getY()));
        locationTo.setAttribute("z", Double.toString(getTo().getZ()));
        String toName = getTo().getName();
        if (toName != null) locationTo.setAttribute("name", toName);
        root.appendChild(locationTo);
        Element distance = document.createElement("distance");
        distance.setAttribute("value", Double.toString(getDistance()));
        root.appendChild(distance);
    }
}
