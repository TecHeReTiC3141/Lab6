package common.routeClasses;


import java.io.Serializable;

/**
 * Класс, представляющий поле locationFrom класса Route.
 */
public class LocationTo implements Serializable {

    private float x;
    private Float y;//Поле не может быть null
    private double z;
    private String name; // Поле не может быть null

    /**
     * Instantiates a new Location to.
     */
    public LocationTo() {}

    /**
     * Instantiates a new Location to.
     *
     * @param x    the x
     * @param y    the y
     * @param z    the z
     * @param name the name
     */
    public LocationTo(float x, Float y, float z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * Sets x.
     *
     * @param x the x
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public Float getY() {
        return y;
    }

    /**
     * Sets y.
     *
     * @param y the y
     */
    public void setY(Float y) {
        this.y = y;
    }

    /**
     * Gets z.
     *
     * @return the z
     */
    public double getZ() {
        return z;
    }

    /**
     * Sets z.
     *
     * @param z the z
     */
    public void setZ(double z) {
        this.z = z;
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
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

}
