package common.routeClasses;

import java.util.Comparator;

/**
 * Компаратор для сортировки коллекции по убыванию дистанции, используется в команде print_field_descending_distance
 */
public class RouteDistanceComparator implements Comparator<Route> {

    @Override
    public int compare(Route r1, Route r2) {
        return (int) (r2.getDistance() - r1.getDistance());
    }
}
