package server;

import common.exceptions.InvalidDataBaseEntryException;
import common.routeClasses.Coordinates;
import common.routeClasses.LocationFrom;
import common.routeClasses.LocationTo;
import common.routeClasses.Route;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.ZonedDateTime;

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

    private boolean isRouteOwner(String username, int routeId) {
        try (PreparedStatement userStatement = connection.prepareStatement(GET_USER_BY_USERNAME)) {
            userStatement.setString(1, username);
            ResultSet rs = userStatement.executeQuery();
            if (!rs.next()) return false;

            PreparedStatement routeStatement = connection.prepareStatement(GET_ROUTE_BY_ID);
            routeStatement.setInt(1, routeId);
            ResultSet rs2 = routeStatement.executeQuery();

            if (!rs2.next()) return false;
            Route route = extractRouteFromEntry(rs);
            return route.getId() == routeId;
        } catch (SQLException e) {
            logger.error("Couldn't check if user is owner. Reason: " + e.getMessage());
            return false;
        } catch (InvalidDataBaseEntryException e) {
            logger.error("Invalid route entry in DB. Reason: " + e.getMessage());
            return false;
        }
    }

    private Route extractRouteFromEntry(ResultSet rs) throws SQLException, InvalidDataBaseEntryException {
        Route route = new Route();
        route.setId(rs.getInt("id"));
        if (route.getId() < 0) throw new InvalidDataBaseEntryException("Invalid id");
        route.setName(rs.getString("name"));
        if (route.getName() == null || route.getName().isEmpty()) throw new InvalidDataBaseEntryException("Invalid name");
        route.setCoordinates(new Coordinates(rs.getInt("coordinates_x"), rs.getInt("coordinates_y")));
        route.setCreationDate(ZonedDateTime.from(rs.getTimestamp("creation_date").toLocalDateTime()));
        route.setFrom(new LocationFrom(rs.getInt("from_x"), rs.getLong("from_y"), rs.getDouble("from_z")));
        route.setTo(new LocationTo(rs.getFloat("to_x"), rs.getFloat("to_y"), rs.getFloat("to_z"), rs.getString("to_name")));
        route.setDistance(rs.getLong("distance"));
        if (route.getDistance() < 1) throw new InvalidDataBaseEntryException("Invalid distance");
        return route;
    }

}
