package common.routeClasses;

import java.io.Serializable;

/**
 * Класс, представляющий поле locationFrom класса Route.
 */
public class LocationFrom implements Serializable {

    private Integer x; //Поле не может быть null
    private Long y; //Поле не может быть null
    private Double z; //Поле может быть null

    /**
     * Instantiates a new Location from.
     */
    public LocationFrom() {
    }

    /**
     * Instantiates a new Location from.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     */
    public LocationFrom(Integer x, Long y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public Integer getX() {
        return x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public Long getY() {
        return y;
    }

    /**
     * Gets z.
     *
     * @return the z
     */
    public Double getZ() {
        return z;
    }

    /**
     * Sets x.
     *
     * @param x the x
     */
    public void setX(Integer x) {
        this.x = x;
    }

    /**
     * Sets y.
     *
     * @param y the y
     */
    public void setY(Long y) {
        this.y = y;
    }

    /**
     * Sets z.
     *
     * @param z the z coordinate
     */
    public void setZ(Double z) {
        this.z = z;
    }

}
