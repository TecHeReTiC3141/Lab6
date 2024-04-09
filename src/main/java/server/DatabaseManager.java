package server;

import java.sql.*;

public class DatabaseManager {
    private final String URL;
    private final String username;
    private final String password;
    private Connection connection;

    private static final String peppering = "s*alty_p1ep_+*D__-per438";

    // Request patterns
    private static final String ADD_USER_REQUEST = "INSERT INTO \"user\" (username, password) VALUES (?, ?)";

    private static final String EXISTS_USER_REQUEST = "SELECT EXISTS(SELECT 1 FROM \"user\" WHERE username = ?)";
    private static final String GET_USER_BY_USERNAME =
            "SELECT id, username, password FROM \"user\" WHERE username = ?)";

    public DatabaseManager(String URL, String username, String password) {
        this.URL = URL;
        this.username = username;
        this.password = password;
        connectToDatabase();
    }

    public void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(URL, username, password);
            System.out.println("Connected to the PostgreSQL DB successfully.");
        } catch (SQLException e) {
            System.out.println("Couldn't connect to DB. Reason: " + e.getMessage());
            System.exit(-1);
        }
    }

    public boolean registerUser(String username, String password) {
        if (userExists(username)) return false;
        try (PreparedStatement addStatement = connection.prepareStatement(ADD_USER_REQUEST)) {
            addStatement.setString(1, username);
            addStatement.setString(2, DataHasher.encryptStringSHA224(password + peppering));
            addStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't register user. Reason: " + e.getMessage());
            return false;
        }
    }

    private boolean checkCredentials(String username, String password) {
        try (PreparedStatement statement = connection.prepareStatement(EXISTS_USER_REQUEST)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            String hashed = rs.getString("password");
            String tryHash = DataHasher.encryptStringSHA224(password + peppering);
            if (hashed == null || tryHash == null) return false;
            return tryHash.equals(hashed);
        } catch (SQLException e) {
            System.out.println("Couldn't check if user exists. Reason: " + e.getMessage());
            return false;
        }
    }

    private boolean userExists(String username) {
        try (PreparedStatement statement = connection.prepareStatement(GET_USER_BY_USERNAME)) {
            return statement.executeQuery().getBoolean(1);
        } catch (SQLException e) {
            System.out.println("Couldn't check if user exists. Reason: " + e.getMessage());
            return false;
        }
    }
}
