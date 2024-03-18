package common;

import common.exceptions.InvalidDistanceException;
import common.exceptions.InvalidNameException;

/**
* Класс, содержащий методы для проверки валидности ввода пользователя.
 */

public class RouteDataValidator {

    /**
     * Проверка валидности имени (не пустое).
     * @param name имя
     * @return валидное имя
     * @throws InvalidNameException исключение, если имя невалидное
     */
    public static String checkName(String name) throws InvalidNameException {
        if (name == null || name.isEmpty()) {
            throw new InvalidNameException();
        }
        return name;
    }

    /**
     * Проверка валидности дистанции (больше 1).
     * @param distance дистанция
     * @return валидная дистанция
     * @throws InvalidDistanceException исключение, если дистанция невалидная
     */
    public static double checkDistance(String distance) throws InvalidDistanceException {
        double d = Double.parseDouble(distance);
        if (d <= 1d) {
            throw new InvalidDistanceException();
        }
        return d;
    }

    /**
     * Проверка на валидность ответа (yes или no).
     * @param answer ответ
     */
    public static void checkIfYesOrNo(String answer) {
        if (!answer.equals("yes") && !answer.equals("no")) {
            throw new IllegalArgumentException("Введите yes или no");
        }
    }
}
