package server;

import common.exceptions.InvalidDataBaseEntryException;
import common.routeClasses.Coordinates;
import common.routeClasses.LocationFrom;
import common.routeClasses.LocationTo;
import common.routeClasses.Route;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DatabaseManager {
    private final String URL;
    private final String username;
    private final String password;

    private final Logger logger;
    private Connection connection;

    private static final String peppering = "s*alty_p1ep_+*D__-per438";

    // Request patterns
    private static final String ADD_USER_REQUEST = "INSERT INTO \"user\" (username, password) VALUES (?, ?)";

    private static final String EXISTS_USER_REQUEST = "SELECT EXISTS(SELECT 1 FROM \"user\" WHERE username = ?)";
    private static final String GET_USER_BY_USERNAME =
            "SELECT id, username, password FROM \"user\" WHERE username = ?";

    private static final String GET_ROUTE_BY_ID = "SELECT * FROM route WHERE id = ?";

    private static final String GET_MAX_USER_ID = "SELECT currval('user_id_seq')";
    private static final String GET_MAX_ROUTE_ID = "SELECT currval('route_id_seq')";

    private static final String INSERT_ROUTE = "INSERT INTO route (user_id, name, coordinates_x, coordinates_y, " +
            "from_x, from_y, from_z, to_x, to_y, to_z, to_name, distance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_ROUTE_BY_ID = "UPDATE route " +
            "SET user_id = ?, name = ?, coordinates_x = ?, coordinates_y = ?, " +
            "from_x = ?, from_y = ?, from_z = ?, to_x = ?, to_y = ?, to_z = ?, to_name = ?, distance = ?" +
            "WHERE id = ?";

    public DatabaseManager(String URL, String username, String password, Logger logger) {
        this.URL = URL;
        this.username = username;
        this.password = password;
        this.logger = logger;
        connectToDatabase();
    }

    public void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(URL, username, password);
            logger.info("Connected to the PostgreSQL DB successfully.");
        } catch (SQLException e) {
            logger.error("Couldn't connect to DB. Reason: " + e.getMessage());
            System.exit(-1);
        }
    }


    public ArrayList<Route> loadDataFromDatabase() {
        ArrayList<Route> routes = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM route");
            while (rs.next()) {
                try {
                    Route route = extractRouteFromEntry(rs);
                    routes.add(route);
                } catch (InvalidDataBaseEntryException e) {
                    logger.error("Invalid route entry in DB. Reason: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            logger.error("Couldn't load data from DB. Reason: " + e.getMessage());
            System.exit(-1);
        }
        return routes;
    }

    public int getLastUserId() {
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(GET_MAX_USER_ID);
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            logger.error("Couldn't get last user id. Reason: " + e.getMessage());
            return -1;
        }
    }

    public int getLastRouteId() {
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(GET_MAX_ROUTE_ID);
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            logger.error("Couldn't get last route id. Reason: " + e.getMessage());
            return -1;
        }
    }

    public boolean registerUser(String username, String password) {
        if (userExists(username)) return false;
        try (PreparedStatement addStatement = connection.prepareStatement(ADD_USER_REQUEST)) {
            addStatement.setString(1, username);
            addStatement.setString(2, DataHasher.encryptStringSHA384(password + peppering));
            addStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error("Couldn't register user. Reason: " + e.getMessage());
            return false;
        }
    }

    public boolean checkCredentials(String username, String password) {
        try (PreparedStatement statement = connection.prepareStatement(GET_USER_BY_USERNAME)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String hashed = rs.getString("password");
                String tryHash = DataHasher.encryptStringSHA384(password + peppering);
                if (hashed == null || tryHash == null) return false;
                return tryHash.equals(hashed);
            }
            return false;
        } catch (SQLException e) {
            logger.error("Couldn't check if user exists. Reason: " + e.getMessage());
            return false;
        }
    }

    private boolean userExists(String username) {
        try (PreparedStatement statement = connection.prepareStatement(GET_USER_BY_USERNAME)) {
            return statement.executeQuery().getBoolean(1);
        } catch (SQLException e) {
            logger.error("Couldn't check if user exists. Reason: " + e.getMessage());
            return false;
        }
    }

    private boolean routeExists(long id) {
        try (PreparedStatement statement = connection.prepareStatement(GET_ROUTE_BY_ID)) {
            statement.setLong(1, id);
            return statement.executeQuery().next();
        } catch (SQLException e) {
            logger.error("Couldn't check if route exists. Reason: " + e.getMessage());
            return false;
        }
    }

    public int getUserId(String username) {
        try (PreparedStatement statement = connection.prepareStatement(GET_USER_BY_USERNAME)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            return -1;
        } catch (SQLException e) {
            logger.error("Couldn't get user id. Reason: " + e.getMessage());
            return -1;
        }
    }

    public boolean isRouteOwner(String username, long routeId) {
        int userId = getUserId(username);
        if (userId == -1) return false;
        if (!routeExists(routeId)) return false;
        try (PreparedStatement routeStatement = connection.prepareStatement(GET_ROUTE_BY_ID)) {
            routeStatement.setLong(1, routeId);
            ResultSet rs = routeStatement.executeQuery();
            if (!rs.next()) return false;
            long ownerId = rs.getLong("user_id");
            return ownerId == userId;
        } catch (SQLException e) {
            logger.error("Couldn't check if user is owner. Reason: " + e.getMessage());
            return false;
        }
    }

    private Route extractRouteFromEntry(ResultSet rs) throws SQLException, InvalidDataBaseEntryException {
        Route route = new Route();
        DateTimeFormatter formatter = route.getDateFormat();
        route.setId(rs.getInt("id"));
        if (route.getId() < 0) throw new InvalidDataBaseEntryException("Invalid id");
        route.setName(rs.getString("name"));
        if (route.getName() == null || route.getName().isEmpty())
            throw new InvalidDataBaseEntryException("Invalid name");
        route.setCoordinates(new Coordinates(rs.getInt("coordinates_x"), rs.getInt("coordinates_y")));

        route.setCreationDate(ZonedDateTime.of(rs.getTimestamp("creation_date").toLocalDateTime(), ZoneId.of("UTC+3")));
        route.setFrom(new LocationFrom(rs.getInt("from_x"), rs.getLong("from_y"), rs.getDouble("from_z")));
        route.setTo(new LocationTo(rs.getFloat("to_x"), rs.getFloat("to_y"), rs.getFloat("to_z"), rs.getString("to_name")));
        route.setDistance(rs.getLong("distance"));
        if (route.getDistance() < 1) throw new InvalidDataBaseEntryException("Invalid distance");
        return route;
    }

    public void insertRouteDataIntoStatement(Route route, PreparedStatement statement) {
        try {
            statement.setString(2, route.getName());
            statement.setLong(3, route.getCoordinates().getX());
            statement.setLong(4, route.getCoordinates().getY());
            if (route.getFrom() != null) {
                statement.setInt(5, route.getFrom().getX());
                statement.setLong(6, route.getFrom().getY());
                statement.setDouble(7, route.getFrom().getZ());
            } else {
                statement.setNull(5, Types.INTEGER);
                statement.setNull(6, Types.BIGINT);
                statement.setNull(7, Types.DOUBLE);
            }
            statement.setFloat(8, route.getTo().getX());
            statement.setFloat(9, route.getTo().getY());
            statement.setDouble(10, route.getTo().getZ());
            statement.setString(11, route.getTo().getName() != null? route.getTo().getName() : "");
            statement.setDouble(12, route.getDistance());
        } catch (SQLException e) {
            logger.error("Couldn't insert route data into statement. Reason: " + e.getMessage());
        }
    }

    public boolean addRoute(Route route, String username) {
        int userId = getUserId(username);
        if (userId == -1) return false;
        try (PreparedStatement statement = connection.prepareStatement(INSERT_ROUTE)) {
            statement.setInt(1, userId);
            insertRouteDataIntoStatement(route, statement);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error("Couldn't add route. Reason: " + e.getMessage());
            return false;
        }
    }

    public boolean updateRouteById(Route route, long routeId, String username) {
        if (!isRouteOwner(username, routeId)) return false;
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_ROUTE_BY_ID)) {
            statement.setInt(1, getUserId(username));
            insertRouteDataIntoStatement(route, statement);
            statement.setLong(13, routeId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error("Couldn't update route. Reason: " + e.getMessage());
            return false;
        }
    }

}
