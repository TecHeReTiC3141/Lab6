package client.validators;

import client.Request;
import common.RouteDataValidator;
import common.exceptions.*;
import common.routeClasses.Coordinates;
import common.routeClasses.LocationFrom;
import common.routeClasses.LocationTo;
import common.routeClasses.Route;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ReadValidator extends BaseValidator {

    protected boolean needParse = true;

    @Override
    public Request validate(String command, String[] args, boolean parse){
        try {
            Route route = parse ? parseRoute(args[args.length - 1]) : readRoute();
            return super.validate(command, args, route);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Метод для чтения маршрута из ввода пользователя
     * (нужно сначала ввести name и distance, затем будут запрошены остальные данные).
     *
     * @return прочитанный объект класса Route
     * @throws InvalidNameException     если имя маршрута некорректно
     * @throws InvalidDistanceException если дистанция маршрута некорректна
     * @throws WrongArgumentsException  если введены некорректные аргументы
     */


    public Route readRoute() throws InvalidNameException, InvalidDistanceException, WrongArgumentsException {
        Route route = new Route();
        LocationTo locationTo = new LocationTo();
        LocationFrom locationFrom = new LocationFrom();
        Coordinates coordinates = new Coordinates();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Введите name - не пустую строку: ");
                String line = scanner.nextLine();
                route.setName(RouteDataValidator.checkName(line));
                break;
            } catch (InvalidNameException e) {
                System.err.println("Поле name должно быть непустой строкой");
            } catch (NoSuchElementException e) {
                System.err.println("Выход из программы...");
                System.exit(130);
            }
        }
        while (true) {
            try {
                System.out.println("Введите distance - вещественное число > 1: ");
                String line = scanner.nextLine();
                route.setDistance(RouteDataValidator.checkDistance(line));
                break;
            } catch (InvalidDistanceException e) {
                System.err.println("Поле distance должно быть вещественным числом больше 1");
            } catch (NumberFormatException e) {
                System.err.println("Введите вещественное число");
            } catch (NoSuchElementException e) {
                System.err.println("Выход из программы...");
                System.exit(130);
            }
        }
        while (true) {
            try {
                System.out.println("Введите координату X - целое число: ");
                String line = scanner.nextLine();
                coordinates.setX(Long.parseLong(line));
                break;
            } catch (NumberFormatException e) {
                System.err.println("Координата X должна быть целым числом");
            } catch (NoSuchElementException e) {
                System.err.println("Выход из программы...");
                System.exit(130);
            }
        }
        while (true) {
            try {
                System.out.println("Введите координату Y - целое число: ");
                String line = scanner.nextLine();
                coordinates.setY(Long.parseLong(line));
                break;
            } catch (NumberFormatException e) {
                System.err.println("Координата Y должна быть целым числом");
            } catch (NoSuchElementException e) {
                System.err.println(e.getMessage());
                System.exit(130);
            }
        }
        while (true) {
            try {
                System.out.println("Введите координату X пункта прибытия (To) - вещественное число: ");
                String line = scanner.nextLine();
                locationTo.setX(Float.parseFloat(line));
                break;
            } catch (NumberFormatException e) {
                System.err.println("Координата должна быть вещественным числом");
            } catch (NoSuchElementException e) {
                System.err.println("Выход из программы...");
                System.exit(130);
            }
        }
        while (true) {
            try {
                System.out.println("Введите координату Y пункта прибытия (To) - вещественное число: ");
                String line = scanner.nextLine();
                locationTo.setY(Float.parseFloat(line));
                break;
            } catch (NumberFormatException e) {
                System.err.println("Координата должна быть вещественным числом");
            } catch (NoSuchElementException e) {
                System.err.println("Выход из программы...");
                System.exit(130);
            }
        }
        while (true) {
            try {
                System.out.println("Введите координату Z пункта прибытия (To) - вещественное число: ");
                String line = scanner.nextLine();
                locationTo.setZ(Double.parseDouble(line));
                break;
            } catch (NumberFormatException e) {
                System.err.println("Координата должна быть вещественным числом");
            } catch (NoSuchElementException e) {
                System.err.println("Выход из программы...");
                System.exit(130);
            }
        }

        try {
            System.out.println("Введите название пункта отправления (возможно, пустая строка): ");
            String line = scanner.nextLine().trim();
            locationTo.setName(line.isEmpty() ? null : line);
        } catch (NoSuchElementException e) {
            System.err.println("Выход из программы...");
            System.exit(130);
        }

        boolean hasFrom;
        System.out.println("Будет ли у маршрута пункт отправления (From)? (yes/no)");
        while (true) {
            try {
                String line = scanner.nextLine().trim().toLowerCase();
                RouteDataValidator.checkIfYesOrNo(line);
                hasFrom = line.equals("yes");
                break;
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            } catch (NoSuchElementException e) {
                System.err.println("Выход из программы...");
                System.exit(130);
            }
        }
        if (hasFrom) {
            while (true) {
                try {
                    System.out.println("Введите координату X пункта отправления (From) - целое число: ");
                    String line = scanner.nextLine();
                    locationFrom.setX(Integer.parseInt(line));
                    break;
                } catch (NumberFormatException e) {
                    System.err.println("Координата должна быть целым числом");
                } catch (NoSuchElementException e) {
                    System.err.println("Выход из программы...");
                    System.exit(130);
                }
            }
            while (true) {
                try {
                    System.out.println("Введите координату Y пункта отправления (From) - целое число: ");
                    String line = scanner.nextLine();
                    locationFrom.setY(Long.parseLong(line));
                    break;
                } catch (NumberFormatException e) {
                    System.err.println("Координата должна быть целым числом");
                } catch (NoSuchElementException e) {
                    System.err.println("Выход из программы...");
                    System.exit(130);
                }
            }
            while (true) {
                try {
                    System.out.println("Введите координату Z пункта отправления (From) - вещественное число: ");
                    String line = scanner.nextLine();
                    locationFrom.setZ(Double.parseDouble(line));
                    break;
                } catch (NumberFormatException e) {
                    System.err.println("Координата должна быть вещественным числом");
                } catch (NoSuchElementException e) {
                    System.err.println("Выход из программы...");
                    System.exit(130);
                }
            }
            route.setFrom(locationFrom);
        }
        route.setCreationDate(ZonedDateTime.now());
        route.setCoordinates(coordinates);
        route.setTo(locationTo);

        return route;
    }

    /**
     * Метод для чтения маршрута из строки, содержащей все необходимые поля маршрута.
     * Используется при чтении add и update команд, запущенные в execute_script.
     *
     * @param line строка, содержащая значения полей маршрута
     * @return прочитанный объект класса Route
     * @throws InvalidNameException              если имя маршрута некорректно
     * @throws InvalidDistanceException          если дистанция маршрута некорректна
     * @throws WrongArgumentsException           если введены некорректные аргументы
     * @throws AbsentRequiredParametersException если не хватает обязательных параметров
     */


    public Route parseRoute(String line) throws InvalidNameException, InvalidDistanceException, WrongArgumentsException, AbsentRequiredParametersException {
        String[] pairs = line.trim().replace('{', ' ').replace('}', ' ')
                .replace("\"", "").replace("'", "").split(",");
        Route route = new Route();
        LocationTo locationTo = new LocationTo();
        LocationFrom locationFrom = new LocationFrom();
        Coordinates coordinates = new Coordinates();
        ArrayList<String> requiredParams = new ArrayList<>(
                List.of("name, distance, coordinatesX, coordinatesY, creationDate, toX, toY, toZ".split(", "))
        );
        int addFrom = 0;

        DateTimeFormatter formatter = route.getDateFormat();

        for (String pair : pairs) {
            String[] keyValue = pair.split("=");

            if (keyValue.length != 2) {
                throw new WrongArgumentsException("Неверное количество параметров, проверьте, что у всех переданных параметров непустое значение");
            }
            String key = keyValue[0].trim(), value = keyValue[1].trim();
            switch (key) {
                case "id":
                    route.setId(Long.parseLong(value));
                    break;
                case "name":
                    RouteDataValidator.checkName(value);
                    route.setName(value);
                    break;
                case "distance":
                    route.setDistance(RouteDataValidator.checkDistance(value));
                    break;
                case "coordinatesX":
                    coordinates.setX(Long.parseLong(value));
                    break;
                case "coordinatesY":
                    coordinates.setY(Long.parseLong(value));
                    break;
                case "creationDate":
                    route.setCreationDate(ZonedDateTime.of(LocalDateTime.parse(value, formatter), ZoneId.of("UTC+3")));
                    break;
                case "toX":
                    locationTo.setX(Float.parseFloat(value));
                    break;
                case "toY":
                    locationTo.setY(Float.parseFloat(value));
                    break;
                case "toZ":
                    locationTo.setZ(Double.parseDouble(value));
                    break;
                case "toName":
                    locationTo.setName(value);
                    break;
                case "fromX":
                    locationFrom.setX(Integer.parseInt(value));
                    ++addFrom;
                    break;
                case "fromY":
                    locationFrom.setY(Long.parseLong(value));
                    ++addFrom;
                    break;
                case "fromZ":
                    locationFrom.setZ(Double.parseDouble(value));
                    ++addFrom;
                    break;
                default:
                    throw new WrongArgumentsException("Лишнее поле: " + keyValue[0]);
            }
            requiredParams.remove(key);
        }
        if (!requiredParams.isEmpty()) {
            throw new AbsentRequiredParametersException("Не хватает обязательных параметров: " + requiredParams);
        }
        route.setCoordinates(coordinates);
        route.setTo(locationTo);
        if (addFrom == 3) {
            route.setFrom(locationFrom);
        }
        return route;
    }

    public boolean getNeedParse() {
        return needParse;
    }
}
