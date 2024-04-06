package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnection {
    private String URL;
    private String username;
    private String password;
    private Connection connection;

    private static final String peppering = "s*alty_p1ep_+*D__-per438";

    // Request patterns
    private static final String ADD_USER_REQUEST = "INSERT INTO \"user\" (username, password) VALUES (?, ?)";

    private static final String EXISTS_USER_REQUEST = "SELECT EXISTS(SELECT 1 FROM \"user\" WHERE username = ?)";


    public DatabaseConnection() {
        this.URL = "jdbc:postgresql://pg:5432/studs";
    }

    public DatabaseConnection(String URL, String username, String password) {
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
            addStatement.setString(2, DataHasher.encryptStringMD2(password + peppering));
            addStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't register user. Reason: " + e.getMessage());
            return false;
        }
    }

    private boolean userExists(String username) {
        try (PreparedStatement statement = connection.prepareStatement(EXISTS_USER_REQUEST)) {
            return statement.executeQuery().getBoolean(1);
        } catch (SQLException e) {
            System.out.println("Couldn't check if user exists. Reason: " + e.getMessage());
            return false;
        }
    }
}
